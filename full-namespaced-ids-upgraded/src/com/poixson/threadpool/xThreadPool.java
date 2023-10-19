package com.poixson.threadpool;

import com.poixson.exceptions.ContinueException;
import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.logger.xLog;
import com.poixson.threadpool.types.xThreadPool_Main;
import com.poixson.tools.Keeper;
import com.poixson.tools.abstractions.RunnableMethod;
import com.poixson.tools.abstractions.xStartable;
import com.poixson.tools.xTime;
import com.poixson.utils.Utils;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public abstract class xThreadPool implements xStartable, Runnable {
  public static final int HARD_MAX_WORKERS = 100;
  
  public static final int DEFAULT_THREAD_PRIORITY = 5;
  
  public final String poolName;
  
  public enum TaskPriority {
    NOW, LATER, LAZY;
  }
  
  protected final AtomicBoolean running = new AtomicBoolean(false);
  
  protected final AtomicBoolean stopping = new AtomicBoolean(false);
  
  protected static final AtomicBoolean StoppingAll = new AtomicBoolean(false);
  
  protected static final ConcurrentHashMap<String, xThreadPool> pools = new ConcurrentHashMap<>(3);
  
  protected final AtomicBoolean keepOneAlive = new AtomicBoolean(true);
  
  protected final ThreadGroup threadGroup;
  
  protected final AtomicInteger threadPriority = new AtomicInteger(5);
  
  protected final LinkedBlockingDeque<xThreadPoolTask> queueNow = new LinkedBlockingDeque<>();
  
  protected final LinkedBlockingDeque<xThreadPoolTask> queueLater = new LinkedBlockingDeque<>();
  
  protected final LinkedBlockingDeque<xThreadPoolTask> queueLazy = new LinkedBlockingDeque<>();
  
  protected final AtomicLong countNow = new AtomicLong(0L);
  
  protected final AtomicLong countLater = new AtomicLong(0L);
  
  protected final AtomicLong countLazy = new AtomicLong(0L);
  
  protected final AtomicLong idleLoopCount = new AtomicLong(0L);
  
  protected final AtomicLong taskIndexCount = new AtomicLong(0L);
  
  private final AtomicReference<SoftReference<xLog>> _log;
  
  public static xThreadPool get(String poolName) {
    return pools.get(poolName);
  }
  
  public void start() {
    if (!okStart())
      return; 
    startNewWorkerIfNeededAndAble();
    Keeper.add(this);
  }
  
  public void run() {
    go();
  }
  
  protected boolean okStart() {
    if (StoppingAll.get())
      return false; 
    if (isRunning())
      return false; 
    this.stopping.set(false);
    queueStartupTask();
    return true;
  }
  
  protected void queueStartupTask() {
    xThreadPoolTask task = new xThreadPoolTask(this, "Pool-Startup") {
        public void run() {
          xThreadPool.this.log()
            .fine("Thread queue is running..", new Object[0]);
        }
      };
    this.queueLater.addFirst(task);
  }
  
  public void stop() {
    if (this.stopping.compareAndSet(false, true))
      stopWorkers(); 
    Keeper.remove(this);
  }
  
  public static void StopAll() {
    StoppingAll.set(true);
    Iterator<xThreadPool> it = pools.values().iterator();
    while (it.hasNext()) {
      xThreadPool pool = it.next();
      if (pool.isMainPool() || pool.isGraphicsPool())
        continue; 
      pool.stop();
    } 
  }
  
  public void join(xTime time) {
    if (time == null) {
      join(0L);
    } else {
      join(time.ms());
    } 
  }
  
  public void join() {
    join(0L);
  }
  
  public boolean isRunning() {
    return this.running.get();
  }
  
  public boolean isStopping() {
    if (StoppingAll.get())
      return true; 
    return this.stopping.get();
  }
  
  public static boolean isStoppingAll() {
    return StoppingAll.get();
  }
  
  public boolean isMainPool() {
    return false;
  }
  
  public boolean isGraphicsPool() {
    return false;
  }
  
  public long getNextTaskIndex() {
    return this.taskIndexCount
      .incrementAndGet();
  }
  
  public boolean isEmpty() {
    if (!this.queueLazy.isEmpty())
      return false; 
    if (!this.queueLater.isEmpty())
      return false; 
    if (!this.queueNow.isEmpty())
      return false; 
    return true;
  }
  
  public xThreadPoolTask grabNextTask() throws InterruptedException {
    while (true) {
      if (isStopping())
        return null; 
      xThreadPoolTask xThreadPoolTask1 = this.queueNow.poll();
      if (xThreadPoolTask1 != null) {
        this.countNow.incrementAndGet();
        return xThreadPoolTask1;
      } 
      xThreadPoolTask1 = this.queueLater.poll();
      if (xThreadPoolTask1 != null) {
        this.countLater.incrementAndGet();
        return xThreadPoolTask1;
      } 
      int index = (int)(this.idleLoopCount.incrementAndGet() % getLowLoopCount());
      if (index == 0) {
        xThreadPoolTask xThreadPoolTask = this.queueLazy.poll();
        if (xThreadPoolTask != null) {
          this.idleLoopCount.set(-1L);
          this.countLazy.incrementAndGet();
          return xThreadPoolTask;
        } 
      } 
      xThreadPoolTask task = this.queueNow.poll(
          getHighLoopWait(), TimeUnit.MILLISECONDS);
      if (task != null) {
        this.countNow.incrementAndGet();
        return task;
      } 
    } 
  }
  
  public xThreadPoolTask addTask(TaskPriority priority, Runnable run) {
    return addTask(priority, null, run);
  }
  
  public xThreadPoolTask addTask(TaskPriority priority, String taskName, Runnable run) {
    if (run instanceof xThreadPoolTask) {
      xThreadPoolTask xThreadPoolTask = (xThreadPoolTask)run;
      if (Utils.notEmpty(taskName))
        xThreadPoolTask.setTaskName(taskName); 
      addTask(priority, xThreadPoolTask);
      return xThreadPoolTask;
    } 
    xThreadPoolTask task = new xThreadPoolTask(this, taskName, run);
    addTask(priority, task);
    return task;
  }
  
  public void addTask(TaskPriority priority, xThreadPoolTask task) {
    if (task == null)
      throw new RequiredArgumentException("task"); 
    if (getMaxWorkers() == 0) {
      xThreadPool_Main.Get()
        .addTask(priority, task);
      return;
    } 
    TaskPriority pri = (priority == null) ? TaskPriority.LATER : priority;
    LinkedBlockingDeque<xThreadPoolTask> queue = getQueueByPriority(pri);
    int maxAddAttempts = getMaxAddAttempts();
    long addTimeout = getAddTimeout();
    try {
      boolean success = false;
      for (int i = 0; i < maxAddAttempts; i++) {
        success = queue.offer(task, addTimeout, TimeUnit.MILLISECONDS);
        if (success)
          break; 
      } 
      if (isRunning())
        startNewWorkerIfNeededAndAble(); 
      if (!success) {
        log().warning("Thread queue %s jammed!", new Object[] { pri.name() });
        switch (priority) {
          case NOW:
            log().warning("Thread queue jammed, trying a lower priority.. (high->norm)", new Object[] { task.getTaskName() });
            addTask(TaskPriority.LATER, task);
            return;
          case LATER:
            log().warning("Thread queue jammed, trying a lower priority.. (norm->low)", new Object[] { task.getTaskName() });
            addTask(TaskPriority.LAZY, task);
            return;
        } 
        throw new RuntimeException("Timeout queueing task: " + task.getTaskName());
      } 
    } catch (InterruptedException ignore) {
      throw new RuntimeException("Interrupted queueing task: " + task.getTaskName());
    } 
  }
  
  protected LinkedBlockingDeque<xThreadPoolTask> getQueueByPriority(TaskPriority priority) {
    if (priority == null)
      throw new RequiredArgumentException("priority"); 
    switch (priority) {
      case NOW:
        return this.queueNow;
      case LATER:
        return this.queueLater;
      case LAZY:
        return this.queueLazy;
    } 
    throw new UnsupportedOperationException("Unknown task priority: " + priority.toString());
  }
  
  public void runTaskNow(Runnable run) {
    addTask(TaskPriority.NOW, run);
  }
  
  public void runTaskNow(String taskName, Runnable run) {
    addTask(TaskPriority.NOW, taskName, run);
  }
  
  public void runTaskNow(xThreadPoolTask task) {
    addTask(TaskPriority.NOW, task);
  }
  
  public void runTaskLater(Runnable run) {
    addTask(TaskPriority.LATER, run);
  }
  
  public void runTaskLater(String taskName, Runnable run) {
    addTask(TaskPriority.LATER, taskName, run);
  }
  
  public void runTaskLater(xThreadPoolTask task) {
    addTask(TaskPriority.LATER, task);
  }
  
  public void runTaskLazy(Runnable run) {
    addTask(TaskPriority.LAZY, run);
  }
  
  public void runTaskLazy(String taskName, Runnable run) {
    addTask(TaskPriority.LAZY, taskName, run);
  }
  
  public void runTaskLazy(xThreadPoolTask task) {
    addTask(TaskPriority.LAZY, task);
  }
  
  public boolean proper(Object callingFrom, String methodName, TaskPriority priority, Object... args) {
    if (callingFrom == null)
      throw new RequiredArgumentException("callingFrom"); 
    if (Utils.isEmpty(methodName))
      throw new RequiredArgumentException("methodName"); 
    if (priority == null)
      throw new RequiredArgumentException("priority"); 
    if (isCurrentThread())
      return false; 
    RunnableMethod<Object> run = new RunnableMethod(callingFrom, methodName, args);
    addTask(priority, (Runnable)run);
    return true;
  }
  
  public boolean proper(Object callingFrom, String methodName, Object... args) {
    return proper(callingFrom, methodName, TaskPriority.LATER, args);
  }
  
  public <V> V properResult(Object callingFrom, String methodName, TaskPriority priority, Object... args) throws ContinueException {
    if (callingFrom == null)
      throw new RequiredArgumentException("callingFrom"); 
    if (Utils.isEmpty(methodName))
      throw new RequiredArgumentException("methodName"); 
    if (priority == null)
      throw new RequiredArgumentException("priority"); 
    if (isCurrentThread())
      throw new ContinueException(); 
    RunnableMethod<V> run = new RunnableMethod(callingFrom, methodName, args);
    addTask(priority, (Runnable)run);
    return (V)run.getResult();
  }
  
  public <V> V properResult(Object callingFrom, String methodName, Object... args) throws ContinueException {
    return properResult(callingFrom, methodName, TaskPriority.NOW, args);
  }
  
  public <E extends Exception> void threadOrException(E e) throws E {
    if (!isCurrentThread())
      throw e; 
  }
  
  public void threadOrException() throws RuntimeException {
    threadOrException(new RuntimeException());
  }
  
  public String getPoolName() {
    return this.poolName;
  }
  
  public int getThreadPriority() {
    return this.threadPriority.get();
  }
  
  public void setThreadPriority(int priority) {
    this.threadPriority.set(priority);
    this.threadGroup.setMaxPriority(priority);
  }
  
  public ThreadGroup getThreadGroup() {
    return this.threadGroup;
  }
  
  public long getHighLoopWait() {
    return 25L;
  }
  
  public int getLowLoopCount() {
    return 5;
  }
  
  public long getAddTimeout() {
    return 100L;
  }
  
  public int getMaxAddAttempts() {
    return 5;
  }
  
  public long getQueueCountNow() {
    return this.countNow.get();
  }
  
  public long getQueueCountLater() {
    return this.countLater.get();
  }
  
  public long getQueueCountLazy() {
    return this.countLazy.get();
  }
  
  public long getQueueCount(TaskPriority priority) {
    switch (priority) {
      case NOW:
        return this.countNow.get();
      case LATER:
        return this.countLater.get();
      case LAZY:
        return this.countLazy.get();
    } 
    return -1L;
  }
  
  public long getTaskCountTotal() {
    long count = 0L;
    count += this.countLazy.get();
    count += this.countLater.get();
    count += this.countNow.get();
    return count;
  }
  
  public long getRunCount() {
    xThreadPoolWorker[] workers = getWorkers();
    if (workers == null)
      return 0L; 
    long count = 0L;
    for (xThreadPoolWorker worker : workers)
      count += worker.getRunCount(); 
    return count;
  }
  
  public long getIdleLoops() {
    return this.idleLoopCount.get();
  }
  
  public int getQueueCount() {
    int count = this.queueLazy.size();
    count += this.queueLater.size();
    count += this.queueNow.size();
    return count;
  }
  
  public String getStatsDisplay() {
    return 
      String.format("Queued: %d  Threads: %d[%d]  Active/Free: %d/%d  Finished: %d", new Object[] { Integer.valueOf(getQueueCount()), 
          Integer.valueOf(getWorkerCount()), 
          Integer.valueOf(getMaxWorkers()), 
          Integer.valueOf(getActiveWorkerCount()), 
          Integer.valueOf(getInactiveWorkerCount()), 
          Long.valueOf(getRunCount()) });
  }
  
  protected xThreadPool(String poolName) {
    this._log = new AtomicReference<>(null);
    if (Utils.isEmpty(poolName))
      throw new RequiredArgumentException("poolName"); 
    if (StoppingAll.get())
      throw new IllegalStateException("Cannot create new thread pool, already stopping all!"); 
    this.poolName = poolName;
    this.threadGroup = new ThreadGroup(poolName);
    Keeper.add(this);
  }
  
  public xLog log() {
    SoftReference<xLog> ref = this._log.get();
    if (ref != null) {
      xLog xLog = ref.get();
      if (xLog != null)
        return xLog; 
    } 
    xLog log = _log();
    SoftReference<xLog> softReference1 = new SoftReference<>(log);
    if (this._log.compareAndSet(null, softReference1))
      return log; 
    return log();
  }
  
  protected xLog _log() {
    StringBuilder name = new StringBuilder();
    name.append("thpool-").append(getPoolName());
    return xLog.Get(name.toString());
  }
  
  public abstract void go();
  
  public abstract xThreadPoolWorker[] getWorkers();
  
  protected abstract void stopWorkers();
  
  protected abstract void startNewWorkerIfNeededAndAble();
  
  public abstract void join(long paramLong);
  
  public abstract void unregisterWorker(xThreadPoolWorker paramxThreadPoolWorker);
  
  public abstract int getActiveCount();
  
  public abstract long getNextWorkerIndex();
  
  public abstract xThreadPoolWorker getCurrentThreadWorker();
  
  public abstract boolean isCurrentThread();
  
  public abstract int getMaxWorkers();
  
  public abstract void setMaxWorkers(int paramInt);
  
  public abstract int getWorkerCount();
  
  public abstract int getActiveWorkerCount();
  
  public abstract int getInactiveWorkerCount();
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\threadpool\xThreadPool.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */
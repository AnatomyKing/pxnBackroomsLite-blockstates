package com.poixson.tools;

import com.poixson.logger.xLog;
import com.poixson.utils.Utils;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;

public class xClock {
  public static final String DEFAULT_TIMESERVER = "pool.ntp.org";
  
  public static final boolean DEFAULT_BLOCKING = true;
  
  private volatile String timeserver = null;
  
  private volatile boolean enableNTP = false;
  
  private volatile double localOffset = 0.0D;
  
  private volatile double lastChecked = 0.0D;
  
  private final AtomicReference<Thread> thread = new AtomicReference<>(null);
  
  private final AtomicBoolean running = new AtomicBoolean(false);
  
  private final AtomicReference<Condition> blockingLock = new AtomicReference<>(null);
  
  public Object clone() throws CloneNotSupportedException {
    throw new ClassCastException();
  }
  
  public void update(boolean blocking) {
    if (!this.enableNTP)
      return; 
    if (blocking) {
      doUpdate(blocking);
      return;
    } 
    if (this.thread.get() == null) {
      Thread thread = (new Thread() {
          private volatile boolean blocking = false;
          
          public Thread init(boolean blocking) {
            this.blocking = blocking;
            return this;
          }
          
          public void run() {
            xClock.this.doUpdate(this.blocking);
          }
        }).init(blocking);
      if (this.thread.compareAndSet(null, thread)) {
        thread.setDaemon(true);
        thread.setName("xClock Update");
        thread.start();
      } 
    } 
  }
  
  protected void doUpdate(boolean blocking) {
    // Byte code:
    //   0: aload_0
    //   1: getfield enableNTP : Z
    //   4: ifne -> 8
    //   7: return
    //   8: aload_0
    //   9: getfield running : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   12: iconst_0
    //   13: iconst_1
    //   14: invokevirtual compareAndSet : (ZZ)Z
    //   17: ifne -> 120
    //   20: iload_1
    //   21: ifeq -> 119
    //   24: aload_0
    //   25: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   28: invokevirtual get : ()Ljava/lang/Object;
    //   31: checkcast java/util/concurrent/locks/Condition
    //   34: astore_2
    //   35: aload_2
    //   36: ifnonnull -> 119
    //   39: new java/util/concurrent/locks/ReentrantLock
    //   42: dup
    //   43: invokespecial <init> : ()V
    //   46: invokevirtual newCondition : ()Ljava/util/concurrent/locks/Condition;
    //   49: astore_2
    //   50: aload_0
    //   51: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   54: aconst_null
    //   55: aload_2
    //   56: invokevirtual compareAndSet : (Ljava/lang/Object;Ljava/lang/Object;)Z
    //   59: ifne -> 73
    //   62: aload_0
    //   63: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   66: invokevirtual get : ()Ljava/lang/Object;
    //   69: checkcast java/util/concurrent/locks/Condition
    //   72: astore_2
    //   73: aload_0
    //   74: getfield running : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   77: invokevirtual get : ()Z
    //   80: ifne -> 86
    //   83: goto -> 115
    //   86: aload_0
    //   87: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   90: invokevirtual get : ()Ljava/lang/Object;
    //   93: ifnonnull -> 99
    //   96: goto -> 115
    //   99: aload_2
    //   100: ldc2_w 100
    //   103: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
    //   106: invokeinterface await : (JLjava/util/concurrent/TimeUnit;)Z
    //   111: pop
    //   112: goto -> 73
    //   115: goto -> 119
    //   118: astore_3
    //   119: return
    //   120: invokestatic getSystemTime : ()J
    //   123: lstore_2
    //   124: aload_0
    //   125: getfield lastChecked : D
    //   128: dconst_0
    //   129: dcmpl
    //   130: ifeq -> 148
    //   133: lload_2
    //   134: l2d
    //   135: aload_0
    //   136: getfield lastChecked : D
    //   139: dsub
    //   140: ldc2_w 60.0
    //   143: dcmpg
    //   144: ifge -> 148
    //   147: return
    //   148: aload_0
    //   149: invokevirtual log : ()Lcom/poixson/logger/xLog;
    //   152: astore #4
    //   154: aconst_null
    //   155: astore #5
    //   157: new java/net/DatagramSocket
    //   160: dup
    //   161: invokespecial <init> : ()V
    //   164: astore #5
    //   166: aload #5
    //   168: sipush #500
    //   171: invokevirtual setSoTimeout : (I)V
    //   174: aload_0
    //   175: getfield timeserver : Ljava/lang/String;
    //   178: invokestatic getByName : (Ljava/lang/String;)Ljava/net/InetAddress;
    //   181: astore #6
    //   183: new com/poixson/tools/xClockNtpMessage
    //   186: dup
    //   187: invokespecial <init> : ()V
    //   190: invokevirtual toByteArray : ()[B
    //   193: astore #7
    //   195: new java/net/DatagramPacket
    //   198: dup
    //   199: aload #7
    //   201: aload #7
    //   203: arraylength
    //   204: aload #6
    //   206: bipush #123
    //   208: invokespecial <init> : ([BILjava/net/InetAddress;I)V
    //   211: astore #8
    //   213: aload #8
    //   215: invokevirtual getData : ()[B
    //   218: bipush #40
    //   220: invokestatic fromUnixTimestamp : ()D
    //   223: invokestatic encodeTimestamp : ([BID)V
    //   226: aload #5
    //   228: aload #8
    //   230: invokevirtual send : (Ljava/net/DatagramPacket;)V
    //   233: aload #5
    //   235: aload #8
    //   237: invokevirtual receive : (Ljava/net/DatagramPacket;)V
    //   240: new com/poixson/tools/xClockNtpMessage
    //   243: dup
    //   244: aload #8
    //   246: invokevirtual getData : ()[B
    //   249: invokespecial <init> : ([B)V
    //   252: astore #9
    //   254: invokestatic getSystemTime : ()J
    //   257: lstore_2
    //   258: aload_0
    //   259: aload #9
    //   261: getfield receiveTimestamp : D
    //   264: aload #9
    //   266: getfield originateTimestamp : D
    //   269: dsub
    //   270: aload #9
    //   272: getfield transmitTimestamp : D
    //   275: lload_2
    //   276: l2d
    //   277: invokestatic fromUnixTimestamp : (D)D
    //   280: dsub
    //   281: dadd
    //   282: ldc2_w 2.0
    //   285: ddiv
    //   286: putfield localOffset : D
    //   289: aload_0
    //   290: getfield localOffset : D
    //   293: ldc2_w 0.1
    //   296: dcmpg
    //   297: ifge -> 342
    //   300: aload_0
    //   301: getfield localOffset : D
    //   304: ldc2_w -0.1
    //   307: dcmpl
    //   308: ifle -> 342
    //   311: aload #4
    //   313: ldc 'System time only off by %d, ignoring adjustment.'
    //   315: iconst_1
    //   316: anewarray java/lang/Object
    //   319: dup
    //   320: iconst_0
    //   321: ldc '0.000'
    //   323: aload_0
    //   324: getfield localOffset : D
    //   327: invokestatic FormatDecimal : (Ljava/lang/String;D)Ljava/lang/String;
    //   330: aastore
    //   331: invokevirtual info : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   334: aload_0
    //   335: dconst_0
    //   336: putfield localOffset : D
    //   339: goto -> 425
    //   342: aload #4
    //   344: ldc 'Internal time adjusted by %s%s seconds'
    //   346: iconst_2
    //   347: anewarray java/lang/Object
    //   350: dup
    //   351: iconst_0
    //   352: aload_0
    //   353: getfield localOffset : D
    //   356: dconst_0
    //   357: dcmpl
    //   358: ifle -> 366
    //   361: ldc '+'
    //   363: goto -> 368
    //   366: ldc '-'
    //   368: aastore
    //   369: dup
    //   370: iconst_1
    //   371: ldc '0.000'
    //   373: aload_0
    //   374: getfield localOffset : D
    //   377: invokestatic FormatDecimal : (Ljava/lang/String;D)Ljava/lang/String;
    //   380: aastore
    //   381: invokevirtual info : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   384: aload #4
    //   386: ldc 'System time:   %s'
    //   388: iconst_1
    //   389: anewarray java/lang/Object
    //   392: dup
    //   393: iconst_0
    //   394: lload_2
    //   395: l2d
    //   396: ldc2_w 1000.0
    //   399: ddiv
    //   400: invokestatic timestampToString : (D)Ljava/lang/String;
    //   403: aastore
    //   404: invokevirtual info : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   407: aload #4
    //   409: ldc 'Internal time: %s'
    //   411: iconst_1
    //   412: anewarray java/lang/Object
    //   415: dup
    //   416: iconst_0
    //   417: aload_0
    //   418: invokevirtual getString : ()Ljava/lang/String;
    //   421: aastore
    //   422: invokevirtual info : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   425: aload #5
    //   427: invokestatic SafeClose : (Ljava/io/Closeable;)V
    //   430: aload_0
    //   431: lload_2
    //   432: l2d
    //   433: putfield lastChecked : D
    //   436: aload_0
    //   437: getfield running : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   440: iconst_0
    //   441: invokevirtual set : (Z)V
    //   444: aload_0
    //   445: getfield thread : Ljava/util/concurrent/atomic/AtomicReference;
    //   448: aconst_null
    //   449: invokevirtual set : (Ljava/lang/Object;)V
    //   452: aload_0
    //   453: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   456: invokevirtual get : ()Ljava/lang/Object;
    //   459: checkcast java/util/concurrent/locks/Condition
    //   462: astore #6
    //   464: aload #6
    //   466: ifnull -> 489
    //   469: aload #6
    //   471: invokeinterface signalAll : ()V
    //   476: goto -> 481
    //   479: astore #7
    //   481: aload_0
    //   482: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   485: aconst_null
    //   486: invokevirtual set : (Ljava/lang/Object;)V
    //   489: goto -> 865
    //   492: astore #6
    //   494: aload #4
    //   496: aload #6
    //   498: invokevirtual trace : (Ljava/lang/Throwable;)V
    //   501: aload #5
    //   503: invokestatic SafeClose : (Ljava/io/Closeable;)V
    //   506: aload_0
    //   507: lload_2
    //   508: l2d
    //   509: putfield lastChecked : D
    //   512: aload_0
    //   513: getfield running : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   516: iconst_0
    //   517: invokevirtual set : (Z)V
    //   520: aload_0
    //   521: getfield thread : Ljava/util/concurrent/atomic/AtomicReference;
    //   524: aconst_null
    //   525: invokevirtual set : (Ljava/lang/Object;)V
    //   528: aload_0
    //   529: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   532: invokevirtual get : ()Ljava/lang/Object;
    //   535: checkcast java/util/concurrent/locks/Condition
    //   538: astore #6
    //   540: aload #6
    //   542: ifnull -> 565
    //   545: aload #6
    //   547: invokeinterface signalAll : ()V
    //   552: goto -> 557
    //   555: astore #7
    //   557: aload_0
    //   558: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   561: aconst_null
    //   562: invokevirtual set : (Ljava/lang/Object;)V
    //   565: goto -> 865
    //   568: astore #6
    //   570: aload #4
    //   572: aload #6
    //   574: invokevirtual trace : (Ljava/lang/Throwable;)V
    //   577: aload #5
    //   579: invokestatic SafeClose : (Ljava/io/Closeable;)V
    //   582: aload_0
    //   583: lload_2
    //   584: l2d
    //   585: putfield lastChecked : D
    //   588: aload_0
    //   589: getfield running : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   592: iconst_0
    //   593: invokevirtual set : (Z)V
    //   596: aload_0
    //   597: getfield thread : Ljava/util/concurrent/atomic/AtomicReference;
    //   600: aconst_null
    //   601: invokevirtual set : (Ljava/lang/Object;)V
    //   604: aload_0
    //   605: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   608: invokevirtual get : ()Ljava/lang/Object;
    //   611: checkcast java/util/concurrent/locks/Condition
    //   614: astore #6
    //   616: aload #6
    //   618: ifnull -> 641
    //   621: aload #6
    //   623: invokeinterface signalAll : ()V
    //   628: goto -> 633
    //   631: astore #7
    //   633: aload_0
    //   634: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   637: aconst_null
    //   638: invokevirtual set : (Ljava/lang/Object;)V
    //   641: goto -> 865
    //   644: astore #6
    //   646: aload #4
    //   648: aload #6
    //   650: invokevirtual trace : (Ljava/lang/Throwable;)V
    //   653: aload #5
    //   655: invokestatic SafeClose : (Ljava/io/Closeable;)V
    //   658: aload_0
    //   659: lload_2
    //   660: l2d
    //   661: putfield lastChecked : D
    //   664: aload_0
    //   665: getfield running : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   668: iconst_0
    //   669: invokevirtual set : (Z)V
    //   672: aload_0
    //   673: getfield thread : Ljava/util/concurrent/atomic/AtomicReference;
    //   676: aconst_null
    //   677: invokevirtual set : (Ljava/lang/Object;)V
    //   680: aload_0
    //   681: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   684: invokevirtual get : ()Ljava/lang/Object;
    //   687: checkcast java/util/concurrent/locks/Condition
    //   690: astore #6
    //   692: aload #6
    //   694: ifnull -> 717
    //   697: aload #6
    //   699: invokeinterface signalAll : ()V
    //   704: goto -> 709
    //   707: astore #7
    //   709: aload_0
    //   710: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   713: aconst_null
    //   714: invokevirtual set : (Ljava/lang/Object;)V
    //   717: goto -> 865
    //   720: astore #6
    //   722: aload #4
    //   724: aload #6
    //   726: invokevirtual trace : (Ljava/lang/Throwable;)V
    //   729: aload #5
    //   731: invokestatic SafeClose : (Ljava/io/Closeable;)V
    //   734: aload_0
    //   735: lload_2
    //   736: l2d
    //   737: putfield lastChecked : D
    //   740: aload_0
    //   741: getfield running : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   744: iconst_0
    //   745: invokevirtual set : (Z)V
    //   748: aload_0
    //   749: getfield thread : Ljava/util/concurrent/atomic/AtomicReference;
    //   752: aconst_null
    //   753: invokevirtual set : (Ljava/lang/Object;)V
    //   756: aload_0
    //   757: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   760: invokevirtual get : ()Ljava/lang/Object;
    //   763: checkcast java/util/concurrent/locks/Condition
    //   766: astore #6
    //   768: aload #6
    //   770: ifnull -> 793
    //   773: aload #6
    //   775: invokeinterface signalAll : ()V
    //   780: goto -> 785
    //   783: astore #7
    //   785: aload_0
    //   786: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   789: aconst_null
    //   790: invokevirtual set : (Ljava/lang/Object;)V
    //   793: goto -> 865
    //   796: astore #10
    //   798: aload #5
    //   800: invokestatic SafeClose : (Ljava/io/Closeable;)V
    //   803: aload_0
    //   804: lload_2
    //   805: l2d
    //   806: putfield lastChecked : D
    //   809: aload_0
    //   810: getfield running : Ljava/util/concurrent/atomic/AtomicBoolean;
    //   813: iconst_0
    //   814: invokevirtual set : (Z)V
    //   817: aload_0
    //   818: getfield thread : Ljava/util/concurrent/atomic/AtomicReference;
    //   821: aconst_null
    //   822: invokevirtual set : (Ljava/lang/Object;)V
    //   825: aload_0
    //   826: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   829: invokevirtual get : ()Ljava/lang/Object;
    //   832: checkcast java/util/concurrent/locks/Condition
    //   835: astore #11
    //   837: aload #11
    //   839: ifnull -> 862
    //   842: aload #11
    //   844: invokeinterface signalAll : ()V
    //   849: goto -> 854
    //   852: astore #12
    //   854: aload_0
    //   855: getfield blockingLock : Ljava/util/concurrent/atomic/AtomicReference;
    //   858: aconst_null
    //   859: invokevirtual set : (Ljava/lang/Object;)V
    //   862: aload #10
    //   864: athrow
    //   865: return
    // Line number table:
    //   Java source line number -> byte code offset
    //   #85	-> 0
    //   #87	-> 8
    //   #89	-> 20
    //   #90	-> 24
    //   #91	-> 35
    //   #92	-> 39
    //   #93	-> 50
    //   #94	-> 62
    //   #98	-> 73
    //   #99	-> 83
    //   #100	-> 86
    //   #101	-> 96
    //   #102	-> 99
    //   #104	-> 115
    //   #107	-> 119
    //   #109	-> 120
    //   #111	-> 124
    //   #112	-> 133
    //   #113	-> 147
    //   #116	-> 148
    //   #117	-> 154
    //   #119	-> 157
    //   #120	-> 166
    //   #121	-> 174
    //   #122	-> 183
    //   #123	-> 195
    //   #124	-> 213
    //   #125	-> 226
    //   #126	-> 233
    //   #127	-> 240
    //   #129	-> 254
    //   #130	-> 258
    //   #133	-> 277
    //   #136	-> 289
    //   #137	-> 311
    //   #139	-> 327
    //   #137	-> 331
    //   #141	-> 334
    //   #144	-> 342
    //   #146	-> 352
    //   #147	-> 377
    //   #144	-> 381
    //   #149	-> 384
    //   #151	-> 400
    //   #149	-> 404
    //   #153	-> 407
    //   #164	-> 425
    //   #165	-> 430
    //   #166	-> 436
    //   #167	-> 444
    //   #169	-> 452
    //   #170	-> 464
    //   #172	-> 469
    //   #173	-> 476
    //   #174	-> 481
    //   #176	-> 489
    //   #155	-> 492
    //   #156	-> 494
    //   #164	-> 501
    //   #165	-> 506
    //   #166	-> 512
    //   #167	-> 520
    //   #169	-> 528
    //   #170	-> 540
    //   #172	-> 545
    //   #173	-> 552
    //   #174	-> 557
    //   #176	-> 565
    //   #157	-> 568
    //   #158	-> 570
    //   #164	-> 577
    //   #165	-> 582
    //   #166	-> 588
    //   #167	-> 596
    //   #169	-> 604
    //   #170	-> 616
    //   #172	-> 621
    //   #173	-> 628
    //   #174	-> 633
    //   #176	-> 641
    //   #159	-> 644
    //   #160	-> 646
    //   #164	-> 653
    //   #165	-> 658
    //   #166	-> 664
    //   #167	-> 672
    //   #169	-> 680
    //   #170	-> 692
    //   #172	-> 697
    //   #173	-> 704
    //   #174	-> 709
    //   #176	-> 717
    //   #161	-> 720
    //   #162	-> 722
    //   #164	-> 729
    //   #165	-> 734
    //   #166	-> 740
    //   #167	-> 748
    //   #169	-> 756
    //   #170	-> 768
    //   #172	-> 773
    //   #173	-> 780
    //   #174	-> 785
    //   #176	-> 793
    //   #164	-> 796
    //   #165	-> 803
    //   #166	-> 809
    //   #167	-> 817
    //   #169	-> 825
    //   #170	-> 837
    //   #172	-> 842
    //   #173	-> 849
    //   #174	-> 854
    //   #176	-> 862
    //   #177	-> 865
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   35	84	2	lock	Ljava/util/concurrent/locks/Condition;
    //   183	242	6	address	Ljava/net/InetAddress;
    //   195	230	7	buf	[B
    //   213	212	8	packet	Ljava/net/DatagramPacket;
    //   254	171	9	msg	Lcom/poixson/tools/xClockNtpMessage;
    //   464	25	6	lock	Ljava/util/concurrent/locks/Condition;
    //   494	7	6	e	Ljava/net/SocketException;
    //   540	25	6	lock	Ljava/util/concurrent/locks/Condition;
    //   570	7	6	e	Ljava/net/UnknownHostException;
    //   616	25	6	lock	Ljava/util/concurrent/locks/Condition;
    //   646	7	6	e	Ljava/io/IOException;
    //   692	25	6	lock	Ljava/util/concurrent/locks/Condition;
    //   722	7	6	e	Ljava/lang/Exception;
    //   768	25	6	lock	Ljava/util/concurrent/locks/Condition;
    //   837	25	11	lock	Ljava/util/concurrent/locks/Condition;
    //   0	866	0	this	Lcom/poixson/tools/xClock;
    //   0	866	1	blocking	Z
    //   124	742	2	time	J
    //   154	712	4	log	Lcom/poixson/logger/xLog;
    //   157	709	5	socket	Ljava/net/DatagramSocket;
    // Exception table:
    //   from	to	target	type
    //   73	115	118	java/lang/InterruptedException
    //   157	425	492	java/net/SocketException
    //   157	425	568	java/net/UnknownHostException
    //   157	425	644	java/io/IOException
    //   157	425	720	java/lang/Exception
    //   157	425	796	finally
    //   469	476	479	java/lang/IllegalMonitorStateException
    //   492	501	796	finally
    //   545	552	555	java/lang/IllegalMonitorStateException
    //   568	577	796	finally
    //   621	628	631	java/lang/IllegalMonitorStateException
    //   644	653	796	finally
    //   697	704	707	java/lang/IllegalMonitorStateException
    //   720	729	796	finally
    //   773	780	783	java/lang/IllegalMonitorStateException
    //   796	798	796	finally
    //   842	849	852	java/lang/IllegalMonitorStateException
  }
  
  public boolean isRunning() {
    return this.running.get();
  }
  
  public String getTimeServer() {
    return Utils.ifEmpty(this.timeserver, "pool.ntp.org");
  }
  
  public void setTimeServer(String host) {
    this.timeserver = host;
  }
  
  public void setEnabled(boolean enabled) {
    this.enableNTP = enabled;
    if (!enabled) {
      this.localOffset = 0.0D;
      this.lastChecked = 0.0D;
    } 
  }
  
  public static long getSystemTime() {
    return System.currentTimeMillis();
  }
  
  public long getCurrentTime() {
    return getSystemTime() - (long)this.localOffset;
  }
  
  public long millis() {
    long time = getCurrentTime();
    return time;
  }
  
  public double seconds() {
    return millis() / 1000.0D;
  }
  
  protected static double fromUnixTimestamp(double timestamp) {
    return timestamp / 1000.0D + 2.2089888E9D;
  }
  
  protected static double fromUnixTimestamp() {
    return 
      fromUnixTimestamp(
        getSystemTime());
  }
  
  public static String timestampToString(double timestamp) {
    if (timestamp <= 0.0D)
      return "0"; 
    StringBuilder buf = new StringBuilder();
    buf.append((new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss"))
        
        .format(new Date((long)(timestamp * 1000.0D))));
    buf.append((new DecimalFormat("0.000"))
        
        .format(timestamp - timestamp));
    return buf.toString();
  }
  
  public String getString() {
    return timestampToString(seconds());
  }
  
  public xLog log() {
    return Utils.log();
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\xClock.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */
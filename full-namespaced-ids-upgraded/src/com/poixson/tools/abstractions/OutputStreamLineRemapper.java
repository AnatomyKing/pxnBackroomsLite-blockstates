package com.poixson.tools.abstractions;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public abstract class OutputStreamLineRemapper extends OutputStream {
  private final StringBuilder buf = new StringBuilder();
  
  public static PrintStream toPrintStream(OutputStream outstream) {
    return new PrintStream(outstream, false);
  }
  
  public abstract void line(String paramString);
  
  public void write(int b) throws IOException {
    if (b == 13)
      return; 
    if (b == 10) {
      line(this.buf
          .toString());
      this.buf.setLength(0);
      return;
    } 
    this.buf.append(
        Character.toChars(b));
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\abstractions\OutputStreamLineRemapper.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */
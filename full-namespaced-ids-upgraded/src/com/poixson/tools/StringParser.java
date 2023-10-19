package com.poixson.tools;

public class StringParser {
  public final String data;
  
  public final int data_len;
  
  public int pos = 0;
  
  public StringParser(String data) {
    this.data = data;
    this.data_len = data.length();
  }
  
  public void trim(int len) {
    this.pos += len;
  }
  
  public void trim() {
    while (!eof()) {
      char chr = this.data.charAt(this.pos);
      if (chr != ' ' && chr != '\t' && chr != '\n' && chr != '\r')
        break; 
      this.pos++;
    } 
  }
  
  public String toString() {
    if (eof())
      return ""; 
    return this.data.substring(this.pos);
  }
  
  public char getChar(int pos) {
    return this.data.charAt(pos + this.pos);
  }
  
  public char getChar() {
    return this.data.charAt(this.pos);
  }
  
  public boolean matchNext(char chr) {
    if (eof())
      return false; 
    return (this.data.charAt(this.pos) == chr);
  }
  
  public boolean matchNext(String match) {
    if (this.pos >= this.data_len - 1)
      return false; 
    return (this.data.substring(this.pos, match.length()) == match);
  }
  
  public boolean eof() {
    return (this.pos >= this.data_len - 1);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\StringParser.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */
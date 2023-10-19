package com.poixson.tools;

public class JsonChunker {
  protected final StringBuilder buffer = new StringBuilder();
  
  protected boolean insideSingleQuote = false;
  
  protected boolean insideDoubleQuote = false;
  
  protected int insideBrackets = 0;
  
  protected final ChunkProcessor processor;
  
  public JsonChunker(ChunkProcessor processor) {
    this.processor = processor;
  }
  
  public void process(String data) {
    int len = data.length();
    for (int i = 0; i < len; i++)
      process(data.charAt(i)); 
  }
  
  public void process(char chr) {
    if (chr == '\r')
      return; 
    if (this.buffer.length() == 0)
      switch (chr) {
        case '{':
          break;
        case '\t':
        case '\n':
        case ' ':
        case ',':
          return;
        default:
          throw new RuntimeException(
              String.format("JSON must start with { bracket, found %s <%d>", new Object[] { Character.valueOf(chr), 
                  Integer.valueOf(chr) }));
      }  
    switch (chr) {
      case '{':
        if (this.insideSingleQuote || 
          this.insideDoubleQuote)
          break; 
        this.insideBrackets++;
        break;
      case '}':
        if (this.insideSingleQuote || 
          this.insideDoubleQuote)
          break; 
        this.insideBrackets--;
        if (this.insideBrackets == 0) {
          this.buffer.append('}');
          this.processor.process(this.buffer.toString());
          this.buffer.setLength(0);
          return;
        } 
        if (this.insideBrackets < 0)
          throw new RuntimeException("Invalid brackets in json"); 
        break;
      case '\'':
        if (this.insideDoubleQuote)
          break; 
        this.insideSingleQuote = !this.insideSingleQuote;
        break;
      case '"':
        if (this.insideSingleQuote)
          break; 
        this.insideDoubleQuote = !this.insideDoubleQuote;
        break;
    } 
    this.buffer.append(chr);
  }
  
  public static interface ChunkProcessor {
    void process(String param1String);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\JsonChunker.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */
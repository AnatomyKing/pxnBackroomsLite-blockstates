package com.poixson.tools.byref;

public interface StringRefInterface {
  void value(String paramString);
  
  String value();
  
  boolean isEmpty();
  
  boolean notEmpty();
  
  int length();
  
  int indexOf(char paramChar);
  
  int indexOf(String paramString);
  
  int indexOf(char... paramVarArgs);
  
  int indexOf(String... paramVarArgs);
  
  int indexOfLast(char paramChar);
  
  int indexOfLast(String paramString);
  
  int indexOfLast(char... paramVarArgs);
  
  int indexOfLast(String... paramVarArgs);
  
  String cutFirstPart(char paramChar);
  
  String cutFirstPart(String paramString);
  
  String cutFirstPart(char... paramVarArgs);
  
  String cutFirstPart(String... paramVarArgs);
  
  String cutLastPart(char paramChar);
  
  String cutLastPart(String paramString);
  
  String cutLastPart(char... paramVarArgs);
  
  String cutLastPart(String... paramVarArgs);
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\byref\StringRefInterface.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */
package com.poixson.tools;

import com.poixson.utils.NumberUtils;
import com.poixson.utils.StringUtils;
import com.poixson.utils.Utils;

public class xURL {
  public String protocol = null;
  
  public String user = null;
  
  public String pass = null;
  
  public String host = null;
  
  public int port = -1;
  
  public String path = null;
  
  public static xURL getNew() {
    return new xURL();
  }
  
  public static xURL getNew(String uri) {
    return new xURL(uri);
  }
  
  public static xURL getNew(String protocol, String host) {
    return new xURL(protocol, host);
  }
  
  public static xURL getNew(String host, int port) {
    return new xURL(host, port);
  }
  
  public static xURL getNew(String protocol, String host, int port) {
    return new xURL(protocol, host, port);
  }
  
  public static xURL getNew(String protocol, String host, int port, String path) {
    return new xURL(protocol, host, port, path);
  }
  
  public static xURL getNew(String user, String pass, String protocol, String host, int port, String path) {
    return new xURL(protocol, user, pass, host, port, path);
  }
  
  public xURL() {}
  
  public xURL(String uri) {
    this();
    setURI(uri);
  }
  
  public xURL(String protocol, String host) {
    this();
    this.protocol = protocol;
    this.host = host;
  }
  
  public xURL(String host, int port) {
    this();
    this.host = host;
    this.port = port;
  }
  
  public xURL(String protocol, String host, int port) {
    this();
    this.protocol = protocol;
    this.host = host;
    this.port = port;
  }
  
  public xURL(String protocol, String host, int port, String path) {
    this();
    this.protocol = protocol;
    this.host = host;
    this.port = port;
    this.path = path;
  }
  
  public xURL(String user, String pass, String protocol, String host, int port, String path) {
    this();
    this.protocol = protocol;
    this.user = user;
    this.pass = pass;
    this.host = host;
    this.port = port;
    this.path = path;
  }
  
  public xURL reset() {
    this.protocol = null;
    this.user = null;
    this.pass = null;
    this.host = null;
    this.port = -1;
    this.path = null;
    return this;
  }
  
  public xURL setURI(String uri) {
    String buf = uri;
    reset();
    int pos = buf.indexOf("//");
    if (pos != -1) {
      this
        .protocol = StringUtils.ceTrim(buf
          .substring(0, pos), new char[] { ':' });
      buf = buf.substring(pos + 2);
    } 
    pos = buf.indexOf('@');
    if (pos != -1) {
      String str = buf.substring(0, pos);
      buf = buf.substring(pos + 1);
      pos = str.indexOf(':');
      if (pos == -1) {
        this.user = str;
      } else {
        this.user = str.substring(0, pos);
        this.pass = str.substring(pos + 1);
      } 
    } 
    pos = buf.indexOf('/');
    if (pos != -1) {
      String str = buf.substring(0, pos);
      buf = buf.substring(pos);
      pos = str.indexOf(':');
      if (pos != -1) {
        this.host = str.substring(0, pos);
        Integer i = NumberUtils.CastInteger(str
            .substring(pos + 1));
        this
          
          .port = (i == null) ? -1 : i.intValue();
      } 
    } 
    if (Utils.notEmpty(buf))
      this.path = buf; 
    return this;
  }
  
  public String toString() {
    StringBuilder str = new StringBuilder();
    String protocol = this.protocol;
    String user = this.user;
    String pass = this.pass;
    String host = this.host;
    int port = this.port;
    String path = this.path;
    if (Utils.notEmpty(protocol))
      str.append(protocol).append("://"); 
    if (Utils.notEmpty(user) || Utils.notEmpty(pass)) {
      if (Utils.notEmpty(user))
        str.append(user); 
      if (Utils.notEmpty(pass))
        str.append(':').append(pass); 
      str.append('@');
    } 
    str.append(host);
    if (port > 0)
      str.append(':').append(port); 
    if (Utils.notEmpty(path))
      str.append(path); 
    return str.toString();
  }
  
  public xURL setProtocol(String protocol) {
    this.protocol = protocol;
    return this;
  }
  
  public String getProtocol() {
    return this.protocol;
  }
  
  public boolean hasProtocol() {
    return Utils.notEmpty(this.protocol);
  }
  
  public xURL setUser(String user) {
    this.user = user;
    return this;
  }
  
  public String getUser() {
    return this.user;
  }
  
  public boolean hasUser() {
    return Utils.notEmpty(this.user);
  }
  
  public xURL setPass(String pass) {
    this.pass = pass;
    return this;
  }
  
  public String getPass() {
    return this.pass;
  }
  
  public boolean hasPass() {
    return Utils.notEmpty(this.pass);
  }
  
  public xURL setHost(String host) {
    this.host = host;
    return this;
  }
  
  public String getHost() {
    return this.host;
  }
  
  public boolean hasHost() {
    return Utils.notEmpty(this.host);
  }
  
  public xURL setPort(int port) {
    this
      
      .port = (port > 0) ? port : -1;
    return this;
  }
  
  public int getPort() {
    int port = this.port;
    return 
      (port > 0) ? 
      port : 
      -1;
  }
  
  public boolean hasPort() {
    return (this.port > 0);
  }
  
  public xURL setPath(String path) {
    this.path = path;
    return this;
  }
  
  public String getPath() {
    return this.path;
  }
  
  public boolean hasPath() {
    return Utils.notEmpty(this.path);
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\xURL.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */
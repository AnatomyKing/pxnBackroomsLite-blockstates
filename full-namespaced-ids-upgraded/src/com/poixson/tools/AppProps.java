package com.poixson.tools;

import com.poixson.exceptions.RequiredArgumentException;
import com.poixson.utils.StringUtils;
import com.poixson.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProps {
  public static final String PROPS_FILE = "app.properties";
  
  public final String name;
  
  public final String title;
  
  public final String version;
  
  public final String commitHashFull;
  
  public final String commitHashShort;
  
  public final String url;
  
  public final String orgName;
  
  public final String orgUrl;
  
  public final String issueName;
  
  public final String issueUrl;
  
  public static AppProps LoadFromClassRef(Class<?> clss) throws IOException {
    Properties props;
    InputStream in = null;
    try {
      in = clss.getResourceAsStream(
          StringUtils.ForceStarts("/", "app.properties"));
      if (in == null)
        throw new IOException(
            String.format("Failed to load %s resource from jar", new Object[] { "app.properties" })); 
      props = new Properties();
      props.load(in);
    } finally {
      Utils.SafeClose(in);
    } 
    return new AppProps(props);
  }
  
  protected AppProps(Properties props) {
    if (props == null)
      throw new RequiredArgumentException("props"); 
    this.name = props.getProperty("name");
    this.title = props.getProperty("title");
    this.version = props.getProperty("version");
    this.url = props.getProperty("url");
    this.orgName = props.getProperty("org_name");
    this.orgUrl = props.getProperty("org_url");
    this.issueName = props.getProperty("issue_name");
    this.issueUrl = props.getProperty("issue_url");
    String hash = props.getProperty("commit");
    if (Utils.isEmpty(hash)) {
      this.commitHashFull = null;
      this.commitHashShort = null;
    } else if (hash.startsWith("${")) {
      this.commitHashFull = null;
      this.commitHashShort = null;
    } else {
      this.commitHashFull = hash;
      this.commitHashShort = hash.substring(0, 7);
    } 
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\tools\AppProps.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */
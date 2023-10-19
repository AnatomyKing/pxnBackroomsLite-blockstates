package com.poixson.commonmc.tools.updatechecker;

import com.google.gson.Gson;
import com.poixson.tools.xTime;
import com.poixson.utils.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import org.bukkit.Bukkit;

public class SpigotWebAPI {
  public static final String SPIGOT_API_URL = "https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=%s";
  
  public static final String SPIGOT_RES_URL = "https://www.spigotmc.org/resources/%s/";
  
  public int id;
  
  public String title;
  
  public String tag;
  
  public String current_version;
  
  public String native_minecraft_version;
  
  public Set<String> supported_minecraft_versions;
  
  public String icon_link;
  
  public String external_download_url;
  
  public PluginStats stats;
  
  public class PluginStats {
    public int downloads;
    
    public int updates;
    
    public int rating;
  }
  
  public static SpigotWebAPI Get(int id) {
    try {
      return Get(String.format("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=%s", new Object[] { Integer.valueOf(id) }));
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    } catch (IOException iOException) {
    
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    } 
    return null;
  }
  
  public static SpigotWebAPI Get(String url) throws MalformedURLException, IOException, URISyntaxException {
    HttpURLConnection connection = (HttpURLConnection)(new URI(url)).toURL().openConnection();
    connection.setConnectTimeout((int)(new xTime("30s")).ms());
    connection.setReadTimeout((int)(new xTime("30s")).ms());
    InputStreamReader input = new InputStreamReader(connection.getInputStream());
    BufferedReader reader = new BufferedReader(input);
    StringBuilder data = new StringBuilder();
    int c;
    while ((c = reader.read()) != -1)
      data.append((char)c); 
    return GetFromData(data.toString());
  }
  
  public static SpigotWebAPI GetFromData(String data) {
    Gson gson = new Gson();
    SpigotWebAPI api = (SpigotWebAPI)gson.fromJson(data, SpigotWebAPI.class);
    return api;
  }
  
  public double diffServerVersion() {
    String server_version = Bukkit.getBukkitVersion();
    double diff = StringUtils.CompareVersions(server_version, this.native_minecraft_version);
    for (String vers : this.supported_minecraft_versions) {
      double d = StringUtils.CompareVersions(server_version, vers);
      if (diff > Math.abs(d))
        diff = Math.abs(d); 
    } 
    return diff;
  }
}


/* Location:              C:\Users\mrluu\Downloads\New folder (18)\pxnCommonPluginMC-4.0.34-SNAPSHOT.jar!\com\poixson\commonmc\tool\\updatechecker\SpigotWebAPI.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */
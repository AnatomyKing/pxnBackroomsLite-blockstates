package com.poixson.backrooms.tasks;

import com.poixson.backrooms.BackroomsPlugin;
import com.poixson.utils.FileUtils;
import com.poixson.utils.RandomUtils;
import com.poixson.utils.Utils;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class QuoteAnnouncer {
  public static final ChatColor DEFAULT_QUOTE_COLOR = ChatColor.BLACK;
  
  protected final BackroomsPlugin plugin;
  
  protected final String[] quotes;
  
  protected int lastRnd = -1;
  
  public QuoteAnnouncer(BackroomsPlugin plugin, String[] quotes) {
    this.plugin = plugin;
    this.quotes = quotes;
  }
  
  public static QuoteAnnouncer Load(BackroomsPlugin plugin) {
    LinkedList<String> quotes = new LinkedList<>();
    InputStream input = plugin.getResource("quotes.txt");
    if (input == null)
      throw new RuntimeException("Failed to load chances.json"); 
    String data = FileUtils.ReadInputStream(input);
    Utils.SafeClose(input);
    String[] array = data.split("\n");
    for (String line : array) {
      if (Utils.notEmpty(line))
        quotes.add(line.trim()); 
    } 
    return new QuoteAnnouncer(plugin, quotes.<String>toArray(new String[0]));
  }
  
  public void announce() {
    Collection<? extends Player> players = Bukkit.getOnlinePlayers();
    if (players.size() > 0) {
      String quote = "" + DEFAULT_QUOTE_COLOR + DEFAULT_QUOTE_COLOR + getQuote();
      for (Player player : players) {
        int level = this.plugin.getPlayerLevel(player);
        if (level >= 0)
          announce(quote, player); 
      } 
    } 
  }
  
  public void announce(Player player) {
    String quote = "" + DEFAULT_QUOTE_COLOR + DEFAULT_QUOTE_COLOR + getQuote();
    announce(quote, player);
  }
  
  public void announce(String quote, Player player) {
    player.sendMessage(quote);
  }
  
  public String getQuote() {
    int count = this.quotes.length;
    int rnd = RandomUtils.GetNewRandom(0, count, this.lastRnd);
    this.lastRnd = rnd;
    return this.quotes[rnd];
  }
}

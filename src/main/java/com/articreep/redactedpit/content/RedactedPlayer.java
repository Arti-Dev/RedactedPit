package com.articreep.redactedpit.content;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.content.Content;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class RedactedPlayer {
    // Houses player object
    private Player player;
    private Main plugin;
    private UUID uuid;
    private double percentContent;
    private HashSet<Content> contentDiscovered;

    public RedactedPlayer(Player player, Main plugin) {
        this.player = player;
        this.plugin = plugin;
        this.uuid = player.getUniqueId();
        this.percentContent = 0.00;
        this.contentDiscovered = new HashSet<Content>();
    }
    public Player getPlayer() {
        return player;
    }

    public void loadData() throws IOException {
        FileConfiguration config = plugin.getPlayerConfig();
        String uuidstring = uuid.toString();
        if (config.contains("players." + uuidstring)) {
            //TODO add gold
            percentContent = config.getDouble("players." + uuidstring + ".percentcontent");
            // Get list of strings
            List<String> tempList = (List<String>) config.getList("players." + uuidstring + ".content");
            // Clear hashset and load in everything
            contentDiscovered.clear();
            for (String string : tempList) {
                contentDiscovered.add(Content.valueOf(string));
            }
        } else {
            saveData();
        }
    }

    public void saveData() throws IOException {
        String uuidstring = uuid.toString();
        plugin.getPlayerConfig().set("players." + uuidstring + ".percentcontent", percentContent);
        // Convert HashSet to List<String>
        HashSet<Content> tempSet = (HashSet<Content>) contentDiscovered.clone();
        List<String> tempList = new ArrayList<String>();
        for (Content content : tempSet) {
            tempList.add(content.toString());
        }
        plugin.getPlayerConfig().set("players." + uuidstring + ".content", tempList);
        plugin.getPlayerConfig().save(plugin.getDataFile());
    }

    //TODO Add method that resets player

    public void calculatePercentContent() {
        // Length of contentDiscovered divide by total amount of content in the Content enum
        // TODO turn these into floats
        float number = (float) contentDiscovered.size() / (float) Content.values().length;
        number *= 100;
        BigDecimal bd = BigDecimal.valueOf(number);
        bd = bd.setScale(1, RoundingMode.DOWN);
        percentContent = bd.doubleValue();
    }

    public double getPercentContent() {
        return percentContent;
    }

    public void addContent(Content content) {
        this.contentDiscovered.add(content);
        // Immediately calculate percentage
        calculatePercentContent();
    }

    public void removeContent(Content content) {
        this.contentDiscovered.remove(content);
        // Immediately calculate percentage
        calculatePercentContent();
    }

    public boolean hasContent(Content content) {
        return this.contentDiscovered.contains(content);
    }
}

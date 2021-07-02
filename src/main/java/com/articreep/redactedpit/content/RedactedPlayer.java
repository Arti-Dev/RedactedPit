package com.articreep.redactedpit.content;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class RedactedPlayer {
    // Houses player object
    private final Player player;
    private final Main plugin;
    private final UUID uuid;
    private boolean importedGold;
    private double gold;
    private String fracContent;
    private double percentContent;
    private HashSet<Content> contentDiscovered;
    private boolean hundredAchieved;

    public RedactedPlayer(Player player, Main plugin) {
        this.player = player;
        this.plugin = plugin;
        this.uuid = player.getUniqueId();
        this.gold = 0.0;
        this.importedGold = false;
        this.fracContent = "0/" + Content.values().length;
        this.percentContent = 0.00;
        this.contentDiscovered = new HashSet<>();
        this.hundredAchieved = false;
    }
    public Player getPlayer() {
        return player;
    }

    public void loadData() throws IOException {
        FileConfiguration config = plugin.getPlayerConfig();
        String uuidstring = uuid.toString();
        if (config.contains("players." + uuidstring)) {
            gold = config.getDouble("players." + uuidstring + ".gold");
            importedGold = config.getBoolean("players." + uuidstring + ".importedgold");
            percentContent = config.getDouble("players." + uuidstring + ".percentcontent");
            hundredAchieved = config.getBoolean("players." + uuidstring + ".hundredachieved");
            // Get list of strings
            List<String> tempList = (List<String>) config.getList("players." + uuidstring + ".content");
            // Clear hashset and load in everything
            contentDiscovered.clear();
            for (String string : tempList) {
                contentDiscovered.add(Content.valueOf(string));
            }
            // Update percentContent just in case something new was added
            calculatePercentContent();
        } else {
            saveData();

        }
    }

    public void saveData() throws IOException {
        String uuidstring = uuid.toString();
        plugin.getPlayerConfig().set("players." + uuidstring + ".gold", gold);
        plugin.getPlayerConfig().set("players." + uuidstring + ".importedgold", importedGold);
        plugin.getPlayerConfig().set("players." + uuidstring + ".percentcontent", percentContent);
        plugin.getPlayerConfig().set("players." + uuidstring + ".hundredachieved", hundredAchieved);
        // Convert HashSet to List<String>
        HashSet<Content> tempSet = (HashSet<Content>) contentDiscovered.clone();
        List<String> tempList = new ArrayList<>();
        for (Content content : tempSet) {
            tempList.add(content.toString());
        }
        plugin.getPlayerConfig().set("players." + uuidstring + ".content", tempList);
        plugin.getPlayerConfig().save(plugin.getDataFile());
    }

    public void resetData() throws IOException {
        percentContent = 0.00;
        contentDiscovered = new HashSet<>();
        gold = 0.0;
        importedGold = false;
        fracContent = "0/" + Content.values().length;
        hundredAchieved = false;
        saveData();
    }

    public void calculatePercentContent() {
        // Length of contentDiscovered divide by total amount of content in the Content enum
        // TODO turn these into floats
        float number = (float) contentDiscovered.size() / (float) Content.values().length;
        number *= 100;
        BigDecimal bd = BigDecimal.valueOf(number);
        bd = bd.setScale(1, RoundingMode.DOWN);
        percentContent = bd.doubleValue();
        fracContent = contentDiscovered.size() + "/" + Content.values().length;
        if (percentContent >= 100 && !hundredAchieved) {
            hundredAchieved = true;
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            Utils.sendTitle(player, ChatColor.AQUA + "" + ChatColor.BOLD + "GRATS!", "You achieved 100% Content Discovered!", 5, 40, 5);

            // Not firework
            Utils.sendSnowParticles(plugin, player);

            Bukkit.broadcastMessage(ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " has achieved 100% Content Discovered!");
        } else {
            hundredAchieved = false;
        }
    }

    public double getGold() {
        return gold;
    }

    public double getPercentContent() {
        return percentContent;
    }

    public String getFracContent() {
        return fracContent;
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

    public void setGold(double amount) {
        this.gold = amount;
    }

    public boolean subtractGold(double amount) {
        if (this.gold - amount < 0) {
            return false;
        }
        this.gold -= amount;
        return true;
    }

    public void addGold(double amount) {
        this.gold += amount;
    }

    public void importedSuccessfully() {
        this.importedGold = true;
    }

    public boolean hasImportedBefore() {
        return importedGold;
    }
}

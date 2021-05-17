package com.articreep.redactedpit.content;

import com.articreep.redactedpit.content.Content;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.UUID;

public class RedactedPlayer {
    // Houses player object
    private Player player;
    private UUID uuid;
    private double percentContent;
    private HashSet<Content> contentDiscovered;

    public RedactedPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.percentContent = 0.00;
        this.contentDiscovered = new HashSet<Content>();
    }
    public Player getPlayer() {
        return player;
    }

    public void loadData() {
        //TODO add things here
    }

    public void saveData() {
        //TODO add things here
    }

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

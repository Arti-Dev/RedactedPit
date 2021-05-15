package com.articreep.redactedpit.listeners;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.UtilBoundingBox;
import com.articreep.redactedpit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ContentListeners implements Listener {
    private Main plugin;
    public static String string;
    // TODO MAKE AN ENUM FOR THIS
    private final UtilBoundingBox futureracecontent;
    public ContentListeners(Main plugin) {
        this.plugin = plugin;
        futureracecontent = new UtilBoundingBox(new Location(Bukkit.getWorld("redacted2"), 71, 47, 65),
                new Location(Bukkit.getWorld("redacted2"), 58, 62, 76));
        string = "gaming";
    }
    @EventHandler
    public void onContentDiscover(PlayerMoveEvent event) {
        Location loc = event.getTo();
        if (futureracecontent.isInBox(loc)) {
            Utils.sendActionBar(event.getPlayer(), "You discovered the Future Race!");
            string = "Future Race";
        }
    }
}

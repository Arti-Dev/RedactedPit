package com.articreep.redactedpit.content;

import com.articreep.redactedpit.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class ContentListeners implements Listener {
    private Main plugin;
    //TODO stop memory leak - remove players from hashmap when they dc
    private static HashMap<Player, RedactedPlayer> redactedPlayerHashMap = new HashMap<Player, RedactedPlayer>();
    // TODO MAKE AN ENUM FOR THIS
    public ContentListeners(Main plugin) {
        this.plugin = plugin;
    }

    //Create a RedactedPlayer object when players log in
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        RedactedPlayer redactedPlayer = new RedactedPlayer(event.getPlayer());
        redactedPlayerHashMap.put(event.getPlayer(), redactedPlayer);
        redactedPlayer.loadData();
    }
    //TODO Make the plugin create redactedplayer objects for people already on the server

    //Remove the RedactedPlayer object when players log out
    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        redactedPlayer.saveData();
        redactedPlayerHashMap.remove(event.getPlayer());
    }

    // Specifically for content that only requires user to step inside a box
    @EventHandler
    public void onContentDiscover(PlayerMoveEvent event) {
        Location loc = event.getTo();
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        // Is the user discovering the Future Race?
        if (!redactedPlayer.hasContent(Content.FUTURE_RACE_DISCOVERY)) {
            if (Content.FUTURE_RACE_DISCOVERY.getBox().isInBox(loc)) {
                redactedPlayer.addContent(Content.FUTURE_RACE_DISCOVERY);
                Utils.sendActionBar(redactedPlayer.getPlayer(), "You discovered content: " + Content.FUTURE_RACE_DISCOVERY.getId());
            }
        }
    }

    // Specifically for placement of the Sun Stone
    public static void onSunStonePlace(BlockPlaceEvent event) {
        // This method is only called from the Sun Stone event, therefore no need to verify
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        if (!redactedPlayer.hasContent(Content.SUN_STONE_PLACE)) {
            redactedPlayer.addContent(Content.SUN_STONE_PLACE);
            Utils.sendActionBar(redactedPlayer.getPlayer(), "You discovered content: " + Content.SUN_STONE_PLACE.getId());
        }
    }

    // Specifically for finishing the Future Race
    public static void onFutureRaceComplete(PlayerMoveEvent event) {
        // This method is only called from the Sun Stone event, therefore no need to verify
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        if (!redactedPlayer.hasContent(Content.FUTURE_RACE_COMPLETE)) {
            redactedPlayer.addContent(Content.FUTURE_RACE_COMPLETE);
            Utils.sendActionBar(redactedPlayer.getPlayer(), "You discovered content: " + Content.FUTURE_RACE_COMPLETE.getId());
        }
    }

    public static HashMap<Player, RedactedPlayer> getRedactedPlayerMap() {
        return redactedPlayerHashMap;
    }
}

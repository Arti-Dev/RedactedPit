package com.articreep.redactedpit.content;

import com.articreep.redactedpit.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.IOException;
import java.util.HashMap;

public class ContentListeners implements Listener {
    private Main plugin;
    private static HashMap<Player, RedactedPlayer> redactedPlayerHashMap = new HashMap<>();

    public ContentListeners(Main plugin) throws IOException {
        this.plugin = plugin;
        // Make the plugin create redactedplayer objects for people already on the server
        for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
            newRedactedPlayer(onlineplayer);
        }
    }

    //Create a RedactedPlayer object when players log in
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) throws IOException {
        newRedactedPlayer(event.getPlayer());
    }

    //Remove the RedactedPlayer object when players log out
    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) throws IOException {
        removeRedactedPlayer(event.getPlayer());
    }

    //Save everyone in alignment with the world saves
    @EventHandler
    public void onWorldSave(WorldSaveEvent event) throws IOException {
        for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
            getRedactedPlayer(onlineplayer).saveData();
        }
    }

    // Specifically for content that only requires user to step inside a box
    @EventHandler
    public void onContentDiscover(PlayerMoveEvent event) {
        Location loc = event.getTo();
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        // Is the user discovering the Future Race?
        if (!redactedPlayer.hasContent(Content.FUTURE_RACE_DISCOVERY)) {
            if (Content.FUTURE_RACE_DISCOVERY.getBox().isInBox(loc)) {
                discoverContent(redactedPlayer, Content.FUTURE_RACE_DISCOVERY);
            }
        }
        // Is the user discovering Dax's Dungeon?
        if (!redactedPlayer.hasContent(Content.DAX_DUNGEON)) {
            if (Content.DAX_DUNGEON.getBox().isInBox(loc)) {
                discoverContent(redactedPlayer, Content.DAX_DUNGEON);
            }
        }
        if (!redactedPlayer.hasContent(Content.DAX_DUNGEON_END)) {
            if (Content.DAX_DUNGEON_END.getBox().isInBox(loc)) {
                discoverContent(redactedPlayer, Content.DAX_DUNGEON_END);
            }
        }
        if (!redactedPlayer.hasContent(Content.JUMP_IN_PIT)) {
            if (Content.JUMP_IN_PIT.getBox().isInBox(loc)) {
                discoverContent(redactedPlayer, Content.JUMP_IN_PIT);
            }
        }
        if (!redactedPlayer.hasContent(Content.SHITASS_CHEST)) {
            if (Content.SHITASS_CHEST.getBox().isInBox(loc)) {
                discoverContent(redactedPlayer, Content.SHITASS_CHEST);
            }
        }
        if (!redactedPlayer.hasContent(Content.EGYPT)) {
            if (Content.EGYPT.getBox().isInBox(loc)) {
                discoverContent(redactedPlayer, Content.EGYPT);
            }
        }
        if (!redactedPlayer.hasContent(Content.SUN_PYRAMID)) {
            if (Content.SUN_PYRAMID.getBox().isInBox(loc)) {
                discoverContent(redactedPlayer, Content.SUN_PYRAMID);
            }
        }
        if (!redactedPlayer.hasContent(Content.ANCIENT_TOWN)) {
            if (Content.ANCIENT_TOWN.getBox().isInBox(loc)) {
                discoverContent(redactedPlayer, Content.ANCIENT_TOWN);
            }
        }
    }

    // Specifically for placement of the Sun Stone
    public static void onSunStonePlace(BlockPlaceEvent event) {
        // This method is only called from the Sun Stone event, therefore no need to verify
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        if (!redactedPlayer.hasContent(Content.SUN_STONE_PLACE)) {
            discoverContent(redactedPlayer, Content.SUN_STONE_PLACE);
        }
    }

    // Specifically for finishing the Future Race
    public static void onFutureRaceComplete(PlayerMoveEvent event) {
        // This method is only called from the Sun Stone event, therefore no need to verify
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        if (!redactedPlayer.hasContent(Content.FUTURE_RACE_COMPLETE)) {
            discoverContent(redactedPlayer, Content.FUTURE_RACE_COMPLETE, ChatColor.DARK_PURPLE + "Quest Complete");
        }
    }

    /**
     * Gets a RedactedPlayer instance of the player. If one does not exist this will return null.
     * @param player
     * @return RedactedPlayer
     */
    public static RedactedPlayer getRedactedPlayer(Player player) {
        return redactedPlayerHashMap.get(player);
    }

    public void newRedactedPlayer(Player player) throws IOException {
        RedactedPlayer redactedPlayer = new RedactedPlayer(player, plugin);
        redactedPlayerHashMap.put(player, redactedPlayer);
        redactedPlayer.loadData();
    }

    public void removeRedactedPlayer(Player player) throws IOException {
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(player);
        redactedPlayer.saveData();
        redactedPlayerHashMap.remove(player);
    }

    /**
     * This method is called when a user discovers new content.
     * @param redplayer RedactedPlayer
     * @param content The content being given
     */
    public static void discoverContent(RedactedPlayer redplayer, Content content) {
        Player player = redplayer.getPlayer();
        redplayer.addContent(content);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        Utils.sendTitle(player, ChatColor.BLUE + "Content Discovered", content.getId(), 5, 60, 5);
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + content.getId());
        player.sendMessage(content.getDescription());
    }

    /**
     * This method is called when a user discovers new content, with customizable title.
     * @param redplayer RedactedPlayer
     * @param content The content being given
     * @param title Title to show player
     */
    public static void discoverContent(RedactedPlayer redplayer, Content content, String title) {
        Player player = redplayer.getPlayer();
        redplayer.addContent(content);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        Utils.sendTitle(player, title, content.getId(), 5, 60, 5);
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + content.getId());
        player.sendMessage(content.getDescription());
    }
}

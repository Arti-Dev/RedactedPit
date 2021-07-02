package com.articreep.redactedpit.content;

import com.articreep.redactedpit.*;
import com.articreep.redactedpit.colosseum.AudienceEffect;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

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
    public void onPlayerLogin(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        Utils.sendTitle(player, ChatColor.DARK_AQUA + "Welcome!", ChatColor.DARK_PURPLE + "Check /questbook for info!", 5, 40, 20);
        // dew it again
        Utils.sendTitle(player, ChatColor.DARK_AQUA + "Welcome!", ChatColor.DARK_PURPLE + "Check /questbook for info!", 5, 40, 20);
        player.playSound(event.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
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
        //TODO Condense into a loop
        Location loc = event.getTo();
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        // Is the user discovering the Future Race?
        if (Content.FUTURE_RACE_DISCOVERY.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.FUTURE_RACE_DISCOVERY);
        }
        // Is the user discovering Dax's Dungeon?
        if (Content.DAX_DUNGEON.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.DAX_DUNGEON);
        }
        //TODO deprecated
        //if (Content.DAX_DUNGEON_END.getBox().isInBox(loc)) {
        //    discoverContent(redactedPlayer, Content.DAX_DUNGEON_END);
        //}
        if (Content.JUMP_IN_PIT.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.JUMP_IN_PIT);
        }
        if (Content.SHITASS_CHEST.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.SHITASS_CHEST, ChatColor.RED + "Random Crap Discovered");
        }
        if (Content.EGYPT.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.EGYPT);
        }
        if (Content.SUN_PYRAMID.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.SUN_PYRAMID);
        }
        if (Content.ANCIENT_TOWN.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.ANCIENT_TOWN);
        }
        if (Content.HIDDEN_ARCHAEOLOGIST.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.HIDDEN_ARCHAEOLOGIST);
        }
        if (Content.LAVA_PIT.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.LAVA_PIT, ChatColor.RED + "Random Crap Discovered");
        }
        if (Content.FIRE_TRIAL.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.FIRE_TRIAL, ChatColor.AQUA + "Bad Content Discovered");
        }
        if (Content.BOB_DESERT.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.BOB_DESERT, ChatColor.AQUA + "Bob");
        }
        if (Content.NINJAFREEZE.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.NINJAFREEZE, ChatColor.RED + "Random Crap Discovered");
        }
        if (Content.FUTURE.getBox().isInBoxExclude(loc)) {
            discoverContent(redactedPlayer, Content.FUTURE);
        }
        if (Content.STIM_GUN_RHYLIE.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.STIM_GUN_RHYLIE);
        }
        if (Content.TIME_MACHINE.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.TIME_MACHINE);
        }
        if (Content.STRANDED_TRAVELER.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.STRANDED_TRAVELER);
        }
        if (Content.JURASSIC_CAVE.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.JURASSIC_CAVE);
        }
        if (Content.BOBCATG.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.BOBCATG, ChatColor.RED + "Random Crap Discovered");
        }
        if (Content.SCRAPPED_ALTAR.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.SCRAPPED_ALTAR);
        }
        if (Content.JURASSIC.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.JURASSIC);
        }
        if (Content.COLOSSEUM.getBox().isInBoxExclude(loc)) {
            discoverContent(redactedPlayer, Content.COLOSSEUM);
        }
        if (Content.ARTI_CAKE.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.ARTI_CAKE, ChatColor.RED + "Random Crap Discovered");
        }
        if (Content.UNDERGROUND_SHORTCUTS.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.UNDERGROUND_SHORTCUTS);
        }
        if (Content.SKYBLOCK_ROOM.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.SKYBLOCK_ROOM);
        }
        if (Content.PUNCHING_BAG.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.PUNCHING_BAG);
        }
        if (Content.TAYLOR_HATS.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.TAYLOR_HATS);
        }
        if (Content.AUCTION_HOUSE.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.AUCTION_HOUSE);
        }
        if (Content.RHYLIE_COMMAND_BUNKER.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.RHYLIE_COMMAND_BUNKER, ChatColor.DARK_GRAY + "Secret Discovered");
        }
    }

    // Specifically for placement of the Sun Stone
    public static void onSunStonePlace(BlockPlaceEvent event) {
        // This method is only called from the Sun Stone event, therefore no need to verify
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        if (!redactedPlayer.hasContent(Content.SUN_STONE_PLACE)) {
            discoverContent(redactedPlayer, Content.SUN_STONE_PLACE, ChatColor.DARK_PURPLE + "Quest Complete");
        }
    }

    // Specifically when player trades Ancient Artifact to Trading Master
    public static void onTradingMasterTrade(Player player) {
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(player);
        if (!redactedPlayer.hasContent(Content.TRADE_ANCIENT_ARTIFACT_TO_TRADEMASTER)) {
            discoverContent(redactedPlayer, Content.TRADE_ANCIENT_ARTIFACT_TO_TRADEMASTER, ChatColor.DARK_PURPLE + "Quest Complete");
        }
    }

    // Specifically when player places Divine Glass
    public static void onDivineGlassPlace(BlockPlaceEvent event) {
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        if (!redactedPlayer.hasContent(Content.DIVINE_GLASS)) {
            discoverContent(redactedPlayer, Content.DIVINE_GLASS);
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

    // Specifically for jumppads
    public static void onJumppad(PlayerInteractEvent event) {
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        if (!redactedPlayer.hasContent(Content.JUMPPADS)) {
            discoverContent(redactedPlayer, Content.JUMPPADS);
        }
    }

    // Specifically for activating Void Charms
    public static void onVoidCharm(PlayerTouchVoidEvent event) {
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        if (!redactedPlayer.hasContent(Content.VOID_CHARM)) {
            discoverContent(redactedPlayer, Content.VOID_CHARM);
        }
    }

    // POG TOWER!!
    @EventHandler
    public void onPogTowerClick(PlayerInteractEvent event) {
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        if (!redactedPlayer.hasContent(Content.POG_TOWER)) {
            Location loc = event.getPlayer().getTargetBlock((Set<Material>) null, 200).getLocation();
            if (Content.POG_TOWER.getBox().isInBox(loc)) {
                discoverContent(redactedPlayer, Content.POG_TOWER, ChatColor.RED + "Random Crap Discovered");
            }
        }
    }

    // Specifically when the user gets hit by an audience effect
    public static void onAudienceEffect(Player player, AudienceEffect effect) {
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(player);
        // First, check if they even have the content for discovering the audience
        discoverContent(redactedPlayer, Content.AUDIENCE);
        // Next, check what effect it was
        if (effect == AudienceEffect.HOT_POTATO) {
            discoverContent(redactedPlayer, Content.HOT_POTATO);
        } else if (effect == AudienceEffect.LARGEKB) {
            discoverContent(redactedPlayer, Content.LARGEKB);
        }
    }

    // Specifically for Pearl
    public static void onPearlQuest(Player player) {
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(player);
        if (!redactedPlayer.hasContent(Content.WARPING_TO_THE_PAST)) {
            discoverContent(redactedPlayer, Content.WARPING_TO_THE_PAST, ChatColor.DARK_PURPLE + "Quest Complete");
        }
    }

    // Specifically for treasure
    public static void onTreasureDiscover(Player player) {
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(player);
        if (!redactedPlayer.hasContent(Content.TREASURE_CHEST)) {
            discoverContent(redactedPlayer, Content.TREASURE_CHEST);
        }
    }

    // Min*r
    public static void onMinerTalk(Player player) {
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(player);
        if (!redactedPlayer.hasContent(Content.TALK_TO_MINER)) {
            discoverContent(redactedPlayer, Content.TALK_TO_MINER, ChatColor.DARK_PURPLE + "Quest Started");
        }
    }

    public static void onSpikeaxeObtain(Player player) {
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(player);
        if (!redactedPlayer.hasContent(Content.SPIKEAXE_OBTAIN)) {
            discoverContent(redactedPlayer, Content.SPIKEAXE_OBTAIN, ChatColor.DARK_PURPLE + "Quest Complete");
        }
    }

    /**
     * Gets a RedactedPlayer instance of the player. If one does not exist this will return null.
     * @param player Bukkit Player
     * @return RedactedPlayer
     */
    public static RedactedPlayer getRedactedPlayer(Player player) {
        return redactedPlayerHashMap.get(player);
    }

    public static boolean isRedactedPlayer(Player player) {
        return redactedPlayerHashMap.containsKey(player);
    }

    public void newRedactedPlayer(Player player) throws IOException {
        RedactedPlayer redactedPlayer = new RedactedPlayer(player, plugin);
        redactedPlayerHashMap.put(player, redactedPlayer);
        redactedPlayer.loadData();
    }

    public static void removeRedactedPlayer(Player player) throws IOException {
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
        if (redplayer.hasContent(content)) return;
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
        if (redplayer.hasContent(content)) return;
        Player player = redplayer.getPlayer();
        redplayer.addContent(content);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        Utils.sendTitle(player, title, content.getId(), 5, 60, 5);
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + content.getId());
        player.sendMessage(content.getDescription());
    }
}

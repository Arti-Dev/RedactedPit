package com.articreep.redactedpit.content;

import com.articreep.redactedpit.*;
import com.articreep.redactedpit.colosseum.AudienceEffect;
import com.articreep.redactedpit.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ContentListeners implements Listener {
    private final Main plugin;
    private static final HashMap<Player, RedactedPlayer> redactedPlayerHashMap = new HashMap<>();
    // To prevent players from discovering multiple content at the same time, we have a set that ensures they're displayed separately
    private static final HashSet<RedactedPlayer> contentCooldown = new HashSet<>();

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
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 100000000, 1));
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
        // Get the location
        Location loc = event.getTo();
        // Grab a redactedPlayer
        RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
        // Verify that it worked
        if (redactedPlayer == null) return;
        // Loop through all content
        for (Content content : Content.values()) {
            // Some contents don't have hitboxes, so skip them
            if (content.getBox() == null) continue;
            if (content.getBox().isInBox(loc)) {
                if (content == Content.SHITASS_CHEST || content == Content.LAVA_PIT || content == Content.BOBCATG || content == Content.ARTI_CAKE ||
                content == Content.MYSTIC_CAR || content == Content.NINJAFREEZE || content == Content.ICE_BAR) {
                    discoverContent(redactedPlayer, content, ChatColor.RED + "Random Crap Discovered");
                } else if (content == Content.BOB_DESERT) {
                    discoverContent(redactedPlayer, content, ChatColor.AQUA + "Bob");
                } else if (content == Content.FIRE_TRIAL || content == Content.PUNCHING_BAG) {
                    discoverContent(redactedPlayer, content, ChatColor.AQUA + "Bad Content Discovered");
                } else if (content == Content.RHYLIE_COMMAND_BUNKER) {
                    discoverContent(redactedPlayer, content, ChatColor.DARK_GRAY + "Secret Discovered");
                } else {
                    discoverContent(redactedPlayer, content);
                }
            }
        }
        // Is the user discovering Dax's Dungeon?
        //if (Content.DAX_DUNGEON.getBox().isInBox(loc)) {
        //   discoverContent(redactedPlayer, Content.DAX_DUNGEON);
        //}
        //if (Content.DAX_DUNGEON_END.getBox().isInBox(loc)) {
        //    discoverContent(redactedPlayer, Content.DAX_DUNGEON_END);
        //}
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
        // The player must be in future
        if (Content.FUTURE.getBox().isInBox(event.getPlayer().getLocation())) {
            RedactedPlayer redactedPlayer = redactedPlayerHashMap.get(event.getPlayer());
            if (!redactedPlayer.hasContent(Content.JUMPPADS)) {
                discoverContent(redactedPlayer, Content.JUMPPADS);
            }
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
        Location loc = event.getPlayer().getTargetBlock((Set<Material>) null, 200).getLocation();
        if (Content.POG_TOWER.getBox().isInBox(loc)) {
            discoverContent(redactedPlayer, Content.POG_TOWER, ChatColor.RED + "Random Crap Discovered");
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
        discoverContent(redactedPlayerHashMap.get(player), Content.TALK_TO_MINER, ChatColor.DARK_PURPLE + "Quest Started");
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
        if (redplayer == null) return;
        if (redplayer.hasContent(content)) return;
        if (contentCooldown.contains(redplayer)) return;
        // All checks have passed, so put them on cooldown now
        contentCooldown.add(redplayer);
        Player player = redplayer.getPlayer();
        redplayer.addContent(content);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        Utils.sendTitle(player, ChatColor.BLUE + "Content Discovered", content.getId(), 5, 60, 5);
        // Add some gold why not
        redplayer.addGold(250);
        player.sendMessage(ChatColor.GOLD + "+250g");
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + content.getId());
        player.sendMessage(content.getDescription());
        // Bukkitrunnable to eventually remove them from cooldown
        new BukkitRunnable() {

            @Override
            public void run() {
                contentCooldown.remove(redplayer);
            }
        }.runTaskLater(Main.getInstance(), 80);
    }

    /**
     * This method is called when a user discovers new content, with customizable title.
     * @param redplayer RedactedPlayer
     * @param content The content being given
     * @param title Title to show player
     */
    public static void discoverContent(RedactedPlayer redplayer, Content content, String title) {
        if (redplayer == null) return;
        if (redplayer.hasContent(content)) return;
        if (contentCooldown.contains(redplayer)) return;
        // All checks have passed, so put them on cooldown now
        contentCooldown.add(redplayer);
        Player player = redplayer.getPlayer();
        redplayer.addContent(content);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        Utils.sendTitle(player, title, content.getId(), 5, 60, 5);
        // Add some gold why not
        redplayer.addGold(250);
        player.sendMessage(ChatColor.GOLD + "+250g");
        player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + content.getId());
        player.sendMessage(content.getDescription());
        // Bukkitrunnable to eventually remove them from cooldown
        new BukkitRunnable() {

            @Override
            public void run() {
                contentCooldown.remove(redplayer);
            }
        }.runTaskLater(Main.getInstance(), 80);
    }
}

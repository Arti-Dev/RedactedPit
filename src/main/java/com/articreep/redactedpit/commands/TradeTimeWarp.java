package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.Utils;
import com.articreep.redactedpit.content.Content;
import com.articreep.redactedpit.content.ContentListeners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class TradeTimeWarp implements CommandExecutor {
    Main plugin;
    public TradeTimeWarp(Main plugin) {
        this.plugin = plugin;
    }
    public static HashMap<UUID, Integer> interact = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Inventory inventory = player.getInventory();
            ItemStack item = new ItemStack(player.getItemInHand());
            ItemMeta itemmeta = item.getItemMeta();
            if (interact.containsKey(player.getUniqueId())) {
                if (interact.get(player.getUniqueId()) == 2) {
                    return true;
                }
            }
            // Check if they have the quest completed
            if (ContentListeners.getRedactedPlayer(player).hasContent(Content.WARPING_TO_THE_PAST)) {
                player.openInventory(createGUI());
            } else {
                if (interact.containsKey(player.getUniqueId())) {
                    if (item == null || item.getType() != Material.GHAST_TEAR || !itemmeta.hasDisplayName() || !itemmeta.getDisplayName().equals(ChatColor.WHITE + "T-Rex Tooth")) {
                        player.sendMessage(ChatColor.YELLOW + "[NPC] Pearl" + ChatColor.WHITE + ": Could you find me something from the " + ChatColor.AQUA +
                                "past that's white, like a tooth?");
                        player.sendMessage(ChatColor.DARK_GRAY + "(Hold it in your hand)");
                        player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
                    } else if (item.isSimilar(RedactedGive.TRexTooth(1))) {
                        interact.put(player.getUniqueId(), 2);
                        inventory.removeItem(RedactedGive.TRexTooth(1));
                        player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
                        player.sendMessage(ChatColor.YELLOW + "[NPC] Pearl" + ChatColor.WHITE + ": Oh? What's this?");
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
                                player.sendMessage(ChatColor.YELLOW + "[NPC] Pearl" + ChatColor.WHITE + ": A T-Rex Tooth? Oh, this'll do just fine!");
                            }
                        }.runTaskLater(plugin, 60);
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
                                player.sendMessage(ChatColor.YELLOW + "[NPC] Pearl" + ChatColor.WHITE + ": Thanks for your help! Now I can continue to make more pearls!");
                                player.sendMessage(ChatColor.YELLOW + "[NPC] Pearl" + ChatColor.WHITE + ": Feel free to buy some from me right now!");
                                interact.remove(player.getUniqueId());
                                ContentListeners.onPearlQuest(player);
                            }
                        }.runTaskLater(plugin, 120);
                    }
                } else {
                    player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
                    player.sendMessage(ChatColor.YELLOW + "[NPC] Pearl" + ChatColor.WHITE + ": Hey, over here!");
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
                            player.sendMessage(ChatColor.YELLOW + "[NPC] Pearl" + ChatColor.WHITE + ": I'm trying to make some super cool tech! They're called "
                            + ChatColor.AQUA + "Time Warp Pearls" + ChatColor.WHITE + ".");
                        }
                    }.runTaskLater(plugin, 60);
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
                            player.sendMessage(ChatColor.YELLOW + "[NPC] Pearl" + ChatColor.WHITE + ": Thing is, I need an item from a different time in order to make " +
                                    "them work..");
                        }
                    }.runTaskLater(plugin, 120);
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
                            player.sendMessage(ChatColor.YELLOW + "[NPC] Pearl" + ChatColor.WHITE + ": They also have to be white, or else it'll ruin the color..");
                        }
                    }.runTaskLater(plugin, 180);
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
                            player.sendMessage(ChatColor.YELLOW + "[NPC] Pearl" + ChatColor.WHITE + ": Could you find me something from the " + ChatColor.AQUA +
                                    "past that's white?");
                        }
                    }.runTaskLater(plugin, 240);
                    interact.put(player.getUniqueId(), 1);
                }
            }
            return true;
        }
        return false;
    }

    private static Inventory createGUI() {
        Inventory inv;
        ItemStack gold = Utils.createGuiItem(Material.GOLD_INGOT, ChatColor.GOLD + "Trade with Gold",
                ChatColor.DARK_GRAY + "Costs 1000g per 2");
        ItemStack artifact = Utils.createGuiItem(Material.DOUBLE_PLANT, ChatColor.YELLOW + "Trade with Ancient Artifacts",
                ChatColor.DARK_GRAY + "Costs 1 artifact per 16");
        inv = Bukkit.createInventory(null, 27, "Pearl");
        inv.setItem(4, RedactedGive.TimeWarpPearl(1));
        inv.setItem(12, gold);
        inv.setItem(14, artifact);
        return inv;
    }
}

package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.utils.Utils;
import com.articreep.redactedpit.content.Content;
import com.articreep.redactedpit.content.ContentListeners;
import com.articreep.redactedpit.content.RedactedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class SpikeaxeMiner implements CommandExecutor {
    Main plugin;
    public SpikeaxeMiner(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            RedactedPlayer redactedPlayer = ContentListeners.getRedactedPlayer(player);
            if (!redactedPlayer.hasContent(Content.TALK_TO_MINER)) {
                new BukkitRunnable() {
                    int i = 0;

                    @Override
                    public void run() {
                        if (i == 0) {
                            player.sendMessage(ChatColor.YELLOW + "[NPC] Miner: " + ChatColor.WHITE + "Hello! I'm just an average, everyday miner.");
                        } else if (i == 1) {
                            player.sendMessage(ChatColor.YELLOW + "[NPC] Miner: " + ChatColor.WHITE + "Believe it or not, I do mine, just when no one's looking.");
                        } else if (i == 2) {
                            player.sendMessage(ChatColor.YELLOW + "[NPC] Miner: " + ChatColor.WHITE + "You look like you want to try mining yourself!");
                        } else if (i == 3) {
                            player.sendMessage(ChatColor.YELLOW + "[NPC] Miner: " + ChatColor.WHITE + "Here, take this pickaxe!");
                        } else if (i == 4) {
                            player.getInventory().addItem(addWoodPickaxe());
                            player.sendMessage(ChatColor.YELLOW + "[NPC] Miner: " + ChatColor.WHITE + "Since you'll only see me standing here, " +
                                    "I'm always happy to upgrade your pickaxe if you bring the materials for it! Who knows, maybe I can grant you an ability..");
                        } else if (i == 5) {
                            ContentListeners.onMinerTalk(player);
                            this.cancel();
                        } else {
                            this.cancel();
                        }
                        player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 1);
                        i++;
                    }
                }.runTaskTimer(plugin, 0, 40);
            } else {
                // Open inventory
                player.openInventory(createGUI());
            }
            return true;
        }
        return false;
    }

    private Inventory createGUI() {
        Inventory inv;
        inv = Bukkit.createInventory(null, 36, "Miner");
        inv.setItem(9, Utils.createGuiItem(Material.WOOD_PICKAXE, ChatColor.YELLOW + "Get another Wooden Pickaxe",
                ChatColor.GRAY + "Lost yours already? I can give you another one for 500g.."));
        inv.setItem(11, Utils.createGuiItem(Material.STONE_PICKAXE, ChatColor.YELLOW + "Craft a Stone Pickaxe",
                ChatColor.GRAY + "Requires 3 Cobblestone and a Wooden Pickaxe"));
        inv.setItem(13, Utils.createGuiItem(Material.IRON_PICKAXE, ChatColor.YELLOW + "Craft an Iron Pickaxe",
                ChatColor.GRAY + "Requires 3 Iron Ore and a Stone Pickaxe"));
        inv.setItem(15, Utils.createGuiItem(Material.GOLD_PICKAXE, ChatColor.YELLOW + "Craft an Gold Pickaxe",
                ChatColor.GRAY + "Requires 3 Gold Ore and an Iron Pickaxe"));
        inv.setItem(17, Utils.createGuiItem(Material.DIAMOND_PICKAXE, ChatColor.YELLOW + "Craft a Spikeaxe",
                ChatColor.GRAY + "Requires 3 Diamonds and a Gold Pickaxe"));
        inv.setItem(18, Utils.createGuiItem(Material.PAPER, ChatColor.YELLOW + "Tips:",
                ChatColor.GRAY + "Don't lose your pickaxe.."));
        inv.setItem(20, Utils.createGuiItem(Material.PAPER, ChatColor.YELLOW + "Tips:",
                ChatColor.GRAY + "There's a mineable boulder", ChatColor.GRAY + "near the tree at the entrance to the cave.",
                ChatColor.GRAY + "Actually, most of the cobblestone and stone blocks ", ChatColor.GRAY + "in the jungle are mineable."));

        inv.setItem(22, Utils.createGuiItem(Material.PAPER, ChatColor.YELLOW + "Tips:",
                ChatColor.GRAY + "There's a hidden cave in the ", ChatColor.GRAY + "far-southwest of the Jurassic area."));
        inv.setItem(24, Utils.createGuiItem(Material.PAPER, ChatColor.YELLOW + "Tips:",
                ChatColor.GRAY + "There's another hidden cave under ", ChatColor.GRAY + "the bridge in front of the Sun Pyramid."));
        inv.setItem(26, Utils.createGuiItem(Material.PAPER, ChatColor.YELLOW + "Tips:",
                ChatColor.GRAY + "There's a small cave blocked off by mineable sandstone ", ChatColor.GRAY + "east of the Sun Stone merchant."));
        inv.setItem(31, RedactedGive.Spikeaxe(1));
        return inv;
    }

    private static ItemStack addWoodPickaxe() {
        ItemStack item = new ItemStack(Material.WOOD_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(true);
        item.setItemMeta(meta);
        return item;
    }
}

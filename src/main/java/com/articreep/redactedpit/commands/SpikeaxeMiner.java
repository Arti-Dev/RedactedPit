package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
            // Dialogue
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
                        player.getInventory().addItem(new ItemStack(Material.WOOD_PICKAXE));
                        player.sendMessage(ChatColor.YELLOW + "[NPC] Miner: " + ChatColor.WHITE + "Since you'll only see me standing here, " +
                                "I'm always happy to upgrade your pickaxe if you bring the materials for it!");
                    } else {
                        this.cancel();
                    }
                    player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 1);
                    i++;
                }
            }.runTaskTimer(plugin, 0, 40);
            return true;
        }
        return false;
    }
}

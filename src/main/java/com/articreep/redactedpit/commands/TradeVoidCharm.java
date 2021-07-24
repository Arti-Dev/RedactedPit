package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.utils.Utils;
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
import org.bukkit.scheduler.BukkitRunnable;

public class TradeVoidCharm implements CommandExecutor {
    Main plugin;
    public TradeVoidCharm(Main plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(ChatColor.YELLOW + "[NPC] ???" + ChatColor.WHITE + ": Do you hate falling into the void?");
            player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 1);
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.openInventory(createGUI());
                }
            }.runTaskLater(plugin, 20);
            return true;
        }
        return false;
    }

    private static Inventory createGUI() {
        Inventory inv;
        ItemStack gold = Utils.createGuiItem(Material.GOLD_INGOT, ChatColor.GOLD + "Trade with Gold",
                ChatColor.DARK_GRAY + "Costs 500g per 1");
        ItemStack artifact = Utils.createGuiItem(Material.DOUBLE_PLANT, ChatColor.YELLOW + "Trade with Ancient Artifacts",
                ChatColor.DARK_GRAY + "Costs 1 artifact per 4");
        inv = Bukkit.createInventory(null, 27, "Void Charm");
        inv.setItem(4, RedactedGive.VoidCharm(1));
        inv.setItem(12, gold);
        inv.setItem(14, artifact);
        return inv;
    }
}

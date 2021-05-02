package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Main;
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

import net.md_5.bungee.api.ChatColor;

public class TradeDivineGlass implements CommandExecutor {
	Main plugin;
	public TradeDivineGlass(Main plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ItemStack item = new ItemStack(player.getItemInHand());
			ItemMeta itemmeta = item.getItemMeta();
			Inventory inventory = player.getInventory();
			if (item == null || item.getType() != Material.DOUBLE_PLANT || itemmeta.hasDisplayName() == false ) { //if it's just some dumb flower
	 			player.sendMessage(ChatColor.YELLOW + "[NPC] Rem: " + ChatColor.WHITE + "Hey! You seem like you're having a fine day!");
	 			player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.sendMessage(ChatColor.YELLOW + "[NPC] Rem: " + ChatColor.WHITE + "How about you take some " + ChatColor.AQUA + "Divine Glass " + ChatColor.WHITE + "with you?");
						player.playSound(player.getLocation(), Sound.VILLAGER_HAGGLE, 1, 2);
					}
				}.runTaskLater(plugin, 20);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.sendMessage(ChatColor.YELLOW + "[NPC] Rem: " + ChatColor.WHITE + "I can craft you some if you bring me a " + ChatColor.YELLOW + "ancient artifact.");
						player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
					}
				}.runTaskLater(plugin, 40);
				return true;
			} else if (itemmeta.getDisplayName().equals(ChatColor.YELLOW + "Ancient Artifact")) {
				inventory.removeItem(RedactedGive.AncientArtifact(1));
				inventory.addItem(RedactedGive.DivineGlass(64));
				player.sendMessage(ChatColor.YELLOW + "[NPC] Rem: " + ChatColor.WHITE + "Here you go!");
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
				player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "SHINY! " + ChatColor.GRAY + "You obtained " + ChatColor.AQUA + "Divine Glass" + ChatColor.GRAY + " x64!");
				return true;
			}
			return true;
		}
		return false;
	}

}

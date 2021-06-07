package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Main;
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


public class TradeSunStone implements CommandExecutor {
	Main plugin;
	public TradeSunStone(Main plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			ItemStack item = new ItemStack(player.getItemInHand());
			ItemMeta itemmeta = item.getItemMeta();
			Inventory inventory = player.getInventory();
			if (item == null || item.getType() != Material.DOUBLE_PLANT || !itemmeta.hasDisplayName()) { //if it's just some dumb flower
	 			player.sendMessage(ChatColor.YELLOW + "[NPC] heartyou: " + ChatColor.WHITE + "Hello!");
	 			player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.sendMessage(ChatColor.YELLOW + "[NPC] heartyou: " + ChatColor.WHITE + "The " + ChatColor.GOLD + "sun" + ChatColor.WHITE + " shines within you..");
						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 2);
					}
				}.runTaskLater(plugin, 20);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.sendMessage(ChatColor.YELLOW + "[NPC] heartyou: " + ChatColor.WHITE + "Bring me an " + ChatColor.YELLOW + "ancient artifact, " + ChatColor.WHITE + "and I can grant you a sun stone!");
						player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 2);
					}
				}.runTaskLater(plugin, 40);
				return true;
			} else if (itemmeta.getDisplayName().equals(ChatColor.YELLOW + "Ancient Artifact")) {
				inventory.removeItem(RedactedGive.AncientArtifact(1));
				inventory.addItem(RedactedGive.SunStone(1));
				player.sendMessage(ChatColor.YELLOW + "[NPC] Rem: " + ChatColor.WHITE + "Here you go!");
				player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
				player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "SHINY! " + ChatColor.GRAY + "You obtained " + ChatColor.GOLD + "Sun Stone" + ChatColor.GRAY + " x1!");
				return true;
			}
			return true;
		}
		return false;
	}

}

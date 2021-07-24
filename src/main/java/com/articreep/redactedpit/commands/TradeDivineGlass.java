package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TradeDivineGlass implements CommandExecutor {
	Main plugin;
	public TradeDivineGlass(Main plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.openInventory(createGUI());
			return true;
		}
		return false;
	}

	private static Inventory createGUI() {
		Inventory inv;
		ItemStack gold = Utils.createGuiItem(Material.GOLD_INGOT, org.bukkit.ChatColor.GOLD + "Trade with Gold",
				org.bukkit.ChatColor.DARK_GRAY + "Costs 1000g per 32");
		ItemStack artifact = Utils.createGuiItem(Material.DOUBLE_PLANT, org.bukkit.ChatColor.YELLOW + "Trade with Ancient Artifacts",
				org.bukkit.ChatColor.DARK_GRAY + "Costs 1 artifact per 64");
		inv = Bukkit.createInventory(null, 27, "Divine Glass");
		inv.setItem(4, RedactedGive.DivineGlass(1));
		inv.setItem(12, gold);
		inv.setItem(14, artifact);
		return inv;
	}

}

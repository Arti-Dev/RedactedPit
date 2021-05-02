package com.articreep.redactedpit.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import com.articreep.redactedpit.Main;
import org.bukkit.Bukkit;
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

public class TradingGUIs implements CommandExecutor {
	Main plugin;
	public TradingGUIs(Main plugin) {
		this.plugin = plugin;
	}
	public static HashMap<UUID, Integer> masterinteract = new HashMap<UUID, Integer>();
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			UUID uuid = player.getUniqueId();
			if (masterinteract.containsKey(uuid) == false) {
				masterinteract.put(uuid, 1);
				player.sendMessage(ChatColor.YELLOW + "[NPC] Trading Master: " + ChatColor.WHITE + 
						"Hey there, I specialize in the art of " + ChatColor.YELLOW + "trade." + ChatColor.WHITE + " *cough*");
				player.playSound(player.getLocation(), Sound.VILLAGER_IDLE, 1, 1);
				new BukkitRunnable() {
					@Override
					public void run() {
						player.sendMessage(ChatColor.YELLOW + "[NPC] Trading Master: " + ChatColor.WHITE + 
								"Wanna check out what I've got?" + ChatColor.GRAY + "(click)");
						player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1, 1);
					}
				}.runTaskLater(plugin, 40);
				return true;
			} else {
				player.openInventory(createInventoryTradingGUI());
				return true;
			}
		}
		return false;
	}

	// Nice little method to create a gui item with a custom name, and description
    public static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));
        

        item.setItemMeta(meta);

        return item;
    }
    
    
    // Method to create the first inventory
    public static Inventory createInventoryTradingGUI() {
    	Inventory inv;
    	ItemStack arrow = new ItemStack(createGuiItem(Material.ARROW, " "));
    	ItemStack gold = createGuiItem(Material.GOLD_INGOT, ChatColor.GOLD + "Trade..?", ChatColor.WHITE + "Trade 1 " + ChatColor.YELLOW + "Ancient Artifact ", 
    			ChatColor.WHITE + "for " + ChatColor.GOLD + "" + ChatColor.BOLD + "5000g");
		inv = Bukkit.createInventory(null, 27, "Trading Mastah");
		inv.setItem(12, RedactedGive.AncientArtifact(1));
		inv.setItem(13, arrow);
		inv.setItem(14, gold);
        return inv;
    }
}

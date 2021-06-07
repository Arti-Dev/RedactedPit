package com.articreep.redactedpit.listeners;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.commands.RedactedGive;
import com.articreep.redactedpit.content.ContentListeners;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class TradingListeners implements Listener {
	Main plugin;
	public TradingListeners(Main plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
    	Player p = (Player) e.getWhoClicked();
    	Inventory inventory = p.getInventory();
        final ItemStack clickedItem = e.getCurrentItem();
    	if (e.getView().getTitle().equals("Trading Mastah")) {
	        e.setCancelled(true);
	        // verify current item is not null
	        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
	        if (e.getSlot() == 12) {
	        	p.closeInventory();
	        	p.sendMessage(ChatColor.YELLOW + "[NPC] Trading Master: " + ChatColor.WHITE + "Haha, I don't actually sell artifacts, I only buy them!");
	        	p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1, 1);
	        	new BukkitRunnable() {
					@Override
					public void run() {
						p.sendMessage(ChatColor.YELLOW + "[NPC] Trading Master: " + ChatColor.WHITE + "What's that? You're supposed to be a master of trading? Who cares if I only buy them???");
						p.playSound(p.getLocation(), Sound.VILLAGER_HAGGLE, 1, 1);
					}
	        	}.runTaskLater(plugin, 40);
	        	return;
	        }
	        if (e.getSlot() == 14) {
	        	if (inventory.containsAtLeast(RedactedGive.AncientArtifact(1), 1)) {
	        		p.closeInventory();
					inventory.removeItem(RedactedGive.AncientArtifact(1));
					inventory.addItem(new ItemStack(Material.GOLD_INGOT, 5000));
					p.sendMessage(ChatColor.YELLOW + "[NPC] Trading Master: " + ChatColor.WHITE + "Here you go! Five thousand gold ingots.");
					ContentListeners.onTradingMasterTrade(p);
					p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "SHINY..? " + ChatColor.GRAY + "You obtained " + ChatColor.GOLD + "Gold Ingot" + ChatColor.GRAY + " x5000!");
					ContentListeners.getRedactedPlayer(p).addGold(5000);
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
				} else {
	        		p.closeInventory();
	        		p.sendMessage(ChatColor.RED + "You don't have any Ancient Artifacts in your inventory!");
	        	}
	        }
    	}
    	if (e.getView().getTitle().equals("Pearl")) {
    		e.setCancelled(true);
			if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
			if (e.getSlot() == 12) {
				if (!ContentListeners.getRedactedPlayer(p).subtractGold(1000)) {
					p.sendMessage(ChatColor.RED + "You don't have enough gold!");
					p.closeInventory();
				} else {
					inventory.addItem(RedactedGive.TimeWarpPearl(2));
					p.sendMessage(ChatColor.YELLOW + "[NPC] Pearl" + ChatColor.WHITE + ": Enjoy!");
					p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "COOL! " + ChatColor.GRAY + "You obtained " + ChatColor.AQUA + "Time Warp Pearl" + ChatColor.GRAY + " x2!");
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
				}
				return;
			}
			if (e.getSlot() == 14) {
				if (inventory.containsAtLeast(RedactedGive.AncientArtifact(1), 1)) {
					inventory.removeItem(RedactedGive.AncientArtifact(1));
					inventory.addItem(RedactedGive.TimeWarpPearl(16));
					p.sendMessage(ChatColor.YELLOW + "[NPC] Pearl" + ChatColor.WHITE + ": Enjoy!");
					p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "COOL! " + ChatColor.GRAY + "You obtained " + ChatColor.AQUA + "Time Warp Pearl" + ChatColor.GRAY + " x16!");
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
				} else {
					p.closeInventory();
					p.sendMessage(ChatColor.RED + "You don't have any Ancient Artifacts in your inventory!");
				}
			}
		}
    	if (e.getView().getTitle().equals("Divine Glass")) {
    		e.setCancelled(true);
			if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
			if (e.getSlot() == 12) {
				if (!ContentListeners.getRedactedPlayer(p).subtractGold(1000)) {
					p.sendMessage(ChatColor.RED + "You don't have enough gold!");
					p.closeInventory();
				} else {
					inventory.addItem(RedactedGive.DivineGlass(32));
					p.sendMessage(ChatColor.YELLOW + "[NPC] Rem" + ChatColor.WHITE + ": Enjoy!");
					p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "SHINY! " + ChatColor.GRAY + "You obtained " + ChatColor.AQUA + "Divine Glass" + ChatColor.GRAY + " x32!");
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
				}
				return;
			}
			if (e.getSlot() == 14) {
				if (inventory.containsAtLeast(RedactedGive.AncientArtifact(1), 1)) {
					inventory.removeItem(RedactedGive.AncientArtifact(1));
					inventory.addItem(RedactedGive.DivineGlass(64));
					p.sendMessage(ChatColor.YELLOW + "[NPC] Rem" + ChatColor.WHITE + ": Enjoy!");
					p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "SHINY! " + ChatColor.GRAY + "You obtained " + ChatColor.AQUA + "Divine Glass" + ChatColor.GRAY + " x64!");
					p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, 1);
				} else {
					p.closeInventory();
					p.sendMessage(ChatColor.RED + "You don't have any Ancient Artifacts in your inventory!");
				}
			}
		}
	}
	//TODO sus
	// Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().getHolder() == null) {
          e.setCancelled(true);
        }
    }
}

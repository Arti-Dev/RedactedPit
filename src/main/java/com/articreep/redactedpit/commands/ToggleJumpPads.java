package com.articreep.redactedpit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class ToggleJumpPads implements CommandExecutor {
	public static boolean toggled = true;
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (toggled) {
			toggled = false;
			sender.sendMessage("Jumppads Toggle: " + ChatColor.RED + false);
		}
		else {
			toggled = true;
			sender.sendMessage("Jumppads Toggle: " + ChatColor.GREEN + true);
		}
		
		return true;
		
	}

}

package com.articreep.redactedpit.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToggleLaunchers implements CommandExecutor {
	public static boolean toggled = true;
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (toggled) {
			toggled = false;
			sender.sendMessage("Launchers Toggle: " + ChatColor.RED + false);
		}
		else {
			toggled = true;
			sender.sendMessage("Launchers Toggle: " + ChatColor.GREEN + true);
		}
		
		return true;
		
	}

}

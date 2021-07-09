package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class Debug implements CommandExecutor {
	Main plugin;
	public static boolean debug;
	public Debug(Main plugin) {
		debug = plugin.getConfig().getBoolean("debug");
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender.hasPermission("redactedpit.debug")) {
			if (!debug) {
				debug = true;
				sender.sendMessage(ChatColor.YELLOW + "Debug messages are now being dumped to console!");
				plugin.getConfig().set("debug", true);
			} else {
				debug = false;
				sender.sendMessage(ChatColor.YELLOW + "Debug messages are now off!");
				plugin.getConfig().set("debug", false);
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Even if you had access to this, it wouldn't really matter..");
		}
		return true;
	}

}

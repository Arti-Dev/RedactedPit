package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.colosseum.ColosseumPlayer;
import com.articreep.redactedpit.colosseum.ColosseumRunnable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ColoInfo implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			// If no arguments are given defaults to sender
			Player lookup = (Player) sender;
			if (args.length >= 2) {
				player.sendMessage(ChatColor.RED + "Please specify only ONE player!");
			}
			if (args.length == 1) {
				lookup = Bukkit.getPlayer(args[0]);
				if (lookup == null) {
					player.sendMessage(ChatColor.RED + "Please specify a valid, online player!");
					return true;
				}
			}
			ColosseumPlayer coloplayer = ColosseumRunnable.getColosseumPlayer(lookup, true);
			coloplayer.sendAllValues(player);
			return true;
		}
		
		return false;
	}

}

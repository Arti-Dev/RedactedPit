package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.RaceListeners;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CancelRace implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (RaceListeners.RaceData.containsKey(player)) {
				RaceListeners.resetData(player);
				player.sendMessage(ChatColor.GREEN + "Race cancelled!");
				Bukkit.getLogger().info(player.getName() + " canceled their Future Race!");
			} else {
				player.sendMessage(ChatColor.RED + "You are currently not doing the race!");
			}
			return true;
		}
		Bukkit.getLogger().severe("Only players can run this command!");
		return true;
	}
}

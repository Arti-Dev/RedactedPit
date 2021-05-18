package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.content.ContentListeners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class ResetContent implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                try {
                    ContentListeners.getRedactedPlayer((Player) sender).resetData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sender.sendMessage(ChatColor.RED + "Your \"Content\" data has been reset!");
                return true;
            } else if (args.length == 1) {
                if (sender.hasPermission("redactedpit.resetcontent")) {
                    Player reset;
                    try {
                        reset = Bukkit.getPlayer(args[0]);
                        ContentListeners.getRedactedPlayer(reset).resetData();
                        sender.sendMessage(ChatColor.RED + "Cleared \"Content\" stats for " + reset.getName());
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "That is not an online player!");
                    }
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to reset another player's content!");
                    return true;
                }

            } else {
                sender.sendMessage(ChatColor.RED + "Too many arguments!");
                return true;
            }
        }
        return false;
    }
}

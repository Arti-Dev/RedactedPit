package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.content.ContentListeners;
import com.articreep.redactedpit.content.RedactedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoldCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Won't use an enum because it's literally two operations
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /gold [add/set] [player] [amount] - negative numbers allowed!");
        } else if (args.length == 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /gold [add/set] [player] [amount] - negative numbers allowed!");
        } else if (args.length == 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /gold [add/set] [player] [amount] - negative numbers allowed!");
        } else if (args.length == 3) {
            // Valid player?
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Invalid player! Must be online.");
                return true;
            }
            RedactedPlayer redactedPlayer = ContentListeners.getRedactedPlayer(player);
            // Make sure the player supplied a number
            double amount;
            try {
                amount = Double.parseDouble(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "That's not a number!");
                return true;
            }
            // Valid operation?
            if (args[0].equalsIgnoreCase("add")) {
                if (redactedPlayer.addGold(amount)) {
                    sender.sendMessage(ChatColor.GREEN + "Sucessfully added " + amount + " gold to " + player.getName());
                } else {
                    sender.sendMessage(ChatColor.RED + "That would end up with a negative amount of gold!");
                }
            } else if (args[0].equalsIgnoreCase("set")) {
                if (amount < 0) {
                    sender.sendMessage(ChatColor.RED + "You may not set gold to a negative number!");
                    return true;
                }
                redactedPlayer.setGold(amount);
                sender.sendMessage(ChatColor.GREEN + "Sucessfully set " + player.getName() + "'s gold to " + amount);
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /gold [add/set] [player] [amount] - negative numbers allowed!");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final ArrayList<String> strings = new ArrayList<>();
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            strings.add("add");
            strings.add("set");
            StringUtil.copyPartialMatches(args[0], strings, completions);
        } else if (args.length == 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                strings.add(player.getName());
            }
            StringUtil.copyPartialMatches(args[1], strings, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}

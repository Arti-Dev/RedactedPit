package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.content.ContentListeners;
import com.articreep.redactedpit.content.RedactedPlayer;
import com.articreep.redactedpit.utils.Operation;
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
    private final Main plugin;
    public GoldCommand(Main plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Operation operation;

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /gold [add/set/check] [player] <amount> - negative numbers allowed!");
        } else if (args.length == 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /gold [add/set/check] [player] <amount> - negative numbers allowed!");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("check")) {

                double gold;
                String name;

                // Is it an online player?
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    // Probably is a UUID
                    if (plugin.getPlayerConfig().contains("players." + args[1])) {
                        // Grab gold value
                        gold = plugin.getPlayerConfig().getDouble("players." + args[1] + ".gold");
                        name = args[1];
                    } else {
                        sender.sendMessage(ChatColor.RED + "Invalid online player or UUID!");
                        return true;
                    }
                } else {
                    gold = ContentListeners.getRedactedPlayer(player).getGold();
                    name = player.getName();
                }
                // Display gold count
                sender.sendMessage(ChatColor.YELLOW + name + " has " + gold + " gold");
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /gold [add/set/check] [player] <amount> - negative numbers allowed!");
            }
        } else if (args.length == 3) {
            // Evaluate the operation first
            if (args[0].equalsIgnoreCase("add")) operation = Operation.ADD;
            else if (args[0].equalsIgnoreCase("set")) operation = Operation.SET;
            else if (args[0].equalsIgnoreCase("check")) operation = Operation.CHECK;
            else {
                sender.sendMessage(ChatColor.RED + "Invalid operation.");
                sender.sendMessage(ChatColor.RED + "Usage: /gold [add/set/check] [player] <amount> - negative numbers allowed!");
                return true;
            }
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
            if (operation == Operation.ADD) {
                if (redactedPlayer.addGold(amount)) {
                    sender.sendMessage(ChatColor.GREEN + "Sucessfully added " + amount + " gold to " + player.getName());
                } else {
                    sender.sendMessage(ChatColor.RED + "That would end up with a negative amount of gold!");
                }
            } else if (operation == Operation.SET) {
                if (amount < 0) {
                    sender.sendMessage(ChatColor.RED + "You may not set gold to a negative number!");
                    return true;
                }
                redactedPlayer.setGold(amount);
                sender.sendMessage(ChatColor.GREEN + "Sucessfully set " + player.getName() + "'s gold to " + amount);
            } else {
                sender.sendMessage(ChatColor.RED + "The \"check\" operation only requires one argument - an online player or a UUID.");
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

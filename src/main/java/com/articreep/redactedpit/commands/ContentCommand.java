package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.content.Content;
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

import java.util.*;

public class ContentCommand implements CommandExecutor, TabCompleter {
    private final Main plugin;
    public ContentCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Two arguments: The action (add, remove, or check), online player, and content
        Operation operation;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /content [add/remove/check] [player] <content>");
            return true;
        } else if (args.length == 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /content [add/remove/check] [player] <content>");
            return true;
        } else if (args.length == 2) {
            // Did they use the check operation?
            if (args[0].equalsIgnoreCase("check")) {

                // Initialize some data objects
                List<String> contentList = new ArrayList<>();
                double percentContent;
                String name;

                // Is it an online player?
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    // Likely a UUID. Let's check our playerdata config.
                    if (plugin.getPlayerConfig().contains("players." + args[1])) {
                        // Cool, it's an actual UUID.
                        // Grab and display all content discovered and percentages
                        contentList = (List<String>) plugin.getPlayerConfig().getList("players." + args[1] + ".content");
                        percentContent = plugin.getPlayerConfig().getDouble("players." + args[1] + ".percentcontent");
                        name = args[1];
                    } else {
                        sender.sendMessage(ChatColor.RED + "Invalid online player or UUID!");
                        return true;
                    }
                } else {
                    // Player is online, so get their RedactedPlayer object
                    RedactedPlayer redactedPlayer = ContentListeners.getRedactedPlayer(player);
                    for (Content content : redactedPlayer.getContentDiscovered()) {
                        contentList.add(content.toString());
                    }
                    percentContent = redactedPlayer.getPercentContent();
                    name = player.getName();
                }

                // Now we actually send the values to the sender
                StringBuilder builder = new StringBuilder(ChatColor.YELLOW + name + "'s Content: " + ChatColor.RESET);
                int invalid = 0;
                for (String string : contentList) {
                    try {
                        Content.valueOf(string);
                        builder.append(string).append(", ");
                    } catch (IllegalArgumentException e) {
                        builder.append(ChatColor.RED).append(string).append(ChatColor.RESET).append(", ");
                        invalid++;
                    }
                }
                sender.sendMessage(builder.toString());
                sender.sendMessage("% Content: " + percentContent + "%, " + (contentList.size() - invalid) + "/" + Content.values().length);
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /content [add/remove/check] [player] <content>");
            }
            return true;
        } else if (args.length == 3) {
            // Evaluate the operation first
            if (args[0].equalsIgnoreCase("add")) operation = Operation.ADD;
            else if (args[0].equalsIgnoreCase("remove")) operation = Operation.REMOVE;
            else if (args[0].equalsIgnoreCase("check")) operation = Operation.CHECK;
            else {
                sender.sendMessage(ChatColor.RED + "Invalid operation.");
                sender.sendMessage(ChatColor.RED + "Usage: /content [add/remove/check] [player] <content>");
                return true;
            }
            // If the operation is add/remove
            if (operation == Operation.ADD || operation == Operation.REMOVE) {

                // Get the player
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    sender.sendMessage(ChatColor.RED + "Invalid player! Must be online.");
                    return true;
                }
                RedactedPlayer redactedPlayer = ContentListeners.getRedactedPlayer(player);

                // Is the third argument "all"?
                if (args[2].equalsIgnoreCase("all")) {
                    if (operation == Operation.ADD) {
                        for (Content content : Content.values()) {
                            redactedPlayer.addContent(content);
                        }
                        sender.sendMessage(ChatColor.GREEN + "Sucessfully added all content to " + player.getName());
                    } else {
                        for (Content content : Content.values()) {
                            redactedPlayer.removeContent(content);
                        }
                        sender.sendMessage(ChatColor.GREEN + "Sucessfully removed all content from " + player.getName());
                        sender.sendMessage(ChatColor.GREEN + "Use /resetcontent <player> to reset everything from a player!");
                    }

                } else {
                    // Is the content correct?
                    try {
                        Content content = Content.valueOf(args[2].toUpperCase(Locale.ROOT));
                        if (operation == Operation.ADD) {
                            if (!redactedPlayer.hasContent(content)) {
                                redactedPlayer.addContent(content);
                                sender.sendMessage(ChatColor.GREEN + "Sucessfully added " + content + " to " + player.getName());
                            } else {
                                sender.sendMessage(ChatColor.RED + player.getName() + " already has " + content);
                            }
                        } else {
                            if (redactedPlayer.hasContent(content)) {
                                redactedPlayer.removeContent(content);
                                sender.sendMessage(ChatColor.GREEN + "Sucessfully removed " + content + " from " + player.getName());
                            } else {
                                sender.sendMessage(ChatColor.RED + player.getName() + " doesn't have " + content + " to begin with!");
                            }
                        }
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(ChatColor.RED + "Invalid content!");
                        StringBuilder message = new StringBuilder(ChatColor.RED + "Acceptable contents: ");
                        for (Content content : Content.values()) {
                            message.append(content).append(", ");
                        }
                        sender.sendMessage(message.toString());
                    }
                }
            } else {
                // They used the "check" operation but provided three arguments
                sender.sendMessage(ChatColor.RED + "The \"check\" operation only requires one argument, an online player or a UUID.");
            }
            return true;
        } else {
            // Generic error message
            sender.sendMessage(ChatColor.RED + "Usage: /content [add/remove/check] [player] <content>");
            return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final ArrayList<String> strings = new ArrayList<>();
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            strings.add("add");
            strings.add("remove");
            StringUtil.copyPartialMatches(args[0], strings, completions);
        } else if (args.length == 2) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                strings.add(player.getName());
            }
            StringUtil.copyPartialMatches(args[1], strings, completions);
        } else if (args.length == 3) {
            strings.add("ALL");
            for (Content content : Content.values()) {
                strings.add(content.toString());
            }
            StringUtil.copyPartialMatches(args[2], strings, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}

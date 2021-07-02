package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.content.Content;
import com.articreep.redactedpit.content.ContentListeners;
import com.articreep.redactedpit.content.RedactedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class ContentCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean operation;
        // Two arguments: The action (add or remove), online player, and content
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /content [add/remove] [player] [content]");
            return true;
        } else {
            // Is it a valid action?
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
                // Set operation.. true = add, false = remove
                operation = args[0].equalsIgnoreCase("add");
                if (args.length == 1) {
                    // they got it right but nothing else
                    sender.sendMessage(ChatColor.RED + "Usage: /content [add/remove] [player] [content]");
                    return true;
                } else if (args.length == 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /content [add/remove] [player] [content]");
                    return true;
                } else if (args.length == 3) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED + "Invalid player! Must be online.");
                        return true;
                    }
                    // Is the content correct?
                    try {
                        Content content = Content.valueOf(args[2].toUpperCase(Locale.ROOT));
                        RedactedPlayer redactedPlayer = ContentListeners.getRedactedPlayer(player);
                        if (operation == true) {
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
                    return true;
                }
            } else {
                // Generic error message
                sender.sendMessage(ChatColor.RED + "Usage: /content [add/remove] [player] [content]");
                return true;
            }
        }
        return false;
    }
}

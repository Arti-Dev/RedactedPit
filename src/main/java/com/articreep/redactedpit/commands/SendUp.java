package com.articreep.redactedpit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;

public class SendUp implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) { //Sender only typed '/sendup' and nothing else
                ((Player) sender).setVelocity(new Vector(0, 5, 0));
                sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Up you go!");
                ((Player) sender).getWorld().strikeLightning(((Player) sender).getLocation());
            } else {
                for (String arg : args) {
                    Player player = Bukkit.getPlayer(arg);
                    if (player == null) {
                        sender.sendMessage(ChatColor.RED + "You sent an invalid player!");
                        continue;
                    }
                    player.setVelocity(new Vector(0, 5, 0));
                    player.getWorld().strikeLightningEffect(player.getLocation());
                    player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Up you go!");

                }
            }
            return true;
        }
        Bukkit.getLogger().severe("Only players can run this command!");
        return true;

    }

}

package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.listeners.Listeners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spawn implements CommandExecutor {
    private final Main plugin;
    public Spawn(Main plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!Listeners.isInCombat(player)) {
                double x = plugin.getConfig().getDouble("spawn.x");
                double y = plugin.getConfig().getDouble("spawn.y");
                double z = plugin.getConfig().getDouble("spawn.z");
                float yaw = (float) plugin.getConfig().getDouble("spawn.yaw");
                float pitch = (float) plugin.getConfig().getDouble("spawn.pitch");
                Location location = new Location(Bukkit.getWorld("redacted2"), x, y, z, yaw, pitch);
                player.teleport(location);
            } else {
                player.sendMessage(ChatColor.RED + "You may not /spawn in combat!");
            }
            return true;
        }
        return false;
    }
}

package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.content.Content;
import com.articreep.redactedpit.content.ContentListeners;
import com.google.gson.JsonObject;
import net.hypixel.api.HypixelAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ImportGold implements CommandExecutor {
    Main plugin;
    HypixelAPI api;
    public ImportGold(Main plugin) {
        this.plugin = plugin;
        this.api = new HypixelAPI(UUID.fromString(plugin.getConfig().getString("apikey")));
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {

            Player player = (Player) sender;
            if (ContentListeners.getRedactedPlayer(player).hasImportedBefore()) {
                player.sendMessage(ChatColor.RED + "You have already imported your gold before!");
                return true;
            }
            CompletableFuture<Double> goldfuture = getGoldAPI(api, player.getUniqueId().toString());
            while (!goldfuture.isDone()) {
                // do nothing
            }
            try {
                ContentListeners.getRedactedPlayer(player).setGold(goldfuture.get());
                ContentListeners.getRedactedPlayer(player).importedSuccessfully();
                player.sendMessage(ChatColor.GOLD + "Gold imported successfully!");
            } catch (InterruptedException e) {
                e.printStackTrace();
                player.sendMessage(ChatColor.RED + "There was an issue getting your gold!");
            } catch (ExecutionException e) {
                Bukkit.getLogger().severe("The API Key specified in config.yml is not valid!");
                player.sendMessage(ChatColor.RED + "There was an issue getting your gold! API key not valid.");
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
    public static CompletableFuture<Double> getGoldAPI(HypixelAPI api, String uuid) {
        return api.getPlayerByUuid(UUID.fromString(uuid)).thenApply((reply) -> {
            JsonObject player = reply.getPlayer();
            JsonObject stats = player.getAsJsonObject("stats");
            JsonObject pit = stats.getAsJsonObject("Pit");
            JsonObject pit2 = pit.getAsJsonObject("profile");
            BigDecimal bd = BigDecimal.valueOf(pit2.get("cash").getAsDouble());
            bd = bd.setScale(2, RoundingMode.DOWN);
            return bd.doubleValue();
        });

    }
}

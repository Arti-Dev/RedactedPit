package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.Utils;
import com.articreep.redactedpit.content.Content;
import com.articreep.redactedpit.content.ContentListeners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class QuestBook implements CommandExecutor {
    private static List<Content> contentList;
    static {
        contentList = Arrays.asList(Content.values());
        Collections.shuffle(contentList);
    }
    private static HashMap<Integer, Integer> numberToSlot = new HashMap<Integer, Integer>() {{
        put(1, 12);
        put(2, 13);
        put(3, 14);
        put(4, 21);
        put(5, 22);
        put(6, 23);
        put(7, 30);
        put(8, 31);
        put(9, 32);
    }};
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (ContentListeners.getRedactedPlayer(player).hasImportedBefore()) {
                player.openInventory(createInventory(player));
            } else {
                player.openInventory(createTutorial());
            }
            return true;
        }
        return false;
    }

    public static Inventory createInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "Quest Book");
        inv.setItem(4, Utils.createGuiItem(Material.PAPER, ChatColor.AQUA + "Quest Book"));
        int i = 0;
        if (!ContentListeners.getRedactedPlayer(player).hasContent(Content.HIDDEN_ARCHAEOLOGIST)) {
            inv.setItem(numberToSlot.get(i + 1), Utils.createGuiItem(Material.BOOK_AND_QUILL, ChatColor.AQUA + Content.HIDDEN_ARCHAEOLOGIST.getId(),
                    ChatColor.YELLOW + Content.HIDDEN_ARCHAEOLOGIST.getBookDescription()));
            i++;
        }
        if (!ContentListeners.getRedactedPlayer(player).hasContent(Content.WARPING_TO_THE_PAST)) {
            inv.setItem(numberToSlot.get(i + 1), Utils.createGuiItem(Material.BOOK_AND_QUILL, ChatColor.AQUA + Content.WARPING_TO_THE_PAST.getId(),
                    ChatColor.YELLOW + Content.WARPING_TO_THE_PAST.getBookDescription()));
            i++;
        }
        if (!ContentListeners.getRedactedPlayer(player).hasContent(Content.TALK_TO_MINER)) {
            inv.setItem(numberToSlot.get(i + 1), Utils.createGuiItem(Material.BOOK_AND_QUILL, ChatColor.AQUA + Content.TALK_TO_MINER.getId(),
                    ChatColor.YELLOW + Content.TALK_TO_MINER.getBookDescription()));
            i++;
        }
        for (Content content : contentList) {
            if (content.getBookDescription() == null) continue;
            // Featured quests
            if (ContentListeners.getRedactedPlayer(player).hasContent(content)) continue;
            if (content == Content.HIDDEN_ARCHAEOLOGIST || content == Content.WARPING_TO_THE_PAST || content == Content.TALK_TO_MINER) continue;
            inv.setItem(numberToSlot.get(i + 1), Utils.createGuiItem(Material.BOOK_AND_QUILL,
                    ChatColor.AQUA + content.getId(), ChatColor.YELLOW + content.getBookDescription()));
            i++;
            if (i >= 9) break;
        }
        if (i == 0) {
            inv.setItem(22, Utils.createGuiItem(Material.GLOWSTONE_DUST, ChatColor.RED + "Nothing here!",
                    ChatColor.YELLOW + "You achieved 100% Content Discovered!"));
        }
        inv.setItem(49, Utils.createGuiItem(Material.DIRT, ChatColor.AQUA + "Re-read the Tutorial"));
        return inv;
    }

    public static Inventory createTutorial() {
        Inventory inv = Bukkit.createInventory(null, 54, "Tutorial");
        inv.setItem(10, Utils.createGuiItem(Material.DIRT, ChatColor.AQUA + "Welcome to the Redacted Pit - the Time Travel Map!",
                ChatColor.YELLOW + "This server was created as a place\nto develop lots of ideas that the\n[redacted] group wanted to make,\nall on one server.\n\n" +
                "Please note that not all ideas will be good,\nand not all ideas are completed and usable.\n\n" +
                        "Some things are also intentionally\nunbalanced for accessibility,\nin addition we are not professional builders.\n\n" +
                "Hover over the other items for more!"));
        inv.setItem(12, Utils.createGuiItem(Material.SEA_LANTERN, ChatColor.AQUA + "Progression",
                ChatColor.YELLOW + "The goal of this server is not to level up,\nit's to discover all of the content that \nwe have put out on here.\n\n" +
                "Reaching 100% Content Discovered\non your scoreboard \nmeans that you've seen\neverything we've created,\nand there isn't much else\nfor you on this server.\n\n" +
                "Feel free to let us know if \nyou found something you\nthink we forgot to include!\n"));
        inv.setItem(14, Utils.createGuiItem(Material.CHEST, ChatColor.AQUA + "Chests!",
                ChatColor.YELLOW + "All players are encouraged to open any chest they see.\n\n" +
                "A lot of ideas were stored in chests anyways,\nso they're very important.\n\n" +
                "You cannot modify them, however."));
        inv.setItem(16, Utils.createGuiItem(Material.BOOK, ChatColor.AQUA + "Discovering things",
                ChatColor.YELLOW + "In order to increase your Content Discovered percentage,\nyou must travel throughout the map.\n\n" +
                "Entering certain areas will grant you a discovery,\nwhile some require you to interact\nwith NPCs or use items.\n\n" +
                "If you're not sure what to do,\nlook at your quest book!"));
        inv.setItem(28, Utils.createGuiItem(Material.IRON_SWORD, ChatColor.AQUA + "PVP",
                ChatColor.YELLOW + "This server is not meant to be competitive,\nhowever PVP is enabled.\n\n" +
                "You probably shouldn't be 100% PVPing on this server.\n\n" +
                "Each kill grants a flat 50g.\nNot all kill mechanics are enabled.\n\n" +
                "There are no perks, but there\nare golden apples on kill.\nUse /kit pit, /kit shopdia, and /spawn.\n\n" +
                "Everything is kept on death.\n\n" +
                "Do not spam kills on NPCs that trigger kill messages."));
        inv.setItem(30, Utils.createGuiItem(Material.GOLD_INGOT, ChatColor.AQUA + "Pit Mechanics",
                ChatColor.YELLOW + "You can import your gold from\nactual Pit using /importgold.\nYou can only do this once.\n\n" +
                "The launchers should work.\nThey aren't perfect, but they're launchers.\n\n" +
                "Certain statistics are only tracked\nin the Colosseum\nand can be viewed with /coloinfo"));
        inv.setItem(32, Utils.createGuiItem(Material.FLINT, ChatColor.AQUA + "TL:DR",
                ChatColor.YELLOW + "- We made a lot of ideas and dumped them in this server\n" +
                "- Your goal is to reach 100% Content Discovered\n" +
                "- You can open chests, just can't modify them\n" +
                "- Look at your Quest Book if you don't know what to do\n" +
                "- Use /importgold to import your gold from actual Pit"));
        inv.setItem(34, Utils.createGuiItem(Material.ARROW, ChatColor.AQUA + "Ready to go?",
                ChatColor.YELLOW + "Click here to view the Quest Book.\n" +
                "If you keep getting this tutorial every single time,\nyou need to import your gold!"));
        return inv;
    }
}

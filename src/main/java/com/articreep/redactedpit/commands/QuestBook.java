package com.articreep.redactedpit.commands;

import com.articreep.redactedpit.utils.Utils;
import com.articreep.redactedpit.content.Content;
import com.articreep.redactedpit.content.ContentListeners;
import com.articreep.redactedpit.content.RedactedPlayer;
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
    private static final List<Content> contentList;
    static {
        contentList = Arrays.asList(Content.values());
        Collections.shuffle(contentList);
    }
    private static final HashMap<Integer, Integer> numberToSlot = new HashMap<Integer, Integer>() {{
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
            RedactedPlayer redplayer = ContentListeners.getRedactedPlayer(player);
            if (!redplayer.hasQuestbookOpened()) {
                redplayer.setQuestbookOpened(true);
            }
            if (redplayer.hasImportedBefore()) {
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
        if (!ContentListeners.getRedactedPlayer(player).hasContent(Content.WARPING_TO_THE_PAST)) {
            inv.setItem(numberToSlot.get(i + 1), Utils.createGuiItem(Material.BOOK_AND_QUILL, ChatColor.AQUA + Content.WARPING_TO_THE_PAST.getId(),
                    ChatColor.YELLOW + Content.WARPING_TO_THE_PAST.getBookDescription()));
            i++;
        }
        if (!ContentListeners.getRedactedPlayer(player).hasContent(Content.HIDDEN_ARCHAEOLOGIST)) {
            inv.setItem(numberToSlot.get(i + 1), Utils.createGuiItem(Material.BOOK_AND_QUILL, ChatColor.AQUA + Content.HIDDEN_ARCHAEOLOGIST.getId(),
                    ChatColor.YELLOW + Content.HIDDEN_ARCHAEOLOGIST.getBookDescription()));
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
                ChatColor.YELLOW + "This server was created as a place", ChatColor.YELLOW + "to develop lots of ideas that the", ChatColor.YELLOW +
                        "[redacted] group wanted to make,", ChatColor.YELLOW + "all on one server.." + ChatColor.YELLOW + "treat it like a museum.", "", ChatColor.YELLOW +
                "Please note that not all ideas will be good,", ChatColor.YELLOW + "and not all ideas are completed and usable.", "", ChatColor.YELLOW +
                        "Some things are also intentionally", ChatColor.YELLOW + "unbalanced for accessibility,", ChatColor.YELLOW +
                        "in addition we are not professional builders.", "", ChatColor.YELLOW +
                "Hover over the other items for more!"));
        inv.setItem(12, Utils.createGuiItem(Material.SEA_LANTERN, ChatColor.AQUA + "Progression",
                ChatColor.YELLOW + "The goal of this server is not to level up,", ChatColor.YELLOW + "it's to discover all of the content that", ChatColor.YELLOW +
                        "we have put out on here.", "", ChatColor.YELLOW +
                "Reaching 100% Content Discovered", ChatColor.YELLOW + "on your scoreboard ", ChatColor.YELLOW + "means that you've seen", ChatColor.YELLOW +
                        "everything we've created,", ChatColor.YELLOW + "and there isn't much else", ChatColor.YELLOW + "for you on this server.", "", ChatColor.YELLOW +
                "Feel free to let us know if", ChatColor.YELLOW + "you found something you", ChatColor.YELLOW + "think we forgot to include!"));
        inv.setItem(14, Utils.createGuiItem(Material.CHEST, ChatColor.AQUA + "Chests!",
                ChatColor.YELLOW + "All players are encouraged to open any chest they see.", "", ChatColor.YELLOW +
                "A lot of ideas were stored in chests anyways,", ChatColor.YELLOW + "so they're very important.", "", ChatColor.YELLOW +
                "You cannot modify them, however."));
        inv.setItem(16, Utils.createGuiItem(Material.BOOK, ChatColor.AQUA + "Discovering things",
                ChatColor.YELLOW + "In order to increase your Content Discovered percentage,", ChatColor.YELLOW + "you must travel throughout the map.", "", ChatColor.YELLOW +
                "Entering certain areas will grant you a discovery,", ChatColor.YELLOW + "while some require you to interact", ChatColor.YELLOW + "with NPCs or use items.", "", ChatColor.YELLOW +
                "If you're not sure what to do,", ChatColor.YELLOW + "look at your quest book!"));
        inv.setItem(28, Utils.createGuiItem(Material.IRON_SWORD, ChatColor.AQUA + "PVP",
                ChatColor.YELLOW + "This server is not meant to be competitive,", ChatColor.YELLOW + "however PVP is enabled.", "", ChatColor.YELLOW +
                "You probably shouldn't be 100% PVPing on this server.", "", ChatColor.YELLOW +
                "Each kill grants a flat 50g.", ChatColor.YELLOW + "Not all kill mechanics are enabled.", "", ChatColor.YELLOW +
                "There are no perks, but there", ChatColor.YELLOW + "are golden apples on kill.", ChatColor.YELLOW + "Use /kit pit, /kit shopdia, and /spawn.", "", ChatColor.YELLOW +
                "Everything is kept on death.", "", ChatColor.YELLOW +
                "Do not spam kills on NPCs that trigger kill messages.", "", ChatColor.YELLOW + "If you wish to opt out of PVP, use /god.",
                ChatColor.YELLOW + "Abuse of commands will result in a ban."));
        inv.setItem(30, Utils.createGuiItem(Material.GOLD_INGOT, ChatColor.AQUA + "Pit Mechanics",
                ChatColor.YELLOW + "You can import your gold from", ChatColor.YELLOW + "actual Pit using /importgold.", ChatColor.YELLOW +
                        "You can only do this once.", "", ChatColor.YELLOW +
                "The launchers should work.", ChatColor.YELLOW + "They aren't perfect, but they're launchers.", "", ChatColor.YELLOW +
                "Certain statistics are only tracked", ChatColor.YELLOW + "in the Colosseum", ChatColor.YELLOW + "and can be viewed with /coloinfo"));
        inv.setItem(32, Utils.createGuiItem(Material.FLINT, ChatColor.AQUA + "TL:DR",
                ChatColor.YELLOW + "- We made a lot of ideas and dumped them in this server", ChatColor.YELLOW +
                "- Your goal is to reach 100% Content Discovered", ChatColor.YELLOW +
                "- You can open chests, just can't modify them", ChatColor.YELLOW +
                "- Look at your Quest Book if you don't know what to do", ChatColor.YELLOW +
                "- Use /importgold to import your gold from actual Pit"));
        inv.setItem(34, Utils.createGuiItem(Material.ARROW, ChatColor.AQUA + "Ready to go?",
                ChatColor.YELLOW + "Click here to view the Quest Book.", ChatColor.YELLOW +
                "If you keep getting this tutorial every single time,", ChatColor.YELLOW + "you need to import your gold!"));
        return inv;
    }
}

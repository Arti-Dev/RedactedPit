package com.articreep.redactedpit.content;

import com.articreep.redactedpit.UtilBoundingBox;
import org.bukkit.ChatColor;
import org.graalvm.compiler.core.common.util.Util;

public enum Content {
    // Some of these enums have boundingboxes that must be entered to discover
    JUMP_IN_PIT(new UtilBoundingBox(13, 42, 12, -14, 55, -15), "The Redacted Pit",
            "Unfortunately, the middle is asymmetrical -\nbut the middle features corridors that serve as pathways to the outskirts."),
    SHITASS_CHEST(new UtilBoundingBox(10, 42, -6, 13, 47, -10), "Funny Shitass Chest",
            "This chest houses the items used to make the \"Hey Shitass, wanna godfight?\" meme.\nWatch it here: https://youtu.be/DVvJhYdWuVs"),
    EGYPT(new UtilBoundingBox(-15, 43, -28, 53, 97, -119), "Egypt Area",
            "Egypt is home to a very dry desert. \nThis was the very first quadrant the [redacted] team worked on."),
    SUN_PYRAMID(new UtilBoundingBox(2, 44, -56, 28, 64, -82), "Sun Pyramid",
            "The ancient Sun Pyramid used to grant buffs to people who fought on it, but it currently lacks power.\nMaybe you can help the Sun King?"),
    ANCIENT_TOWN(new UtilBoundingBox(31, 44, -1, 117, 98, -64), "Ancient Town",
            "Quoting LogicSoba from the redacted Discord server:\nThere is a town that can trade kewl stuffs\n" +
                    "Here are some things you can do:\n" +
                    "Sell your artifacts to the villagers for gold\n" +
                    "Sell your artifacts for special items"),
    //TODO add shovel item
    LAVA_PIT(new UtilBoundingBox(-5, 43, -68, -20, 51, -87), "Pit of Things that are dead to me",
            ChatColor.RED + "" + ChatColor.BOLD + "jump in, you're dead to us."),
    HIDDEN_ARCHAEOLOGIST(new UtilBoundingBox(6, 38, -27, 0, 41, -34), "Hidden Archaeologist",
            "This NPC has been living under de watah for ages. \nThis content was never properly coded."),
    FIRE_TRIAL(new UtilBoundingBox(69, 93, -95, 81, 105, -73), "Fire Trials",
            "Another rip-off from SkyBlock.."),
    BOB_DESERT(new UtilBoundingBox(49, 72, -99, 55, 84, -92), "Bob, fixing the ladder.",
            "Whose idea was this?"),
    TRADE_ANCIENT_ARTIFACT_TO_TRADEMASTER(null, "Too Much Gold", "Why would you do that?\n" +
            "Joke: The gold system didn't exist when I made this, so I instead gave the player a lot of gold ingots."),
    NINJAFREEZE(new UtilBoundingBox(108, 45, -12, 116, 48, -17), "Ninjafreeze's Cell",
            "Note from Arti: I didn't make this, and I don't know why someone did.."),
    DIVINE_GLASS(null, "Divine Glass",
            "Divine Glass is essentially your everyday Pit obsidian - \nexcept it breaks much more quickly, and it's glass!\n" +
                    "Our intentions were to create a block that would be used temporarily to block players off, not long-term.\n" +
                    "Plus, it's kept on death!"),
    FUTURE_RACE_DISCOVERY(new UtilBoundingBox(58, 47, 65, 68, 61, 75), "Future Race",
            "A race that's based around launchpads!"),
    FUTURE_RACE_COMPLETE(null, "Future Race Complete", "There's a secret reward that probably isn't obtainable with commands."),
    SUN_STONE_PLACE(null, "Sun Stone Placed", "The Sun Stone powers the age-old Pyramid, \ngranting it its ancient reward buffs."),
    DAX_DUNGEON(new UtilBoundingBox(7, 53, -105, 15, 63, -100), "Dax's Strange Dungeon",
            "What do you want me to put here, Dax?"),
    DAX_DUNGEON_END(new UtilBoundingBox(16, 53, -134, 8, 56, -137), "Dax's locked chest",
            "Pretend that there were Bank I books in here..");

    private final UtilBoundingBox box;
    private final String id;
    private final String description;
    Content(UtilBoundingBox box, String identifier, String description) {
        this.box = box;
        this.id = identifier;
        this.description = description;
    }

    public UtilBoundingBox getBox() {
        return box;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}

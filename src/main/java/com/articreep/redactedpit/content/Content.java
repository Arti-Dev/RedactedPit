package com.articreep.redactedpit.content;

import com.articreep.redactedpit.UtilBoundingBox;

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

package com.articreep.redactedpit.content;

import com.articreep.redactedpit.UtilBoundingBox;
import org.bukkit.ChatColor;

public enum Content {
    // Some of these enums have boundingboxes that must be entered to discover
    JUMP_IN_PIT(new UtilBoundingBox(13, 42, 12, -14, 55, -15), "The Redacted Pit",
            "Unfortunately, the middle is asymmetrical -\nbut the middle features corridors that serve as pathways to the outskirts.",
            "Jump in the Pit!"),
    SHITASS_CHEST(new UtilBoundingBox(10, 42, -6, 13, 47, -10), "Funny Shitass Chest",
            "This chest houses the items used to make the \"Hey Shitass, wanna godfight?\" meme.\nWatch it here: https://youtu.be/DVvJhYdWuVs",
            "There's a special chest inside the middle of Pit.."),
    EGYPT(new UtilBoundingBox(-15, 43, -28, 53, 97, -119), "Egypt Area",
            "Egypt is home to a very dry desert. \nThis was the very first quadrant the [redacted] team worked on.",
            "Enter the Egypt area"),
    SUN_PYRAMID(new UtilBoundingBox(2, 44, -56, 28, 64, -82), "Sun Pyramid",
            "The ancient Sun Pyramid used to grant buffs to people who fought on it, but it currently lacks power.\nMaybe you can help the Sun King?",
            "There's a large pyramid on the right side of the Egypt area.."),
    ANCIENT_TOWN(new UtilBoundingBox(31, 44, -1, 117, 98, -64), "Ancient Town",
            "Quoting LogicSoba from the redacted Discord server:\nThere is a town that can trade kewl stuffs\n" +
                    "Here are some things you can do:\n" +
                    "Sell your artifacts to the villagers for gold\n" +
                    "Sell your artifacts for special items", "The town is located on the right side of Egypt."),
    //TODO add shovel item
    LAVA_PIT(new UtilBoundingBox(-5, 43, -68, -20, 51, -87), "Pit of Things that are dead to me",
            ChatColor.RED + "" + ChatColor.BOLD + "jump in, you're dead to us.", "-12, 45, -78"),
    HIDDEN_ARCHAEOLOGIST(new UtilBoundingBox(6, 38, -27, 0, 41, -34), "Hidden Archaeologist",
            "This NPC has been living under de watah for ages. \nClick the guy next to her for a special shovel!",
            "Click the button at 10, 43, -27"),
    FIRE_TRIAL(new UtilBoundingBox(69, 93, -95, 81, 105, -73), "Fire Trials",
            "Another rip-off from SkyBlock..",
            "There's a warp inside the SkyBlock ripoff room."),
    BOB_DESERT(new UtilBoundingBox(49, 72, -99, 55, 84, -92), "Bob, fixing the ladder.",
            "Whose idea was this?",
            "There's a stairway up the mountain behind a cactus in Egypt.."),
    TRADE_ANCIENT_ARTIFACT_TO_TRADEMASTER(null, "Too Much Gold", "Why would you do that?\n" +
            "Joke: The gold system didn't exist when I made this, so I instead gave the player a lot of gold ingots.",
            "Trade an Ancient Artifact to the Trading Master in Egypt"),
    NINJAFREEZE(new UtilBoundingBox(108, 45, -12, 116, 48, -17), "[removed]'s Cell",
            "Someone is locked up.",
            "There's a hidden cave behind a statue in Egypt"),
    DIVINE_GLASS(null, "Divine Glass",
            "Divine Glass is essentially your everyday Pit obsidian - \nexcept it breaks much more quickly, and it's glass!\n" +
                    "Our intentions were to create a block that would be used temporarily to block players off, not long-term.\n" +
                    "Plus, it's kept on death!",
            "Buy it from an NPC in Egypt"),
    FUTURE(new UtilBoundingBox(10, 96, 6, 124, 25, 141,26, 43, 24, 10, 96, 6), "Future Area",
            "A glimpse of the future, all localized into one quarter. \nThis was featured by LogicSoba several times - try putting shaders on!\nThis was the second area that the [redacted] team worked on",
            "Enter the Future area"),
    JUMPPADS(null, "Future Jumppads", "A cool mechanic that provides for some interesting gameplay!\n" +
            "The 'charging' mechanic where you must stand there for a second before it launches you is actually unintentional, but it's good balance.",
            "Look for pressure plates in Future!"),
    VOID_CHARM(null, "Void Charms", "A lot of people don't like void, so this item gives you a second chance at living!",
            "There's a NPC on a floating island in Future."),
    STIM_GUN_RHYLIE(new UtilBoundingBox(18, 43, 39, 8, 57, 76), "AgentRhylie's Stim Gun",
            "Rhylie, what do you want me to put here?", "Located on the far right side of Future"),
    POG_TOWER(new UtilBoundingBox(100, 103, 25, 121, 108, 47), "Pog Tower",
            "The tower is pogging :pog:",
            "Click the POG tower in Future"),
    FUTURE_RACE_DISCOVERY(new UtilBoundingBox(58, 47, 65, 68, 61, 75), "Future Race",
            "A race that's based around launchpads!", "Located in the back of Future"),
    FUTURE_RACE_COMPLETE(null, "Future Race Complete", "There's a secret reward that probably isn't obtainable with commands.",
            "Complete the Future Race"),
    SUN_STONE_PLACE(null, "Sun Stone Placed", "The Sun Stone powers the age-old Pyramid, \ngranting it its ancient reward buffs.",
            "Buy a Sun Stone in Egypt"),
    DAX_DUNGEON(new UtilBoundingBox(7, 53, -105, 15, 63, -100), "Dax's Strange Dungeon",
            "What do you want me to put here, Dax?",
            "Look for a gold door in Egypt.."),
    //TODO UPDATE IN FUTURE
    DAX_DUNGEON_END(new UtilBoundingBox(16, 53, -134, 8, 56, -137), "Dax's locked chest",
            "Pretend that there were Bank I books in here..", "Unobtainable right now!"),
    TIME_MACHINE(new UtilBoundingBox(98, 43, 46, 101, 40, 42), "Time Machine",
            "Transports you between the Future and Jurassic time periods.\n\"non-euclidean time machine done\n" +
                    "it only works flawlessly if ur render distance is like the whole map oofie\"\n",
            "Look behind the NPC Pearl in Future"),
    STRANDED_TRAVELER(new UtilBoundingBox(-67, 43, -44, -60, 49, -53), "Stranded Traveler",
            "Looks like he's stuck..\nThis could have definitely been turned into a quest.",
            "Look for a campsite in Jurassic"),
    JURASSIC_CAVE(new UtilBoundingBox(-60, 47, -62, -46, 38, -79), "Jurrasic Cave",
            "I'm not sure what the lore behind this is.. Woool? Can you help me?", "Look for where the river flows into in Jurassic"),
    BOBCATG(new UtilBoundingBox(-103, 67, -42, -108, 73, -50), "bobcatg's grave",
            "He was a god of the bow.", "Climb the mountain in Jurassic"),
    SCRAPPED_ALTAR(new UtilBoundingBox(-47, 21, 1, -63, 29, 11), "Scrapped Jurassic Altar",
            "This altar was built to be a global quest per lobby. It was eventually scrapped.",
            "There's a warp inside the SkyBlock ripoff room."),
    JURASSIC(new UtilBoundingBox(-32, 43, -2, -119, 95, -116), "Jurassic Area",
            "This area is home to a lush jungle, in addition to many Dinos.\nThis area was the third area the [redacted] team worked on.", null),
    COLOSSEUM(new UtilBoundingBox(-4, 98, 10, -100, 30, 98, -4, 98, 10, -23, 30, 23), "Roman Colosseum",
            "The Colosseum is a nice and flat area for (hopefully) fair fights!\nThis is the last section the [redacted] team worked on.\n" +
                    "An audience cheers on the sidelines, powering up or striking down certain fighters.\n" +
                    ChatColor.ITALIC + "From Arti: I wanted to add horses, but we never got around to it..", "Enter the Colosseum"),
    ARTI_CAKE(new UtilBoundingBox(-88, 48, 64, -91, 53, 68), "Arti's Cake",
            "One day, there was a running joke that it was always my birthday for some dumb reason.\n" +
                    "This was in commemoration of that..", "Use Divine Glass to climb the empty spectator area"),
    UNDERGROUND_SHORTCUTS(new UtilBoundingBox(-25, 37, 63, -31, 33, 61), "Underground Shortcuts",
            "These are some quick shortcuts you can use if you wanna quickly appear on the other side!\nBut seriously, why would you?",
            "Look for holes in the Colosseum"),
    AUDIENCE(null, "Colosseum Audience", "Ah, the audience. They love to cheer on people they like, and hate on people they don't like.\n" +
            "Their opinion on your will change over time based on what you do. For example, if you don't fight for a while, you'll get hated on, and will receive debuffs.\n" +
            "However, if for example you're low on health, the audience might try and save you!", "Fight in the Colosseum with other players"),
    HOT_POTATO(null, "Hot Potato", "Hurry up and pass it on!",
            "Audience Colosseum effect"),
    LARGEKB(null, "Increased KB Hit", "Probably one of the most powerful moves that the Colosseum can grant you.\n" +
            "However, I can see how people might hate it, even if they were the one who inflicted it.",
            "Audience Colosseum effect"),
    SKYBLOCK_ROOM(new UtilBoundingBox(0, 28, 10, 8, 35, -1), "SkyBlock Ripoff Room",
            "This room contains lots of SkyBlock-like concepts that have been banished to this room.\n" +
                    "There's also a few teleport corridors that lead to a few places.",
            "The entrance is between the Egypt and Future launchers."),
    TAYLOR_HATS(new UtilBoundingBox(-5, 28, -4, 0, 33, -8), "Taylor's hats",
            "Designed by LogicSoba, these are actually pretty interesting ideas. They're single-use hats that provide one-time effects.\n" +
                    "If they were to be in the game, though, they'd need major reworking to make them slightly more viable.", "Located in the SkyBlock Ripoff room"),
    AUCTION_HOUSE(new UtilBoundingBox(-15, 28, -6, -6, 35, -11), "Pit Auction House", "Designed by SoulCrushing. \n" +
            "A possible way to implement skyblock's Auction House into Pit while still trying to preserve Pit's unique trading culture.\n" +
            "Basically, after you would put a item up for auction, other people would be able to search for it, and place offers.\n" +
            "The person who put it up could then choose at the end of the auction which offer to choose.", "Located in the SkyBlock Ripoff room"),
    PUNCHING_BAG(new UtilBoundingBox(-16, 28, 7, -22, 34, 13), "Punching Bag",
            "Very similar to the Fire Trials, except for damage. This really wouldn't work well with Pit since we use vanilla damaging, however, there" +
                    "might be a better way to achieve 'progression' in this.", "Located in the SkyBlock Ripoff room"),
    //TODO Make this accessible
    RHYLIE_COMMAND_BUNKER(new UtilBoundingBox(-24, 34, 88, -18, 38, 102), "Rhylie's Command Block Bunker",
            "Various command blocks for Rhylie's Stim Gun idea",
            "There's a button in Dr. Zork's tent in Future.."),
    WARPING_TO_THE_PAST(null, "Warping to the Past",
            "You can now buy Time Warp Pearls!",
            "Talk to Pearl in Future"),
    TREASURE_CHEST(null, "Treasure Chests",
            "The generic treasure hunting game, with some extra spice.\n" +
                    "Anyone can dig up treasure, but only the person who started it has a chance at Ancient Artifacts.\n" +
                    ChatColor.ITALIC + "The soil underneath the top of the desert" +
                    "shifts old treasures back up to the surface, and if not dug up correctly, they disappear forever..",
            "Look for particles in the sand and mine them"),
    TALK_TO_MINER(null, "Strange Miner", "This guy seems like a weirdo if you ask me..\n" +
            "Upgrade your pickaxe, maybe you'll find something cool..\n" +
            "Click on him again for tips!",
            "He's located in the cave in Jurassic"),
    SPIKEAXE_OBTAIN(null, "The Spikeaxe", "You've finally upgraded your pickaxe to the best possible, the Spikeaxe!\n" +
            "Crits with this pickaxe sends your opponents straight down..\n" +
            "If they hit the ground hard enough, they'll bounce back up!\n"+
            "Or you can send them straight into the void!", "Upgrade your pickaxe to diamond through the Miner in Jurassic");

    private final UtilBoundingBox box;
    private final String id;
    private final String description;
    private final String bookDescription;
    Content(UtilBoundingBox box, String identifier, String description, String bookDescription) {
        this.box = box;
        this.id = identifier;
        this.description = description;
        this.bookDescription = bookDescription;
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

    public String getBookDescription() {
        return bookDescription;
    }
}

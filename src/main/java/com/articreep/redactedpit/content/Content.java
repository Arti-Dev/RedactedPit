package com.articreep.redactedpit.content;

import com.articreep.redactedpit.UtilBoundingBox;

public enum Content {
    // Some of these enums have boundingboxes that must be entered to discover
    FUTURE_RACE_DISCOVERY(new UtilBoundingBox(71, 47, 65, 8, 62, 76), "Future Race"),
    FUTURE_RACE_COMPLETE(null, "Future Race Complete"),
    SUN_STONE_PLACE(null, "Sun Stone Placed"),
    DAX_DUNGEON(new UtilBoundingBox(7, 53, -105, 15, 63, -100), "Dax's Strange Dungeon");

    private final UtilBoundingBox box;
    private final String id;
    Content(UtilBoundingBox box, String identifier) {
        this.box = box;
        this.id = identifier;
    }

    public UtilBoundingBox getBox() {
        return box;
    }

    public String getId() {
        return id;
    }
}

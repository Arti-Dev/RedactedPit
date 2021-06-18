package com.articreep.redactedpit.treasure;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TreasureChest {
    private Player discoverer;
    private Location location;

    public TreasureChest(Location location) {
        this.location = location;
    }

    public void setDiscoverer(Player discoverer) {
        this.discoverer = discoverer;
    }

    public Location getLocation() {
        return location;
    }
}

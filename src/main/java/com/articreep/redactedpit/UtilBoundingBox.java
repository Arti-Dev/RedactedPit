package com.articreep.redactedpit;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class UtilBoundingBox {
    private Location loc1;
    private Location loc2;
    private double lowerX;
    private double higherX;
    private double lowerY;
    private double higherY;
    private double lowerZ;
    private double higherZ;

    /**
     * Creates a bounding box.
     * If the two locations have values that are equivalent, e.g. loc1's x value equals loc2's x value, it may not make a 3D box!
     * Not world-aware
     * @param loc1 Location 1
     * @param loc2 Location 2
     */
    public UtilBoundingBox(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
        if (loc1.getX() > loc2.getX()) {
            higherX = loc1.getX();
            lowerX = loc2.getX();
        } else {
            higherX = loc2.getX();
            lowerX = loc1.getX();
        }
        if (loc1.getY() > loc2.getX()) {
            higherY = loc1.getY();
            lowerY = loc2.getY();
        } else {
            higherY = loc2.getY();
            lowerY = loc1.getY();
        }
        if (loc1.getZ() > loc2.getZ()) {
            higherZ = loc1.getZ();
            lowerZ = loc2.getZ();
        } else {
            higherZ = loc2.getZ();
            lowerZ = loc1.getZ();
        }
        //Bukkit.broadcastMessage(lowerX + " " + higherX + " " + lowerY + " " + higherY + " " + lowerZ + " " + higherZ);
    }

    public boolean isInBox(Location loc) {
        if ((lowerX <= loc.getX() && loc.getX() <= higherX
                && lowerY <= loc.getY() && loc.getY() <= higherY
                && lowerZ <= loc.getZ() && loc.getZ() <= higherZ )) {
            return true;
        }
        return false;
    }
}

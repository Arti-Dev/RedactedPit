package com.articreep.redactedpit;

import org.bukkit.Location;

public class UtilBoundingBox {
    private final double lowerX;
    private final double higherX;
    private final double lowerY;
    private final double higherY;
    private final double lowerZ;
    private final double higherZ;

    /**
     * Creates a bounding box.
     * If the two locations have values that are equivalent, e.g. loc1's x value equals loc2's x value, it may not make a 3D box!
     * Not world-aware
     * @param loc1 Location 1
     * @param loc2 Location 2
     */
    public UtilBoundingBox(Location loc1, Location loc2) {
        if (loc1.getX() > loc2.getX()) {
            higherX = loc1.getX();
            lowerX = loc2.getX();
        } else {
            higherX = loc2.getX();
            lowerX = loc1.getX();
        }
        if (loc1.getY() > loc2.getY()) {
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
    }

    public UtilBoundingBox(double x1, double y1, double z1, double x2, double y2, double z2) {
        if (x1 > x2) {
            higherX = x1;
            lowerX = x2;
        } else {
            higherX = x2;
            lowerX = x1;
        }
        if (y1 > y2) {
            higherY = y1;
            lowerY = y2;
        } else {
            higherY = y2;
            lowerY = y1;
        }
        if (z1 > z2) {
            higherZ = z1;
            lowerZ = z2;
        } else {
            higherZ = z2;
            lowerZ = z1;
        }
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

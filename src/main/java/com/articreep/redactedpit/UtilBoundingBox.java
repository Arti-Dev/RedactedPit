package com.articreep.redactedpit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class UtilBoundingBox {
    private final World world;
    private final double lowerX;
    private final double higherX;
    private final double lowerY;
    private final double higherY;
    private final double lowerZ;
    private final double higherZ;
    private final double highereX;
    private final double highereY;
    private final double highereZ;
    private final double lowereX;
    private final double lowereY;
    private final double lowereZ;

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
        highereX = 0;
        highereY = 0;
        highereZ = 0;
        lowereX = 0;
        lowereY = 0;
        lowereZ = 0;
        world = Bukkit.getWorld("redacted2");
    }


    public UtilBoundingBox(double x1, double y1, double z1, double x2, double y2, double z2, double ex1, double ey1, double ez1, double ex2, double ey2, double ez2) {
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
        // Start exclusion hitbox
        if (ex1 > ex2) {
            highereX = ex1;
            lowereX = ex2;
        } else {
            highereX = ex2;
            lowereX = ex1;
        }
        if (ey1 > ey2) {
            highereY = ey1;
            lowereY = ey2;
        } else {
            highereY = ey2;
            lowereY = ey1;
        }
        if (ez1 > ez2) {
            highereZ = ez1;
            lowereZ = ez2;
        } else {
            highereZ = ez2;
            lowereZ = ez1;
        }
        world = Bukkit.getWorld("redacted2");

    }

    public boolean isInBox(Location loc) {
        if (!loc.getWorld().equals(world)) return false;
        if ((lowerX <= loc.getX() && loc.getX() <= higherX
                && lowerY <= loc.getY() && loc.getY() <= higherY
                && lowerZ <= loc.getZ() && loc.getZ() <= higherZ )) {
            return true;
        }
        return false;
    }

    public boolean isInBoxExclude(Location loc) {
        // If in exclusion area return false
        if (!loc.getWorld().equals(world)) return false;
        if ((lowereX <= loc.getX() && loc.getX() <= highereX
                && lowereY <= loc.getY() && loc.getY() <= highereY
                && lowereZ <= loc.getZ() && loc.getZ() <= highereZ )) {
            return false;
        }
        if ((lowerX <= loc.getX() && loc.getX() <= higherX
                && lowerY <= loc.getY() && loc.getY() <= higherY
                && lowerZ <= loc.getZ() && loc.getZ() <= higherZ )) {
            return true;
        }
        return false;
    }

    public Location randomLocation() {
        int randomX = ThreadLocalRandom.current().nextInt((int) lowerX, (int) higherX + 1);
        int randomY = ThreadLocalRandom.current().nextInt((int) lowerY, (int) higherY + 1);
        int randomZ = ThreadLocalRandom.current().nextInt((int) lowerZ, (int) higherZ + 1);
        return new Location(Bukkit.getWorld("redacted2"), randomX, randomY, randomZ);
    }

    /**
     * Returns every single block location in the box.
     * Does not account for exclusion areas
     * @return List of locations
     */
    public ArrayList<Block> blockList() {
        ArrayList<Block> listofblocks = new ArrayList<>();
        for (double x = lowerX; x <= higherX; x++) {
            for (double y = lowerY; y <= higherY; y++) {
                for (double z = lowerZ; z <= higherZ; z++) {
                    Location loc = new Location(world, x, y, z);
                    listofblocks.add(loc.getBlock());
                }
            }
        }
        return listofblocks;
    }
}

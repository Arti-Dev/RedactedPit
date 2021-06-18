package com.articreep.redactedpit.listeners;

import com.articreep.redactedpit.UtilBoundingBox;
import com.articreep.redactedpit.Utils;
import com.articreep.redactedpit.commands.RedactedGive;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class TreasureListeners implements Listener {
    UtilBoundingBox treasureBox = new UtilBoundingBox(102, 47, 1, -20, 43, -87);

    @EventHandler
    public void onShovelClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().isSimilar(RedactedGive.ArcheologistShovel(1))) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                newChestLocation(event.getPlayer());
            }
        }
    }

    public Location newChestLocation(Player player) {
        Location loc;
        Location airLoc;
        Location finalLoc;
        int i = 0;
        while (true) {
            loc = treasureBox.randomLocation();
            airLoc = loc.clone();
            if (loc.getBlock().getType() == Material.SAND) {
                if (airLoc.add(0, 1, 0).getBlock().isEmpty()) {
                    // Go through all blocks around now.
                    ArrayList<Block> list = Utils.getBlocksAround(loc, 1);
                    if (list.size() == 8) {
                        for (int j = 0; j < list.size(); j++) {
                            if (!list.get(j).getLocation().add(0, 1, 0).getBlock().isEmpty()) {
                                list.remove(j);
                                break;
                            }
                        }
                        // Check again if list is size 8
                        if (list.size() == 8) {
                            player.sendMessage("Found a block!");
                            player.sendMessage("Block is at " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
                            player.teleport(airLoc.add(0.5, 0.5, 0.5));
                            player.sendMessage("There are " + Utils.getBlocksAround(loc, 1).size() + " blocks in the list!");
                            break;
                        }
                    }
                }
            }
            i++;
            // Fail-safe
            if (i > 250) {
                player.sendMessage("Couldn't find a block.. try again?");
                break;
            }
        }
        return loc;
    }



}

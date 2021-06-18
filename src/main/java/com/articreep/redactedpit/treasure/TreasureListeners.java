package com.articreep.redactedpit.treasure;

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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class TreasureListeners extends BukkitRunnable implements Listener {
    public static UtilBoundingBox treasureBox = new UtilBoundingBox(102, 47, 1, -20, 43, -87);
    // At any given time there will only be two places to dig
    public static ArrayList<TreasureChest> treasureList = new ArrayList<>();

    @EventHandler
    public void onShovelClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().isSimilar(RedactedGive.ArcheologistShovel(1))) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                // Only update locations when the shovel is used
                while (treasureList.size() < 2) {
                    try {
                        treasureList.add(newChestLocation());
                        if (treasureList.size() == 2) {
                            if (treasureList.get(0) == treasureList.get(1)) {
                                treasureList.remove(1);
                            }
                        }
                    } catch (BlockNotFoundException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                if (treasureList.size() == 0) {
                    player.sendMessage("Couldn't find a treasure.. try again?");
                } else {
                    Location loc1 = treasureList.get(0).getLocation();
                    Location loc2 = treasureList.get(1).getLocation();
                    if (loc1.distanceSquared(player.getLocation()) < loc2.distanceSquared(player.getLocation())) {
                        player.sendMessage("Nearest block is at " + loc1.getX() + " " + loc1.getY() + " " + loc1.getZ());
                    } else {
                        player.sendMessage("Nearest block is at " + loc2.getX() + " " + loc2.getY() + " " + loc2.getZ());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSandClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().isSimilar(RedactedGive.ArcheologistShovel(1))) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                for (TreasureChest treasureChest : treasureList) {
                    if (treasureChest.getLocation().getBlock().equals(event.getClickedBlock())) {
                        player.sendMessage("Coming soon");
                        break;
                    }
                }
            }
        }
    }

    public TreasureChest newChestLocation() throws BlockNotFoundException {
        Location loc;
        Location airLoc;
        Location finalLoc = null;
        int i = 0;
        // Only give this 250 tries
        while (i < 250) {
            loc = treasureBox.randomLocation();
            // airLoc is the original location but one block higher
            airLoc = loc.clone().add(0, 1, 0);
            if (loc.getBlock().getType() == Material.SAND) {
                // There must not be anything above the center block
                if (airLoc.getBlock().isEmpty()) {
                    // Go through all blocks around now.
                    ArrayList<Block> list = Utils.getBlocksAround(loc, 1);
                    // All blocks must be solid..
                    if (list.size() == 8) {
                        // Check each block above the blocks in the list. Are they empty?
                        for (int j = 0; j < list.size(); j++) {
                            if (!list.get(j).getLocation().add(0, 1, 0).getBlock().isEmpty()) {
                                list.remove(j);
                                break;
                            }
                        }
                        // Check again if list is size 8
                        if (list.size() == 8) {
                            // If so, this location works
                            finalLoc = loc;
                            break;
                        }
                    }
                }
            }
            i++;
        }
        if (finalLoc == null) {
            throw new BlockNotFoundException("A block was not found after randomly generating blocks " + i + " times.");
        }
        return new TreasureChest(finalLoc);
    }


    @Override
    public void run() {
        for (TreasureChest treasureChest : treasureList) {
            Location location = treasureChest.getLocation();
            Utils.sendSandParticle(location.getX() + 0.5, location.getY() + 1, location.getZ() + 0.5);
        }
    }
}

package com.articreep.redactedpit.treasure;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Sandstone;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("deprecation")
public class TreasureChest {
    private Main plugin;
    private Player discoverer;
    private boolean sinking = false;
    private boolean finished = false;
    private Location location;
    private byte centerData;
    private List<Block> blockOrder;
    private BukkitTask runnable;
    private ArmorStand sword;
    private HashMap<Block, HashMap<Material, Byte>> materialData = new HashMap<>();

    public TreasureChest(Main plugin, Location location, List<Block> blocks) {
        this.plugin = plugin;
        this.location = location;
        this.blockOrder = blocks;
        // Shuffle block order so it's random :)
        Collections.shuffle(blockOrder);
        // Remember materials of blocks so we can rollback
        for (Block block : blockOrder) {
            HashMap<Material, Byte> map = new HashMap<>();
            map.put(block.getType(), block.getData());
            materialData.put(block, map);
        }
        centerData = location.getBlock().getData();
    }

    public boolean hasDiscoverer() {
        return discoverer != null;
    }

    public Location getLocation() {
        return location.clone();
    }

    public List<Block> getBlockOrder() {
        return blockOrder;
    }

    public void start(Player player) {
        this.discoverer = player;
        player.sendMessage("Started, dig in the areas where particles are coming out!");
        this.location.getBlock().setType(Material.CHEST);
        runnable = new BukkitRunnable() {

            @Override
            public void run() {
                if (blockOrder.isEmpty()) {
                    finish();
                    this.cancel();
                } else {
                    Location location = blockOrder.get(0).getLocation();
                    Utils.sendPumpkinParticle(location.getX() + 0.5, location.getY() + 1, location.getZ() + 0.5);
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void progress(Player player) {
        blockOrder.get(0).setType(Material.STEP);
        blockOrder.get(0).setData((byte) 1);
        blockOrder.remove(0);
        player.sendMessage("That's the spot.. next one, perhaps?");
        if (discoverer != player) {
            discoverer.sendMessage("The chest is one step closer from being grabbed!");
        }
    }

    public void finish() {
        // Replace everything
        replaceBlocksAround();
        if (sword != null) {
            sword.remove();
        }
        if (sinking) {
            location.clone().add(0, -1, 0).getBlock().setType(Material.STONE);
        } else {
            location.getBlock().setType(Material.CHEST);
        }
        location.getBlock().setType(Material.SAND);
        location.getBlock().setData(centerData);
        finished = true;
    }

    public BukkitTask sink(Player player) {
        replaceBlocksAround();
        location.getBlock().setType(Material.AIR);
        location.add(0, -1, 0);
        location.getBlock().setType(Material.CHEST);
        sinking = true;
        player.sendMessage("Something went wrong, and the treasure is sinking! Try preventing this by sticking your sword in!");
        return new BukkitRunnable() {

            @Override
            public void run() {
                player.sendMessage("The treasure sank back down to the depths..");
                // Reset location to where it was originally
                location.add(0, 1, 0);
                // Clear the list of blocks
                blockOrder.clear();
            }
        }.runTaskLater(plugin, 200);

    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isSinking() {
        return sinking;
    }

    private void replaceBlocksAround() {
        for (Block block : materialData.keySet()) {
            for (Material material : materialData.get(block).keySet()) {
                block.setType(material);
                block.setData(materialData.get(block).get(material));
                Utils.sendSandParticle(block.getLocation().add(0.5, 1, 0.5));
                block.getWorld().playSound(block.getLocation(), Sound.DIG_SAND, 1, 1);
            }
        }
    }

    public void spawnSword(ItemStack item) {
        sword = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(1, 0.25, 1.25), EntityType.ARMOR_STAND);
        sword.setArms(true);
        sword.setGravity(false);
        sword.setVisible(false);
        sword.setItemInHand(item);
        sword.setRightArmPose(new EulerAngle(Math.toRadians(90), 0, 0));
    }
}

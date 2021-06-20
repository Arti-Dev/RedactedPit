package com.articreep.redactedpit.treasure;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;

import java.util.*;

@SuppressWarnings("deprecation")
public class TreasureChest {
    private final Main plugin;
    private Player discoverer;
    private ChestStatus status;
    private final Location location;
    private final byte centerData;
    private final List<Block> blockOrder;
    private BukkitTask runnable;
    private BukkitTask sinkingRunnable;
    private ArmorStand sword;
    private final HashMap<Block, HashMap<Material, Byte>> materialData = new HashMap<>();
    private int clicks = 0;
    private Set<Player> contributors = new HashSet<>();
    private Set<Player> looted = new HashSet<>();

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

    public void start(Player player) {
        // If the user already has done something don't start
        if (hasStatus()) {
            return;
        }
        status = ChestStatus.IN_PROGRESS;
        this.discoverer = player;
        player.sendMessage("Started, dig in the areas where particles are coming out!");
        // Location in the ground
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
        if (status == ChestStatus.SINKING) {
            player.sendMessage("You probably should be trying to get the chest out!");
        } else {
            blockOrder.get(0).setType(Material.STEP);
            blockOrder.get(0).setData((byte) 1);
            blockOrder.remove(0);
        }
    }

    public void finish() {
        // Replace everything
        replaceBlocksAround();
        clicks = 0;

        location.getBlock().setType(Material.SAND);
        location.getBlock().setData(centerData);

        if (status == ChestStatus.SINKING) {
            // Set the block two blocks under to stone
            location.clone().add(0, -1, 0).getBlock().setType(Material.STONE);
            status = ChestStatus.FINISHED_SANK;
        } else {
            // Make the location above ground now
            location.add(0, 1, 0).getBlock().setType(Material.CHEST);
            status = ChestStatus.FINISHED;
        }
    }

    public void sink(Player player) {
        if (status == ChestStatus.SINKING) {
            return;
        }
        status = ChestStatus.SINKING;
        Utils.sendLavaParticle(getLocation().add(0.5, 1, 0.5));
        location.getBlock().setType(Material.AIR);
        // Move the location of the chest down
        location.add(0, -1, 0);
        location.getBlock().setType(Material.CHEST);
        player.sendMessage("Something went wrong, and the treasure is sinking! Try preventing this by sticking your sword in!");
        sinkingRunnable = new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                i++;
                if (i >= 200) {
                    player.sendMessage("The treasure sank back down to the depths..");
                    // Reset location to where it was originally
                    location.add(0, 1, 0);

                    player.removePotionEffect(PotionEffectType.SLOW);
                    for (Player player : contributors) {
                        player.removePotionEffect(PotionEffectType.SLOW);
                    }

                    // Cancel the other runnable
                    runnable.cancel();

                    if (sword != null) {
                        sword.remove();
                        sword = null;
                    }

                    finish();
                    this.cancel();
                } else if (clicks >= 20) {
                    player.sendMessage("You got it out! Might want to be more careful next time..");
                    location.getBlock().setType(Material.STONE);

                    player.removePotionEffect(PotionEffectType.SLOW);
                    for (Player player : contributors) {
                        player.removePotionEffect(PotionEffectType.SLOW);
                    }

                    sword.remove();
                    sword = null;

                    // Move location back
                    location.add(0, 1, 0);
                    location.getBlock().setType(Material.CHEST);

                    status = ChestStatus.IN_PROGRESS;
                    clicks = 0;
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);

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

    public Location getLocation() {
        return location.clone();
    }

    public List<Block> getBlockOrder() {
        return blockOrder;
    }

    public ChestStatus getStatus() {
        return status;
    }

    public boolean hasStatus() {
        return status != null;
    }

    public boolean hasSword() {
        return sword != null;
    }

    public void addContributor(Player player) {
        if (!(player == discoverer)) {
            contributors.add(player);
        }
    }

    public void addLootedChest(Player player) {
        looted.add(player);
    }

    public Player getDiscoverer() {
        return discoverer;
    }

    public boolean hasLooted(Player player) {
        return looted.contains(player);
    }

    public void increaseClicks() {
        clicks++;
    }
}

package com.articreep.redactedpit.treasure;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.UtilBoundingBox;
import com.articreep.redactedpit.Utils;
import com.articreep.redactedpit.commands.RedactedGive;
import com.articreep.redactedpit.content.ContentListeners;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class TreasureListeners extends BukkitRunnable implements Listener {
    private final Main plugin;
    public static UtilBoundingBox treasureBox = new UtilBoundingBox(102, 47, 1, -20, 40, -87);
    // At any given time there will only be two places to dig
    public static ArrayList<TreasureChest> treasureList = new ArrayList<>();
    public static ArrayList<TreasureChest> dugTreasureList = new ArrayList<>();
    public TreasureListeners(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShovelClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().isSimilar(RedactedGive.ArcheologistShovel(1))) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (treasureList.size() == 0) {
                    player.sendMessage("Couldn't find a treasure.. try again?");
                } else {
                    Location loc1 = treasureList.get(0).getLocation();
                    //
                    if (treasureList.size() == 1) {
                        player.sendMessage("Nearest block is at " + loc1.getX() + " " + loc1.getY() + " " + loc1.getZ());
                    } else if (treasureList.size() == 2) {
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
    }

    @EventHandler
    public void onSandClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().isSimilar(RedactedGive.ArcheologistShovel(1))) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                for (TreasureChest treasureChest : treasureList) {
                    if (treasureChest.getLocation().getBlock().equals(event.getClickedBlock())) {
                        event.setCancelled(true);
                        treasureChest.start(player);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSandProgress(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().isSimilar(RedactedGive.ArcheologistShovel(1))) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                for (TreasureChest treasureChest : treasureList) {
                    if (!treasureChest.hasStatus()) continue;
                    if (treasureChest.getBlockOrder().get(0).equals(event.getClickedBlock())) {
                        event.setCancelled(true);
                        treasureChest.progress(player);
                        break;
                    } else {
                        for (int i = 1; i < treasureChest.getBlockOrder().size(); i++) {
                            if (treasureChest.getBlockOrder().get(i).equals(event.getClickedBlock())) {
                                event.setCancelled(true);
                                treasureChest.sink(player);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLockedChestOpen(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            for (TreasureChest treasureChest : treasureList) {
                if (treasureChest.getLocation().getBlock().equals(event.getClickedBlock())) {
                    event.setCancelled(true);
                    player.sendMessage("The chest can't be opened just yet.. you need to dig it up first.");
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onUnlockedChestOpen(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            for (TreasureChest treasureChest : dugTreasureList) {
                if (treasureChest.getLocation().getBlock().equals(event.getClickedBlock())) {
                    event.setCancelled(true);
                    if (!treasureChest.hasLooted(player)) {
                        ItemStack item = null;
                        // Simple Loot Table
                        // Chances at 5 different items
                        // t-rex 3, divine glass 6, gold 4, ancient 3, why 1: total 17
                        double random = Math.random() * 17;
                        if (player == treasureChest.getDiscoverer()) {
                            if (random <= 3) {
                                item = RedactedGive.TRexTooth(1);
                            } else if (random > 3 && random <= 9) {
                                item = RedactedGive.DivineGlass((int) (Math.random() * 16));
                            } else if (random > 9 && random <= 13) {
                                item = new ItemStack(Material.GOLD_INGOT, 1);
                            } else if (random > 13 && random <= 16) {
                                item = RedactedGive.AncientArtifact(1);
                            } else if (random > 16) {
                                item = Utils.createGuiItem(Material.PAPER,
                                        ChatColor.GRAY + "this is stupid", ChatColor.DARK_GRAY + "this feature is so boring");
                            }
                        } else {
                            // Same thing but without ancient artifact
                            if (random <= 3) {
                                item = RedactedGive.TRexTooth(1);
                            } else if (random > 3 && random <= 9) {
                                item = RedactedGive.DivineGlass((int) (Math.random() * 16));
                            } else if (random > 9 && random <= 16) {
                                item = new ItemStack(Material.GOLD_INGOT, 1);
                            } else if (random > 16) {
                                item = Utils.createGuiItem(Material.PAPER,
                                        ChatColor.GRAY + "this is stupid", ChatColor.DARK_GRAY + "this feature is so boring");
                            }
                        }
                        Inventory inv = Bukkit.createInventory(null, 27, "Treasure Chest");
                        inv.setItem((int) (Math.random() * 27), item);
                        player.playSound(player.getLocation(), Sound.CHEST_OPEN, 1, 1);
                        player.openInventory(inv);
                        treasureChest.addLootedChest(player);
                        ContentListeners.onTreasureDiscover(player);
                        break;
                    } else {
                        player.sendMessage("You've already looted this chest!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTreasureSink(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (player.getItemInHand().getType() == Material.WOOD_SWORD ||
                    player.getItemInHand().getType() == Material.STONE_SWORD ||
                    player.getItemInHand().getType() == Material.IRON_SWORD ||
                    player.getItemInHand().getType() == Material.GOLD_SWORD ||
                    player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
                for (TreasureChest treasureChest : treasureList) {
                    if (treasureChest.getStatus() == ChestStatus.SINKING && treasureChest.getLocation().getBlock().equals(event.getClickedBlock())) {
                        if (!treasureChest.hasSword()) {
                            treasureChest.spawnSword(player.getItemInHand());
                        } else {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 10));
                            Utils.sendSandParticle(treasureChest.getLocation().add(0.5, 1, 0.5));
                            player.playSound(player.getLocation(), Sound.DIG_SAND, 1, 1);
                            treasureChest.addContributor(player);
                            treasureChest.increaseClicks();
                        }
                    }
                }
            }
        }
    }

    public TreasureChest newChestLocation() throws BlockNotFoundException {
        ArrayList<Block> list = null;
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
                    list = Utils.getBlocksAround(loc, 1);
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
        return new TreasureChest(plugin, finalLoc, list);
    }

    @EventHandler
    public void onGoldClaim(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals("Treasure Chest")) {
            if (e.getCurrentItem().getType() == Material.GOLD_INGOT && e.getClickedInventory().getTitle().equals("Treasure Chest")) {
                e.setCancelled(true);
                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "GOLD! " +
                        ChatColor.GRAY + "You obtained " + ChatColor.GOLD + "500g" + ChatColor.GRAY + "!");
                ContentListeners.getRedactedPlayer(player).addGold(500);
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                e.getClickedInventory().remove(Material.GOLD_INGOT);
            } else if (e.getClickedInventory().getHolder() == player) {
                if (e.getAction() == InventoryAction.PICKUP_ALL ||
                        e.getAction() == InventoryAction.PICKUP_SOME ||
                        e.getAction() == InventoryAction.PICKUP_ONE ||
                        e.getAction() == InventoryAction.PICKUP_HALF ||
                        e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY)
                e.setCancelled(true);
            }
        }
    }


    @Override
    public void run() {
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
        // ConcurrentModificationException was happening, so clone into an array ig
        TreasureChest[] cloneList = new TreasureChest[0];
        cloneList = treasureList.toArray(cloneList);
        for (TreasureChest treasureChest : cloneList) {
            if (treasureChest.getStatus() != ChestStatus.IN_PROGRESS) {
                Location location = treasureChest.getLocation().add(0.5, 1, 0.5);
                Utils.sendSandParticle(location.getX(), location.getY(), location.getZ());
            }
            if (treasureChest.getStatus() == ChestStatus.FINISHED) {
                treasureList.remove(treasureChest);
                dugTreasureList.add(treasureChest);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        dugTreasureList.remove(treasureChest);
                        treasureChest.getLocation().getBlock().setType(Material.AIR);
                    }
                }.runTaskLater(plugin, 300);
            } else if (treasureChest.getStatus() == ChestStatus.FINISHED_SANK) {
                treasureList.remove(treasureChest);
            }
        }
    }
}

package com.articreep.redactedpit.treasure;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.utils.UtilBoundingBox;
import com.articreep.redactedpit.utils.Utils;
import com.articreep.redactedpit.commands.RedactedGive;
import com.articreep.redactedpit.content.ContentListeners;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;

public class TreasureListeners extends BukkitRunnable implements Listener {
    private final Main plugin;
    public static UtilBoundingBox treasureBox = new UtilBoundingBox(102, 47, 1, -20, 40, -87);
    // At any given time there will only be two places to dig
    public static ArrayList<TreasureChest> treasureList = new ArrayList<>();
    public static ArrayList<TreasureChest> dugTreasureList = new ArrayList<>();
    public static HashSet<Player> cooldown = new HashSet<>();
    public TreasureListeners(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShovelClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (cooldown.contains(player)) return;
        if (!player.getItemInHand().hasItemMeta()) return;
        if (!player.getItemInHand().getItemMeta().hasDisplayName()) return;
        if (player.getItemInHand().getItemMeta().getDisplayName().equals(RedactedGive.ArcheologistShovel(1).getItemMeta().getDisplayName())) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (treasureList.size() == 0) {
                    player.sendMessage("Couldn't find a treasure.. try again?");
                    player.playSound(player.getLocation(), Sound.DIG_GRASS, 1, 0.5F);
                } else {
                    Location loc1 = treasureList.get(0).getLocation();
                    if (treasureList.size() == 1) {
                        pathParticles(player.getLocation(), loc1.add(0.5, 2, 0.5));
                    } else if (treasureList.size() == 2) {
                        Location loc2 = treasureList.get(1).getLocation();
                        if (loc1.distanceSquared(player.getLocation()) < loc2.distanceSquared(player.getLocation())) {
                            pathParticles(player.getLocation(), loc1.add(0.5, 2, 0.5));
                        } else {
                            pathParticles(player.getLocation(), loc2.add(0.5, 2, 0.5));
                        }
                    }
                    player.playSound(player.getLocation(), Sound.DIG_GRASS, 1, 1);
                }
                addcooldown(player);
            }
        }
    }

    @EventHandler
    public void onSandClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            for (TreasureChest treasureChest : treasureList) {
                if (treasureChest.getLocation().getBlock().equals(event.getClickedBlock())) {
                    if (cooldown.contains(player)) return;
                    event.setCancelled(true);
                    if (!player.getItemInHand().hasItemMeta()) return;
                    if (!player.getItemInHand().getItemMeta().hasDisplayName()) return;
                    if (player.getItemInHand().getItemMeta().getDisplayName().equals(RedactedGive.ArcheologistShovel(1).getItemMeta().getDisplayName())) {
                        treasureChest.start(player);
                    } else {
                        if (!treasureChest.hasStatus()) {
                            player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "TREASURE! " + ChatColor.GRAY +
                                    "You need an " + ChatColor.YELLOW + "Archeologist Shovel" + ChatColor.GRAY + "to uncover this mystery!");
                            player.sendMessage(ChatColor.YELLOW + "Maybe someone under a certain well can help you..");
                        }
                    }
                    addcooldown(player);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onSandProgress(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.getItemInHand().hasItemMeta()) return;
        if (!player.getItemInHand().getItemMeta().hasDisplayName()) return;
        if (player.getItemInHand().getItemMeta().getDisplayName().equals(RedactedGive.ArcheologistShovel(1).getItemMeta().getDisplayName())) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if (cooldown.contains(player)) {
                    event.setCancelled(true);
                    return;
                }
                for (TreasureChest treasureChest : treasureList) {
                    if (!treasureChest.hasStatus() || treasureChest.getBlockOrder().isEmpty()) continue;
                    if (treasureChest.getBlockOrder().get(0).equals(event.getClickedBlock())) {
                        event.setCancelled(true);
                        treasureChest.progress(player);
                        addcooldown(player);
                        break;
                    } else {
                        for (int i = 1; i < treasureChest.getBlockOrder().size(); i++) {
                            if (treasureChest.getBlockOrder().get(i).equals(event.getClickedBlock())) {
                                event.setCancelled(true);
                                treasureChest.sink(player);
                                addcooldown(player);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onLockedChestOpen(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            for (TreasureChest treasureChest : treasureList) {
                if (treasureChest.getLocation().getBlock().equals(event.getClickedBlock())) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "LOCKED! " +
                            ChatColor.GRAY + "You can't open the chest yet!");
                    addcooldown(player);
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
                                item = RedactedGive.DivineGlass((int) (Math.random() * 16) + 1);
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
                        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "GREEDY! " +
                                ChatColor.GRAY + "You've already looted this chest!");
                    }
                    addcooldown(player);
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

    public static void pathParticles(Location loc1, Location loc2) {
        double distance = loc1.distance(loc2);
        double inbetween = 0.5;
        Vector vec1 = loc1.toVector();
        Vector vec2 = loc2.toVector();
        Vector vector = vec2.clone().subtract(vec1).normalize().multiply(inbetween);
        int i = 0;
        for (double covered = 0; covered < distance; vec1.add(vector)) {
            Utils.sendDirtParticle(vec1.getX(), loc1.getY(), vec1.getZ());
            covered += inbetween;
            i++;
            if (i >= 10) {
                break;
            }
        }
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
                        e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY ||
                        e.getAction() == InventoryAction.SWAP_WITH_CURSOR)
                e.setCancelled(true);
            }
        }
    }

    public void addcooldown(Player player) {
        cooldown.add(player);
        new BukkitRunnable() {

            @Override
            public void run() {
                cooldown.remove(player);
            }
        }.runTaskLater(plugin, 10);
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

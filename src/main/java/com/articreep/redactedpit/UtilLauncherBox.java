package com.articreep.redactedpit;

import com.articreep.redactedpit.commands.ToggleLaunchers;
import com.articreep.redactedpit.listeners.LauncherListeners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


public class UtilLauncherBox extends UtilBoundingBox {
    private final HashMap<Vector, Integer> velocityMap;
    /**
     *
     * @param x1 x1
     * @param y1 y1
     * @param z1 z1
     * @param x2 x2
     * @param y2 y2
     * @param z2 z2
     * @param velocityMap Map containing velocity rates and specified y-value to eject at
     */
    public UtilLauncherBox(double x1, double y1, double z1, double x2, double y2, double z2, HashMap<Vector, Integer> velocityMap) {
        super(x1, y1, z1, x2, y2, z2);
        this.velocityMap = velocityMap;
    }

    public void launch(PlayerMoveEvent event, Main plugin) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        if (!ToggleLaunchers.toggled) {
            Utils.sendRedstoneParticle(plugin, loc.getX(), loc.getY(), loc.getZ());
            return;
        }
        // Choose a random launch angle
        Set<Vector> set = velocityMap.keySet();
        List<Vector> list = new ArrayList<>(set);
        Vector angle = Utils.getRandomElement(list);
        int y = velocityMap.get(angle);
        // Make the armor stand
        ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        // Enable noclip
        ((CraftEntity) armorStand).getHandle().noclip = true;
        armorStand.setPassenger(player);
        armorStand.setMarker(true);
        armorStand.setVisible(false);
        armorStand.setVelocity(angle);
        LauncherListeners.launchingList.put(player, armorStand);

        Utils.sendbeegExplosion(player.getLocation());
        player.playSound(player.getLocation(), Sound.EXPLODE, 1, 1);

        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                i++;
                if (player.getLocation().getY() <= y) {
                    armorStand.eject();
                    armorStand.remove();
                    player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 1, 1);
                    LauncherListeners.launchingList.remove(player);
                    this.cancel();
                }
                if (i >= 250) {
                    armorStand.eject();
                    armorStand.remove();
                    LauncherListeners.launchingList.remove(player);
                    Bukkit.getLogger().warning(player.getName() + " was on the launcher for more than 25 seconds and were ejected!");
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 20, 2);
    }



}

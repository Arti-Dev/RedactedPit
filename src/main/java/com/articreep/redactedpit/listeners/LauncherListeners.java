package com.articreep.redactedpit.listeners;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.UtilLauncherBox;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;
import org.graalvm.compiler.core.common.util.Util;

import java.util.ArrayList;
import java.util.HashMap;

public class LauncherListeners implements Listener {
    private Main plugin;
    public static HashMap<Player, ArmorStand> launchingList = new HashMap<>();

    public LauncherListeners(Main plugin) {
        this.plugin = plugin;
    }
    //TODO Configurable?
    public static UtilLauncherBox futureToColoLauncher = new UtilLauncherBox(-18, 55, 83, -25, 49, 76, new HashMap<Vector, Integer>() {{
        put(new Vector(-3, 1, -3), 47);
        put(new Vector(-3.3, 1, -1), 47);
        put(new Vector(-0.7, 1, -3.3), 47);
    }});

    public static UtilLauncherBox jurassicToColoLauncher = new UtilLauncherBox(-63, 54, 9, -54, 46, 15, new HashMap<Vector, Integer>() {{
        put(new Vector(0, 1, 3), 47);
        put(new Vector(1, 1, 2), 47);
        put(new Vector(-1, 1, 2), 47);
    }});

    public static UtilLauncherBox egyptLauncher = new UtilLauncherBox(25, 67, -16, 14, 80, -27, new HashMap<Vector, Integer>() {{
        put(new Vector(2, 0.3, -2), 49);
        put(new Vector(-1, 2, -4), 55);
        put(new Vector(5, 2, -1), 49);
    }});

    public static UtilLauncherBox jurassicLauncher = new UtilLauncherBox(-16, 76, -27, -26, 69, -17, new HashMap<Vector, Integer>() {{
        put(new Vector(-1.5, 0.3, -1.5), 51);
        put(new Vector(-3, 0.5, 0), 51);
    }});

    public static UtilLauncherBox coloLauncher = new UtilLauncherBox(-23, 69, 13, -14, 77, 21, new HashMap<Vector, Integer>() {{
        put(new Vector(-1.5, 2, 4), 47);
        put(new Vector(-3, 2, 3), 47);
        put(new Vector(-4, 2, 1.5), 47);
    }});

    public static UtilLauncherBox futureLauncher = new UtilLauncherBox(24, 78, 14, 15, 69, 24, new HashMap<Vector, Integer>() {{
        put(new Vector(4, 3, 4), 52);
        put(new Vector(1.5, 0.5, 0), 49);
        put(new Vector(0.5, 1, 2.5), 65);
    }});

    public ArrayList<UtilLauncherBox> boxArrayList = new ArrayList<UtilLauncherBox>() {{
        add(futureToColoLauncher);
        add(jurassicToColoLauncher);
        add(egyptLauncher);
        add(jurassicLauncher);
        add(coloLauncher);
        add(futureLauncher);
    }};

    // Trying out launcher tech
    @EventHandler
    public void onPlayerLauncher(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!launchingList.containsKey(player)) {
            for (UtilLauncherBox box : boxArrayList) {
                if (box.isInBox(player.getLocation())) {
                    box.launch(event, plugin);
                }
            }
        }
    }

    @EventHandler
    public void onEntityLeaveLauncher(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (launchingList.containsKey(player)) {
            // put em back on
            launchingList.get(player).setPassenger(player);
        }
    }
}

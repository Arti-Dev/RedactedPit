package com.articreep.redactedpit.listeners;

import java.util.ArrayList;
import java.util.HashMap;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.Utils;
import com.articreep.redactedpit.commands.RedactedGive;
import com.articreep.redactedpit.content.ContentListeners;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class RaceListeners implements Listener {
	Main plugin;
	public RaceListeners(Main plugin) {
		this.plugin = plugin;
	}
	public static HashMap<Player, Boolean> RaceFlying = new HashMap<>();
	public static HashMap<Player, Integer> RaceData = new HashMap<>();
	public static HashMap<Player, Long> RaceTimes = new HashMap<>();
	public static HashMap<Player, BukkitTask> ActionBarMap = new HashMap<>();
	public static HashMap<Player, ArrayList<BukkitTask>> ParticleMap = new HashMap<>();
	public static HashMap<Player, Boolean> RaceCooldown = new HashMap<>();
	@EventHandler
	public void onRaceStart(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		if (event.getAction() == Action.PHYSICAL) { //You have to trigger the pressure plate to start it
			if (loc.getX() < 60 && 56 < loc.getX() && loc.getY() == 47 && 70 < loc.getBlockZ() && loc.getBlockZ() < 77 
					&& player.getWorld().getName().equals("redacted2") && !RaceCooldown.containsKey(player)) {
				if (player.isFlying()) return;
				//Resets everything if you've already started
				if (RaceData.containsKey(player)) {
					resetData(player);
				}
				// Runs regardless whether you started already or not
				if (!RaceData.containsKey(player)) {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "FUTURE RACE " + ChatColor.WHITE + "You started the race! Use /cancelrace to quit.");
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 20, 1.414F);
					RaceData.put(player, 0);
					long startingtime = System.currentTimeMillis();
					RaceTimes.put(player, startingtime);
					//Start particle trail loop
					ArrayList<BukkitTask> trails = particletrails(player);
					ParticleMap.put(player, trails);
					RaceFlying.put(player, false);
					//Start actionbar loop
					BukkitTask runnable = new BukkitRunnable() {
						@Override
						public void run() {
							long currenttime = System.currentTimeMillis() - startingtime;
							Utils.sendActionBar(player, ChatColor.AQUA + "" + ChatColor.BOLD + "FUTURE RACE " + ChatColor.YELLOW + Utils.formattime(currenttime));
						}
					}.runTaskTimer(plugin, 0, 1);
					ActionBarMap.put(player, runnable);
					RaceCooldown.put(player, true);
					new BukkitRunnable() {
						@Override
						public void run() {
							RaceCooldown.remove(player);
						}
					}.runTaskLater(plugin, 20);
				}
			}
		}
	}
	
	@EventHandler
	public void onRaceCheckpoint(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location loc = event.getTo();
		if (player.getWorld().getName().equals("redacted2") && RaceData.containsKey(player)) {
			// If player is at checkpoint 1
			if (loc.getX() >= 24 && loc.getX() <= 26 && loc.getY() >= 51 && loc.getY() <= 53 && loc.getZ() >= 56 && loc.getZ() <= 58) {
				if (RaceData.get(player) == 0) { //Has the player started the race?
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "FUTURE RACE " + ChatColor.WHITE + "You reached Checkpoint 1!!");
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 20, 1.498F);
					RaceData.put(player, 1);
				}
			}
			// If player is at checkpoint 2
			if (loc.getX() >= 66 && loc.getX() <= 68 && loc.getY() >= 48 && loc.getY() <= 50 && loc.getZ() >= 40 && loc.getZ() <= 42) {
				if (RaceData.get(player) == 1) { //Has the player reached Checkpoint 1?
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "FUTURE RACE " + ChatColor.WHITE + "You reached Checkpoint 2!!");
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 20, 1.589F);
					RaceData.put(player, 2);
				}
			}
			// If player is at checkpoint 3
			if (loc.getX() >= 19 && loc.getX() <= 21 && loc.getY() >= 60 && loc.getY() <= 62 && loc.getZ() >= 40 && loc.getZ() <= 42) {
				if (RaceData.get(player) == 2) { //Has the player reached Checkpoint 2?
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "FUTURE RACE " + ChatColor.WHITE + "You reached Checkpoint 3!!");
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 20, 1.681F);
					RaceData.put(player, 3);
				}
			}
			// If player reached the end
			if (loc.getX() >= 63 && loc.getX() <= 65 && loc.getY() >= 47 && loc.getY() <= 49 && loc.getZ() >= 66 && loc.getZ() <= 68) {
				if (RaceData.get(player) == 3) { //Has the player reached Checkpoint 3?
					long millis = System.currentTimeMillis() - RaceTimes.get(player);
					// Send event data to ContentListeners
					ContentListeners.onFutureRaceComplete(event);
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "RACE CLEAR! " + ChatColor.WHITE + "You finished in " + ChatColor.YELLOW + Utils.formattime(millis));
					if (!RaceFlying.get(player) && millis < 31000) {
						player.sendMessage(ChatColor.GREEN + "You were awarded with an " + ChatColor.YELLOW + "Ancient Artifact " + ChatColor.GREEN + "for completing the race under 31 seconds without flying!");
						player.getInventory().addItem(RedactedGive.AncientArtifact(1));
					}
					Bukkit.getLogger().info(player.getName() + " completed the Future Race in " + Utils.formattime(millis));
					resetData(player);
					new BukkitRunnable() {
						int i = 0;
						@Override
						public void run() {
							player.playSound(player.getLocation(), Sound.NOTE_PLING, 20, 1.781F);
							if (i >= 4) {
								this.cancel();
							}
							i++;
						}
					}.runTaskTimer(plugin, 0, 2);
 				}
			}
		}
	}
	
	@EventHandler
	public void onToggleFly(PlayerToggleFlightEvent event) {
		if (event.isFlying() && RaceData.containsKey(event.getPlayer()) && !RaceFlying.get(event.getPlayer())) {
			RaceFlying.put(event.getPlayer(), true);
			Bukkit.getLogger().info(event.getPlayer().getName() + " started flying during the Future Race!");
		}
	}
	
	@EventHandler
	public void onPlayerDC(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (RaceData.containsKey(player)) {
			resetData(player);
			Bukkit.getLogger().info(event.getPlayer().getName() + " DCed and their Race Data was reset!");
		}
	}
	
	@EventHandler
	public void onPlayerDie(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (RaceData.containsKey(player)) {
			resetData(player);
			player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "FUTURE RACE " + ChatColor.WHITE + "You cannot complete the Race if you die!");
			Bukkit.getLogger().info(player.getName() + " DCed and their Race Data was reset!");
		}
	}
	
	
	public BukkitTask sendParticle(Player player, Double x, Double y, Double z) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, x.floatValue(), y.floatValue(), z.floatValue(), 0.1F, 0.1F, 0.1F, 1, 25);

		return new BukkitRunnable() {
			@Override
			public void run() {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			}
		}.runTaskTimer(plugin, 5, 5);
	}
	
	public ArrayList<BukkitTask> particletrails(Player player) {
		ArrayList<BukkitTask> taskarray = new ArrayList<>();
		BukkitTask task1 = sendParticle(player, 37.978, 49.501, 69.945);
		taskarray.add(task1);
		BukkitTask task2 = sendParticle(player, 25.838, 59.139, 65.782);
		taskarray.add(task2);
		BukkitTask task3 = sendParticle(player, 40.121, 53.103, 51.335);
		taskarray.add(task3);
		BukkitTask task4 = sendParticle(player, 62.935, 53.25, 46.725);
		taskarray.add(task4);
		BukkitTask task5 = sendParticle(player, 73.052, 53.656, 34.664);
		taskarray.add(task5);
		BukkitTask task6 = sendParticle(player, 85.141, 39.428, 31.014);
		taskarray.add(task6);
		BukkitTask task7 = sendParticle(player, 89.528, 38.688, 11.560);
		taskarray.add(task7);
		BukkitTask task8 = sendParticle(player, 49.457, 62.529, 22.662);
		taskarray.add(task8);
		BukkitTask task9 = sendParticle(player, 27.492, 62.529, 32.289);
		taskarray.add(task9);
		BukkitTask task10 = sendParticle(player, 41.288, 62.918, 53.234);
		taskarray.add(task10);
		BukkitTask task11 = sendParticle(player, 58.011, 58.067, 61.333);
		taskarray.add(task11);
		return taskarray;
		
	}
	
	public static void resetData(Player player) {
		RaceData.remove(player);
		ActionBarMap.get(player).cancel();
		Utils.sendActionBar(player, "");
		RaceTimes.remove(player);
		ArrayList<BukkitTask> particlelist = ParticleMap.get(player);
		for (BukkitTask bukkitTask : particlelist) {
			bukkitTask.cancel();
		}
		RaceFlying.remove(player);
	}
}

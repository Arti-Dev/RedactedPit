package com.articreep.redactedpit.listeners;

import java.util.HashMap;
import java.util.HashSet;

import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.PlayerTouchVoidEvent;
import com.articreep.redactedpit.Utils;
import com.articreep.redactedpit.colosseum.ColosseumPlayer;
import com.articreep.redactedpit.colosseum.ColosseumRunnable;
import com.articreep.redactedpit.commands.RedactedGive;
import com.articreep.redactedpit.commands.ToggleJumpPads;
import com.articreep.redactedpit.content.ContentListeners;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Listeners implements Listener {
	Main plugin;
	public Listeners(Main plugin) {
		this.plugin = plugin;
	}
	
	// For Jumppads
	@EventHandler
	public static void onJumpPadStep(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		if (event.getAction() == Action.PHYSICAL && player.isSneaking()) { //Is the player sneaking?
			Block block = loc.getBlock();
			if (!ToggleJumpPads.toggled) {
				player.sendMessage("Jumppads are off!"); //If pads are currently disabled immediately return
				return;
			}
			if (block.getType() == Material.GOLD_PLATE) { //If big jumppad
				player.setVelocity(new Vector(0, 1.5, 0));
				player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.5F, 1); 
			}
			if (block.getType() == Material.IRON_PLATE) { //If small jumppad
				player.setVelocity(new Vector(0, 1.1, 0));
				player.playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 0.5F, 1);
			}
		}
	}
	
	//When players die in the redacted world they are immediately respawned and teleported
	//TODO Maybe not hardcode the respawn location?
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = (Player) event.getEntity();
		Location location = new Location(Bukkit.getWorld("redacted2"), -1, 71, 9, 180f, 0.784f);
		if (player.getWorld().getName().equals("redacted2")) {
			new BukkitRunnable() {
				@Override
				public void run() {
					player.spigot().respawn();
					player.teleport(location);
					ColosseumRunnable.getColosseumPlayer(player, true).clearStreakCount();
					ColosseumRunnable.getColosseumPlayer(player, true).increaseDeathStreakCount(1);
				}
			}.runTaskLater(plugin, 1);
		}
	}
	
	public static HashMap<Player, Player> CombatTags = new HashMap<Player, Player>();
	public static HashMap<Player, Player> KnockbackTags = new HashMap<Player, Player>();
	@EventHandler //TODO Clean up
	public void onPlayerKillPlayer(EntityDamageByEntityEvent event) { //Grants players golden apples on killing other players
		if (event.isCancelled()) return;
		if (!(event.getEntity() instanceof Player)) return;
		// Since the victim is a player, we can cast it to a Player object
		Player victim = (Player) event.getEntity();
		// Initialize damager object
		Player damager = null;
		Location loc = null;
		boolean onKOTH = false;
		boolean inColo = false;
		ColosseumPlayer victimcolo = null;
		ColosseumPlayer damagercolo = null;
		int quantity = 1;
		// Find out who the damager is
		
		// Melee hit?
		if (event.getDamager() instanceof Player) {
			damager = (Player) event.getDamager();
		// Ranged hit?
		} else if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
			damager = (Player) ((Projectile) event.getDamager()).getShooter();
		// None of those? Don't care, then
		} else {
			return;
		}
		loc = damager.getLocation();
		// Is the KOTH on/Is the player on the KOTH?
		if (stoneplaced == true) {
			if ((9 <= loc.getX() && loc.getX() <= 21 
					&& 51 <= loc.getY() && loc.getY() <= 58 
					&& -75 <= loc.getZ() && loc.getZ() <= -63 )) {
				onKOTH = true;
				quantity = 2;
			}
		}
		// Is the player in the Colosseum?
		victimcolo = ColosseumRunnable.getColosseumPlayer(victim, false);
		damagercolo = ColosseumRunnable.getColosseumPlayer(damager, false);
		if (Utils.isInColosseum(damager.getLocation())) {
			inColo = true;
			// Add damage to ColosseumPlayers, and not NPCs. If the ColosseumPlayer doesn't already exist it's an NPC.
			if (!(victimcolo == null)) {
				victimcolo.increaseHitsTaken(1);
				victimcolo.increaseDamageTaken(event.getFinalDamage());
			}
			if (!(damagercolo == null)) {
				damagercolo.increaseDamageDealt(event.getFinalDamage());
				damagercolo.increaseHitsDealt(1);
			}
		}
		// Did the player smack them with a hot potato?
		if (damager.getItemInHand().hasItemMeta() == true && damager.getItemInHand().getItemMeta().hasDisplayName()) {
			if (damager.getItemInHand().getItemMeta().getDisplayName().equals(RedactedGive.HotPotato(1).getItemMeta().getDisplayName())) {
				// Only real players can pass the potato
				if (!(victimcolo == null || damagercolo == null)) {
					damager.getInventory().removeItem(RedactedGive.HotPotato(1));
					victim.getWorld().playSound(event.getEntity().getLocation(), Sound.FIZZ, 1, 1);
					Utils.sendGenericParticleOnHead(plugin, victim, EnumParticle.VILLAGER_ANGRY);
					victim.getInventory().addItem(RedactedGive.HotPotato(1));
					if (ColosseumRunnable.isPotatoMode()) {
						ColosseumRunnable.setPotatoVictim(victim);
					}
				}
			}
		}
		// Does the player have an increased punch buff?
		if (!(damagercolo == null)) {
			if (damagercolo.getlargeKB()) {
				// This sets the player's velocity after the initial vanilla hittick - so the player gets two velocity events
				Vector vec = damager.getLocation().getDirection().normalize().multiply(4).setY(1);
				new BukkitRunnable() {

					@Override
					public void run() {
						victim.setVelocity(vec);
						
					}
					
				}.runTaskLater(plugin, 1);
				new BukkitRunnable() {
					int i = 0;
					@Override
					public void run() {
						Utils.sendGenericParticle(plugin, victim.getLocation(), EnumParticle.SNOW_SHOVEL);
						i++;
						if (i >= 20) {
							this.cancel();
						}
					}
					
				}.runTaskTimer(plugin, 1, 1);
				//TODO Add particle trail
				damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 9));
				damager.getWorld().playSound(damager.getLocation(), Sound.ENDERDRAGON_HIT, 1, 0.5F);
				damagercolo.setLargeKB(false);
			}
		}
		
		// Does the victim currently have a 25% blocking buff?
		if (victimcolo != null && victimcolo.getBlockBuff() == true) {
			if (victim.isBlocking()) {
				// Uhh.. is this a good way of doing this?
				event.setDamage(event.getDamage() * 0.75);
			}
		}
		// Was it actually a kill?
		if (victim.getHealth() <= event.getFinalDamage()) {
			damager.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, quantity));
			if (quantity == 2 && onKOTH == true) {
				damager.sendMessage(ChatColor.GREEN + "You gained two golden apples because of the Sun KOTH!");
			} else if (inColo) {
				// Increase kill and death counts
				if (!(victimcolo == null)) victimcolo.increaseDeathCount(1);
				if (!(damagercolo == null)) { 
					damagercolo.increaseKillCount(1);
					damagercolo.increaseKillStreakCount(1);
				}
			}
		}
	}
	
	
	
	// Time Warp Pearls
	public static HashMap<Player, Boolean> hasPearlOut = new HashMap<Player, Boolean>();
	public static HashMap<Player, Location> pearllocations = new HashMap<Player, Location>();
	@EventHandler
	public void onPearlThrow(ProjectileLaunchEvent event) {
		if (event.getEntity().getShooter() instanceof Player && event.getEntity().getType() == EntityType.ENDER_PEARL) {
			Player player = (Player) event.getEntity().getShooter(); //get player
			ItemStack item = new ItemStack(player.getItemInHand());
			item.setAmount(1);
			ItemMeta itemmeta = item.getItemMeta(); //get item meta of the pearl they shot
			if (hasPearlOut.containsKey(player)) { //check if player already exists in hashmap
				event.setCancelled(true); //setcancelled actually consumes the item but doesn't shoot it
				player.sendMessage(ChatColor.RED + "You already have a pearl out!");
				player.getInventory().addItem(item); //reimburse
				return;
			}
			// Player doesn't already exist in hashmap?
			if (itemmeta.hasDisplayName() == false) { //if it's a regular pearl, don't continue
				return;
			}
			if (itemmeta.getDisplayName().equals(RedactedGive.TimeWarpPearl(1).getItemMeta().getDisplayName())) { //If it's a time warp pearl
					pearllocations.put(player, player.getLocation());
					hasPearlOut.put(player, true);
					new BukkitRunnable() {
						@Override
						public void run() {
							if (event.getEntity().getLocation().getY() < 0) {
								pearllocations.remove(player); //if the pearl fell in the void
								hasPearlOut.remove(player);
								this.cancel();
								return;
							}
							if (event.getEntity().isValid() == false) { //if the pearl no longer exists
								this.cancel();
								return;
							}
							pearllocations.put(player, player.getLocation()); //if it still exists and isn't in the void
						}
					}.runTaskTimer(plugin, 0, 5); //doesn't have to run that often
			}
			
		}
	}
	
	// Handles when the pearl lands and removes players from HashMaps
	@EventHandler
	public void onPearlLand(ProjectileHitEvent event) { 
		if (event.getEntity().getShooter() instanceof Player && event.getEntity().getType() == EntityType.ENDER_PEARL) {
			Player player = (Player) event.getEntity().getShooter(); //get player
			if (hasPearlOut.containsKey(player) == false) return; //if player is not in the hashmap do not continue
			Location loc = pearllocations.get(player);
			pearllocations.remove(player);
			player.sendMessage(ChatColor.RED + "You'll be teleported back in 3 seconds!");
			new BukkitRunnable() {
				@Override
				public void run() {
					player.teleport(loc);
					player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5F, 1);
					hasPearlOut.remove(player);
				}
			}.runTaskLater(plugin, 60);
		}
	}
	// Steve's Forward Air replica
	// This map stores individual player cooldowns
	public static HashMap<Player, Boolean> spikecooldown = new HashMap<Player, Boolean>();
	@EventHandler
	public void onPickaxeHit(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			Entity victim = event.getEntity();
			if (player.getItemInHand().getType().equals(Material.DIAMOND_PICKAXE) && player.getLocation().add(0, -0.2, 0).getBlock().getType() == Material.AIR) {
				if (spikecooldown.containsKey(player)) {
					player.sendMessage(ChatColor.RED + "You can only spike every second!");
					return;
				}
				spikecooldown.put(player, true);
				// Freezes the player in place for two ticks
				new BukkitRunnable() {
					int i = 0;
					@Override
					public void run() {
						i++;
						victim.setVelocity(new Vector(0, 0, 0));
						if (i == 2) {
							victim.getWorld().playSound(victim.getLocation(), Sound.ANVIL_LAND, 3.0F, 1);
							this.cancel();
						}
					}
				}.runTaskTimer(plugin, 0, 1);
				// Sends the player downwards
				new BukkitRunnable() {
					@Override
					public void run() {
						victim.setVelocity(new Vector(0, -1.3, 0));
					}
				}.runTaskLater(plugin, 3);
				// Check if player hits the ground
				new BukkitRunnable() { 
					int i = 0;
					int multiple = 0;
					boolean played = false;
					@Override
					public void run() {
						i++;
						if (i == 20) {
							this.cancel();
						}
						// If so, bounce them up based on how long they were spiked for
						if (victim.getLocation().add(0,-1,0).getBlock().getType() != Material.AIR) {
							multiple++;
							double velocity = 0.8 - i*0.03;
							victim.setVelocity(new Vector(0, velocity, 0));
							if (played == false) {
								victim.getWorld().playSound(victim.getLocation(), Sound.ZOMBIE_WOODBREAK, 0.7F, 1);
								played = true;
							}
							if (multiple == 1) {
								if (player.getLocation().add(0, -1, 0).getBlock().getType() != Material.AIR) {
									this.cancel();
								}
							}
							if (multiple == 2) {
								this.cancel();
							}
						}
					}
				}.runTaskTimer(plugin, 5, 1);
				//Remove player from the cooldown HashMap afterwards
				new BukkitRunnable() {
					@Override
					public void run() {
						spikecooldown.remove(player);
					}
				}.runTaskLater(plugin, 20);
			}
		}
	}
	// When players touch y = -10 players instantly die
	// This also has handling for Void Charms
	public static HashSet<Player> VoidCharmCooldown = new HashSet<Player>();
	@EventHandler
	public void onPlayerTouchVoid(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		PlayerTouchVoidEvent playertouchvoidevent = null;
		// If the player is on cooldown return
		if (VoidCharmCooldown.contains(player)) return;
		if (player.getLocation().getY() <= -10 && player.getWorld().getName().equals("redacted2")) {
			playertouchvoidevent = new PlayerTouchVoidEvent(player); // Initialize your Event
			Bukkit.getPluginManager().callEvent(playertouchvoidevent);
		} else {
			return;
		}
		if (!(playertouchvoidevent.isCancelled())) {
			player.setHealth(0);
			player.sendMessage(ChatColor.RED + "You fell in the void!");
			System.gc();
		}
	}
	
	@EventHandler
	// Given: The player is in the void, and they're not on cooldown. Do we kill them or not?
	public void onPlayerVoidCharm(PlayerTouchVoidEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		Inventory inventory = player.getInventory();
		// First check if they are in Creative - Creative users may bypass
		if (player.getGameMode().equals(GameMode.CREATIVE)) {
			event.setCancelled(true);
			return;
		}
		if (inventory.containsAtLeast(RedactedGive.VoidCharm(1),1)) {
			event.setCancelled(true);
			world.strikeLightningEffect(player.getLocation());
			player.damage(6);
			if (player.getHealth() == 0) {
				player.sendMessage(ChatColor.DARK_PURPLE + "Your Void Charm couldn't save you!");
				return;
			}
			player.setVelocity(new Vector(0, 4, 0));
			player.sendMessage(ChatColor.DARK_PURPLE + "Your Void Charm saved you from dying to the void!");
			inventory.removeItem(RedactedGive.VoidCharm(1));
			VoidCharmCooldown.add(player);
			new BukkitRunnable() {
				@Override
				public void run() {
					VoidCharmCooldown.remove(player);
				}
			}.runTaskLater(plugin, 20);
		}
	}

	// Make sure players can't place Void Charms
	@EventHandler
	public void onPlaceVoidCharm(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		ItemMeta itemmeta = player.getItemInHand().getItemMeta();
		if (itemmeta.hasDisplayName() == false) return; //if it's a regular item, don't continue
		if (itemmeta.getDisplayName().equals(RedactedGive.VoidCharm(1).getItemMeta().getDisplayName())) {
			event.setCancelled(true);
			player.sendMessage(ChatColor.DARK_PURPLE + "You may not place the Void Charm!");
		}
	}
	
	// Divine Glass
	// This map holds the locations of glass and what time they were placed
	public static HashMap<Location, Long> glassLocations = new HashMap<Location, Long>();
	@EventHandler
	public void onPlaceDivineGlass(BlockPlaceEvent event) {
		ItemMeta itemmeta = event.getPlayer().getItemInHand().getItemMeta(); //get item meta in hand
		Location location = event.getBlockPlaced().getLocation();
		long time = System.currentTimeMillis();
		if (itemmeta.hasDisplayName() == false) return; //if it's a regular item, don't continue
		if (itemmeta.getDisplayName().equals(RedactedGive.DivineGlass(1).getItemMeta().getDisplayName())) { 
			glassLocations.put(location, time);
			ContentListeners.onDivineGlassPlace(event);
			new BukkitRunnable() {
				//After around 10 seconds check if the ORIGINAL block is still there.
				//If the block was broken and replaced, it will not remove the block in this Runnable instance.
				@Override
				public void run() {
					if (glassLocations.containsKey(location)) { //check if the block is there
						if (glassLocations.get(location).equals(time)) {
							Block block = location.getBlock();
							block.breakNaturally();
							block.getWorld().playSound(location, Sound.GLASS, 0.5F, 1);
						}
					}
				}
			}.runTaskLater(plugin, 200);
		}
	}
	
	@EventHandler
	public void onBreakDivineGlass(BlockBreakEvent event) {
		Location location = event.getBlock().getLocation();
		//If it was a Divine Glass prevent them from being auto-removed
		if (glassLocations.containsKey(location)) glassLocations.remove(location);
	}
	
	// Sun Stones!
	// Init stonelocation variable
	public static Location stoneLocation = null;
	// Init whether stone is placed or not
	public static boolean stoneplaced = false;
	@EventHandler
	public void onPlaceSunStone(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		ItemMeta itemmeta = player.getItemInHand().getItemMeta();
		stoneLocation = new Location(Bukkit.getServer().getWorld("redacted2"), 15, 50, -58);
		Location loc = event.getBlock().getLocation();
		Location target = new Location(Bukkit.getServer().getWorld("redacted2"), 15.5, 60, -68.5);
		// Fix stonelocation if world is null, does happen sometimes
		if (stoneLocation.getWorld() == null) {
			stoneLocation.setWorld(Bukkit.getServer().getWorld("redacted2"));
		}
		if (itemmeta.hasDisplayName() == false) return; //if it's a regular item, don't continue
		if (itemmeta.getDisplayName().equals(RedactedGive.SunStone(1).getItemMeta().getDisplayName())) {
			if (loc.equals(stoneLocation)) {
				player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "SUN STONE! " + ChatColor.DARK_AQUA + "Your Sun Stone will last 4 minutes!");
				// Pass event data to ContentListeners
				ContentListeners.onSunStonePlace(event);
				Utils.sendbeegExplosion(15.5F, 60F, -68.5F);
				Utils.sendExplosion(15.5F, 50.5F, -57.5F);
				for (Player online: Bukkit.getOnlinePlayers()) {
					online.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "SUN STONE! " + ChatColor.AQUA + "" + ChatColor.BOLD + 
							"KOTH " + ChatColor.GRAY + "for 4 minutes at " + ChatColor.YELLOW + "Sun Pyramid");
					new BukkitRunnable() {
						int i = 0;
						@Override
						public void run() {
							if (i == 0) {
								online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 1.189f);
							} else if (i == 1) {
								online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 1.334f);
							} else if (i == 2) {
								online.playSound(online.getLocation(), Sound.NOTE_PLING, 1, 1.498f);
								this.cancel();
							}
							i++;
						}
					}.runTaskTimer(plugin, 0, 5);
				}
				world.playSound(stoneLocation, Sound.EXPLODE, 1, 1);
				stoneplaced = true;
				Utils.connectPoints(plugin, stoneLocation.clone().add(0.5, 0.5, 0.5), target, EnumParticle.REDSTONE);
				// particle
				BukkitTask kothparticles = sendKOTHParticles(15, 54, -69);
				// Phase 1
				new BukkitRunnable() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						stoneLocation.getBlock().setType(Material.WOOL);
						stoneLocation.getBlock().setData((byte) 4);
						world.playSound(stoneLocation, Sound.FIZZ, 1, 1);
						Utils.sendSmokeParticle(15.5F, 50.5F, -57.5F);
					}
				}.runTaskLater(plugin, 1200);
				new BukkitRunnable() {
					@Override
					public void run() {
						stoneLocation.getBlock().setType(Material.GOLD_ORE);
						world.playSound(stoneLocation, Sound.FIZZ, 1, 1);
						Utils.sendSmokeParticle(15.5F, 50.5F, -57.5F);
					}
				}.runTaskLater(plugin, 2400);
				new BukkitRunnable() {
					@Override
					public void run() {
						stoneLocation.getBlock().setType(Material.COBBLESTONE);
						world.playSound(stoneLocation, Sound.FIZZ, 1, 1);
						Utils.sendSmokeParticle(15.5F, 50.5F, -57.5F);
					}
				}.runTaskLater(plugin, 3600);
				new BukkitRunnable() {
					@Override
					public void run() {
						stoneLocation.getBlock().breakNaturally(new ItemStack(Material.DIAMOND_AXE)); //why not?
						stoneplaced = false;
						world.playSound(stoneLocation, Sound.FIZZ, 1, 0.5f);
						Utils.sendSmokeParticle(15.5F, 50.5F, -57.5F);
						kothparticles.cancel();
					}
				}.runTaskLater(plugin, 4800);
				return;
				//End of runnables
				
			} else if (!(event.getBlock().getLocation().equals(stoneLocation))) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "Place the Sun Stone on top of the redstone block at the " + ChatColor.YELLOW + "Sun Pyramid!");
				return;
			}
		}
	}
	
	@EventHandler
	public void onBreakSunStone(BlockBreakEvent event) {
		stoneLocation = new Location(Bukkit.getServer().getWorld("redacted2"), 15, 50, -58);
		Player player = event.getPlayer();
		if (event.getBlock().getLocation().equals(stoneLocation)) {
			if (stoneplaced) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.YELLOW + "You may not break the Sun Stone!");
			} else if (!stoneplaced) {
				player.sendMessage(ChatColor.YELLOW + "Hmm, I don't know how this got here. Guess you can break it?");
			}
		}
	}
	
	// Sends packets giving the KOTH its signature particles
	public BukkitTask sendKOTHParticles(int x, int y, int z) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_HAPPY, true, (float) x, (float) y, (float) z, 4, 4, 4, 0, 25);
		BukkitTask task = new BukkitRunnable() {
			@Override
			public void run() {
				for(Player online : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
                }
			}
		}.runTaskTimer(plugin, 5, 5);
		return task;
	}
	
	// plays a little sound :)
	public void playKOTHAlert(Player player) {
		new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				if (i == 0) {
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1.189f);
				} else if (i == 1) {
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1.334f);
				} else if (i == 2) {
					player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1.498f);
					this.cancel();
				}
				i++;
			}
		}.runTaskTimer(plugin, 0, 5);
	}
	
	
}

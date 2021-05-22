package com.articreep.redactedpit.colosseum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.articreep.redactedpit.commands.Debug;
import com.articreep.redactedpit.commands.RedactedGive;
import com.articreep.redactedpit.Main;
import com.articreep.redactedpit.Utils;
import com.articreep.redactedpit.content.ContentListeners;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class ColosseumRunnable extends BukkitRunnable implements Listener {
	Main plugin;
	public ColosseumRunnable(Main plugin) {
		this.plugin = plugin;
	}
	
	public static HashMap<Player, ColosseumPlayer> ColosseumMap = new HashMap<Player, ColosseumPlayer>();
	private int selectionCooldown = 10;
	private int hotPotatoCountdown = 10;
	private static Player hotPotatoVictim = null; 
	private static boolean hotPotatoMode = false;

	@Override
	public void run() {
		if (Debug.debug) Bukkit.getLogger().info("[DEBUG] There are " + ColosseumMap.size() + " players in the ColosseumHashMap!");
		// Check every ONLINE player whether they're inside or not, and update their stats - this excludes NPCs
		for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
			
			ColosseumPlayer coloplayer = getColosseumPlayer(onlineplayer, true);
			
			if (Utils.isInColosseum(coloplayer.getPlayer().getLocation())) {
				if (Debug.debug) Bukkit.getLogger().info("[DEBUG] " + coloplayer.getPlayer().getName() + " is in the colo!");
				coloplayer.increaseSecondsInside(1);
				coloplayer.increaseInactivity(1);
				// Decrease opinion time
				if (coloplayer.getOpinion() != AudienceOpinion.NEUTRAL) {
					coloplayer.increaseOpinionLength(-1);
					if (coloplayer.getOpinionLength() <= 0) {
						coloplayer.setCrowdOpinion(AudienceOpinion.NEUTRAL);
					}
				}
				coloplayer.clearSecondsOutside();
				
			} else {
				
				// We shouldn't track at all if they've never entered
				if (coloplayer.secondsInside > 0) {
					coloplayer.increaseSecondsOutside(1);
				}
				
				// The audience will forget you if you leave for too long
				if (coloplayer.secondsOutside >= coloplayer.crowdMemory) {
					coloplayer.clearValues();
				}
			}
		}
		
		// Every player should be updated by now in terms of time
		// This number is going to be influenced by the players
		int audienceChance = 0;
		HashMap<ColosseumPlayer, AudienceReason> audienceReasons = new HashMap<ColosseumPlayer, AudienceReason>();
		ArrayList<ColosseumPlayer> audiencePlayers = new ArrayList<ColosseumPlayer>();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			ColosseumPlayer coloplayer = getColosseumPlayer(player, true);
			if (Utils.isInColosseum(coloplayer.getPlayer().getLocation())) {
				// Make an ArrayList of possible criteria the player meets
				ArrayList<AudienceReason> personalReasons = new ArrayList<AudienceReason>();
				// Run a bunch of checks on the players
				boolean lowHealth = false;
				AudienceOpinion personalOpinion = coloplayer.getOpinion();
				if (coloplayer.getHealth() <= 7) {
					// This reason will not be chosen if the audience has a bad opinion
					if (personalOpinion == AudienceOpinion.NEUTRAL || personalOpinion == AudienceOpinion.POSITIVE) {
						audienceChance += 13;
						// Weight of 7
						for (int i = 0; i < 7; i++) {
							personalReasons.add(AudienceReason.LOW_HEALTH);
						}
					}
					lowHealth = true;
				}
				
				if (coloplayer.secondsInside >= 60) {
					// Weight of 2, is overrided by LOW_HEALTH
					if (lowHealth == false) {
						audienceChance += 8;
						for (int i = 0; i < 2; i++) {
							personalReasons.add(AudienceReason.VETERAN);
						}
					}
				}
				
				if (coloplayer.killstreak >= 3) {
					// Weight of 3 per reason here
					audienceChance += 12;
					for (int i = 0; i < 3; i++) {
						if (personalOpinion == AudienceOpinion.POSITIVE || personalOpinion == AudienceOpinion.NEUTRAL) {
							personalReasons.add(AudienceReason.HIGH_STREAK_POSITIVE);
						}
						if (personalOpinion == AudienceOpinion.NEGATIVE || personalOpinion == AudienceOpinion.NEUTRAL) {
						personalReasons.add(AudienceReason.HIGH_STREAK_NEGATIVE);
						}
					}
				}
				
				if (coloplayer.getInactivity() >= 30) {
					// Weight of 4
					audienceChance += 5;
					for (int i = 0; i < 4; i++) {
						personalReasons.add(AudienceReason.INACTIVE);
					}
				}
				
				if (coloplayer.getDeathStreak() >= 3) {
					// This reason will not be chosen if the audience has a bad opinion
					if (personalOpinion == AudienceOpinion.POSITIVE || personalOpinion == AudienceOpinion.NEUTRAL) {
						// Weight of 6
						audienceChance += 8;
						for (int i = 0; i < 6; i++) {
							personalReasons.add(AudienceReason.STRUGGLING_DEATHS);
						}
					}
				}
				
				// Determine which reason will be prioritized
				// If the player did not meet any criteria they don't matter
				if (personalReasons.size() > 0) {
					audienceReasons.put(coloplayer, Utils.getRandomElement(personalReasons));
					audiencePlayers.add(coloplayer);
				}
				
			}
		}
		
		// All checks have gone through
		// Will the audience do something?
		if (Debug.debug) Bukkit.getLogger().info("[DEBUG] There are " + audiencePlayers.size() + " players that can be acted on!");
		if (selectionCooldown > 0) audienceChance = 0;
		if (Debug.debug) Bukkit.getLogger().info("[DEBUG] Audience Chance: " + audienceChance);
		if (Math.random() < audienceChance / 100.0) {
			// Choose a player at random
			ColosseumPlayer chosen = Utils.getRandomElement(audiencePlayers);
			sendPlayerAudienceEffect(new Location(chosen.getPlayer().getWorld(), -11, 61, 40), chosen.getPlayer(), audienceReasons.get(chosen), chosen.getOpinion(), 0.7);
			selectionCooldown = 10;
		} else {
			selectionCooldown -= 1;
		}
		
		// Hot Potato Countdown
		if (hotPotatoMode) {
			if (hotPotatoCountdown <= 0) {
				hotPotatoVictim.damage(3);
				hotPotatoVictim.setFireTicks(200);
				Bukkit.broadcastMessage(ChatColor.RED + hotPotatoVictim.getName() + " was set on fire by a Hot Potato!");
				hotPotatoVictim.getInventory().remove(RedactedGive.HotPotato(1));
				hotPotatoMode = false;
				hotPotatoVictim = null;
			} else if (hotPotatoCountdown > 0) {
				Bukkit.broadcastMessage(ChatColor.RED + "The Hot Potato will burn in " + Integer.toString(hotPotatoCountdown) + " seconds");
				hotPotatoCountdown -= 1;
			}
			
			
		}
		
	}
	
	public static ColosseumPlayer getColosseumPlayer(Player player, boolean createnew) {
		ColosseumPlayer coloplayer = null;
		if (ColosseumMap.containsKey(player) == false) {
			if (createnew == false) return null;
			coloplayer = new ColosseumPlayer(player);
			ColosseumMap.put(player, coloplayer);
			if (Debug.debug) Bukkit.getLogger().info("[DEBUG] Added " + coloplayer.getPlayer().getName() + " to the ColoHashMap!");
			return coloplayer;
		}
		return ColosseumMap.get(player);
	}
	
	// If you disconnect the player is immediately forgotten and class references are removed
	@EventHandler
	public void handleOfflineColosseumPlayer(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (ColosseumMap.containsKey(player)) {
			ColosseumMap.remove(player);
		}
		// I'll handle Hot Potato stuff here too
		if (hotPotatoVictim == player) {
			hotPotatoVictim.getInventory().remove(RedactedGive.HotPotato(1));
			hotPotatoVictim = null;
		}
	}
	
	public static void setPotatoVictim(Player player) {
		hotPotatoVictim = player;
	}
	
	public static boolean isPotatoMode() {
		return hotPotatoMode;
	}
	/**
	 * 
	 * @param startingLoc Starting location of the smoke trail
	 * @param player ColosseumPlayer
	 * @param reason AudienceReason
	 * @param speed Speed of x blocks per two ticks
	 */
	public void sendPlayerAudienceEffect(Location startingLoc, Player player, AudienceReason reason, AudienceOpinion opinion, double speed) {
		ColosseumPlayer coloplayer = getColosseumPlayer(player, true);
		String audienceMessage = null;
		// Determine a effect to give the player based on the reason
		// The audience's opinion will formulate for the first time if it is currently neutral
		if (opinion == AudienceOpinion.NEUTRAL) {
			// The opinion will form now, so might as well give them a random time
			coloplayer.setOpinionLength(ThreadLocalRandom.current().nextInt(15,40));
		}
		ArrayList<AudienceEffect> possibleEffects = new ArrayList<AudienceEffect>();
		if (reason == AudienceReason.LOW_HEALTH) {
			possibleEffects.add(AudienceEffect.ABSORPTION);
			possibleEffects.add(AudienceEffect.INSTANT_HEAL);
			possibleEffects.add(AudienceEffect.REGENERATION);
			possibleEffects.add(AudienceEffect.LARGEKB);
			possibleEffects.add(AudienceEffect.BLOCKBUFF);
			possibleEffects.add(AudienceEffect.BLASTED_AWAY);
			coloplayer.setCrowdOpinion(opinion);
			audienceMessage = ChatColor.GRAY + "" + ChatColor.ITALIC + "The audience notices that " + player.getName() + " is on low health..";
		} else if (reason == AudienceReason.VETERAN) {
			if (opinion == AudienceOpinion.POSITIVE || opinion == AudienceOpinion.NEUTRAL) {
				possibleEffects.add(AudienceEffect.ABSORPTION);
				possibleEffects.add(AudienceEffect.INCREASED_GOLD);
				possibleEffects.add(AudienceEffect.INCREASED_XP);
				possibleEffects.add(AudienceEffect.STRENGTH);
				possibleEffects.add(AudienceEffect.INSTANT_HEAL);
				possibleEffects.add(AudienceEffect.REGENERATION);
				possibleEffects.add(AudienceEffect.LARGEKB);
				possibleEffects.add(AudienceEffect.BLOCKBUFF);
				coloplayer.setCrowdOpinion(AudienceOpinion.POSITIVE);
				audienceMessage = ChatColor.GRAY + "" + ChatColor.ITALIC + "The audience wants " + player.getName() + " to continue fighting!";
			} else if (opinion == AudienceOpinion.NEGATIVE || opinion == AudienceOpinion.NEUTRAL) {
				possibleEffects.add(AudienceEffect.FIRE);
				possibleEffects.add(AudienceEffect.EXPLOSION);
				possibleEffects.add(AudienceEffect.SMITE);
				coloplayer.setCrowdOpinion(AudienceOpinion.NEGATIVE);
				audienceMessage = ChatColor.GRAY + "" + ChatColor.ITALIC + "The audience is a bit sick of " + player.getName() + "..";
			}
		} else if (reason == AudienceReason.HIGH_STREAK_POSITIVE) {
			possibleEffects.add(AudienceEffect.INCREASED_GOLD);
			possibleEffects.add(AudienceEffect.INCREASED_XP);
			possibleEffects.add(AudienceEffect.REGENERATION);
			possibleEffects.add(AudienceEffect.ABSORPTION);
			coloplayer.setCrowdOpinion(AudienceOpinion.POSITIVE);
			audienceMessage = ChatColor.GRAY + "" + ChatColor.ITALIC + "The audience cheers on " + player.getName() + " for such an impressive streak!";
		} else if (reason == AudienceReason.HIGH_STREAK_NEGATIVE) {
			possibleEffects.add(AudienceEffect.EXPLOSION);
			possibleEffects.add(AudienceEffect.FIRE);
			possibleEffects.add(AudienceEffect.WEAKNESS);
			possibleEffects.add(AudienceEffect.SMITE);
			coloplayer.setCrowdOpinion(AudienceOpinion.NEGATIVE);
			audienceMessage = ChatColor.GRAY + "" + ChatColor.ITALIC + "The audience doesn't like how " + player.getName() + " is causing a massacre!";
		} else if (reason == AudienceReason.INACTIVE) {
			possibleEffects.add(AudienceEffect.SMITE);
			possibleEffects.add(AudienceEffect.EXPLOSION);
			possibleEffects.add(AudienceEffect.FIRE);
			if (hotPotatoMode == false) {
				possibleEffects.add(AudienceEffect.HOT_POTATO);
			}
			coloplayer.setCrowdOpinion(AudienceOpinion.NEGATIVE);
			audienceMessage = ChatColor.GRAY + "" + ChatColor.ITALIC + "The audience is disappointed in " + player.getName() + " for not fighting!";
		} else if (reason == AudienceReason.STRUGGLING_DEATHS) {
			possibleEffects.add(AudienceEffect.RESISTANCE);
			possibleEffects.add(AudienceEffect.REGENERATION);
			possibleEffects.add(AudienceEffect.INCREASED_XP);
			possibleEffects.add(AudienceEffect.INCREASED_GOLD);
			possibleEffects.add(AudienceEffect.STRENGTH);
			coloplayer.setCrowdOpinion(AudienceOpinion.POSITIVE);
			audienceMessage = ChatColor.GRAY + "" + ChatColor.ITALIC + "The audience pities " + player.getName() + " for dying so much!";
		}
		// Pull a random effect
		AudienceEffect effect = Utils.getRandomElement(possibleEffects);
		
		for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
			ColosseumPlayer colooplayer = getColosseumPlayer(onlineplayer, true);
			if (Utils.isInColosseum(colooplayer.getPlayer().getLocation())) {
				onlineplayer.sendMessage(audienceMessage);
			}
		}
		
		new BukkitRunnable() {
			Location currentLoc = startingLoc;
			@Override
			public void run() {
				EnumParticle particle = null;
				if (player == null) {
					this.cancel();
				}
				if (effect.getOpinion() == AudienceOpinion.POSITIVE) {
					particle = EnumParticle.VILLAGER_HAPPY;
				} else if (effect.getOpinion() == AudienceOpinion.NEGATIVE) {
					particle = EnumParticle.SMOKE_LARGE;
				} else if (effect.getOpinion() == AudienceOpinion.NEUTRAL) {
					particle = EnumParticle.VILLAGER_ANGRY;
				} else {
					particle = EnumParticle.BARRIER;
				}
				double inbetween = speed;
				Vector vec1 = currentLoc.toVector();
				Vector vec2 = player.getPlayer().getLocation().add(0, 1, 0).toVector();
				Vector vector = vec2.clone().subtract(vec1).normalize().multiply(inbetween);
				vec1.add(vector);
				Utils.sendGenericParticle(plugin, vec1.getX(), vec1.getY(), vec1.getZ(), particle);
				currentLoc = vec1.toLocation(player.getPlayer().getWorld());
				currentLoc.getWorld().playSound(currentLoc, Sound.CLICK, 1, 0.25F);
				// There may be a less costly way to do this
				if (currentLoc.distance(player.getPlayer().getLocation().add(0, 1, 0)) <= 1) {
					// Grant content
					ContentListeners.onAudienceEffect(player, effect);
					// Give the player their effect
					
					if (effect == AudienceEffect.ABSORPTION) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 0));
						player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.YELLOW + 
								"granted you 4 HP of absorption for 10 seconds!");
						
					} else if (effect == AudienceEffect.EXPLOSION) {
						currentLoc.getWorld().createExplosion(currentLoc.getX(), currentLoc.getY(), currentLoc.getZ(), 1.5F, false, false);
						player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.DARK_GRAY + 
								"blasted you!");
						
					} else if (effect == AudienceEffect.FIRE) {
						player.setFireTicks(100);
						player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.DARK_GRAY + 
								"set you on fire for 5 seconds!");
						
					} else if (effect == AudienceEffect.INCREASED_GOLD) {
						player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.GOLD + 
								"(did not) granted you +25% gold for the next minute!");
						
					} else if (effect == AudienceEffect.INCREASED_XP) {
						player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.AQUA + 
								"(did not) granted you +30% XP for the next minute!");
						
					} else if (effect == AudienceEffect.INSTANT_HEAL) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
						player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.GREEN + 
								"healed you for 8 HP!");
						
					} else if (effect == AudienceEffect.REGENERATION) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 0));
						player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.GREEN + 
								"granted you Regen I for 10 seconds!");
						
					} else if (effect == AudienceEffect.STRENGTH) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 120, 0));
						player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.RED + 
								"granted you Strength I for 6 seconds!");
						
					} else if (effect == AudienceEffect.WEAKNESS) {
						player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400, 1));
						player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.DARK_GRAY + 
								"inflicted Weakness II for 20 seconds!");
						
					} else if (effect == AudienceEffect.SMITE) {
						player.getWorld().strikeLightningEffect(player.getLocation());
						player.damage(6);
						player.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.DARK_GRAY + 
								"Their opinions struck you for 6 true damage!");
						
					} else if (effect == AudienceEffect.LARGEKB) {
						player.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.YELLOW + 
								"Your next hit deals increased knockback!");
						coloplayer.setLargeKB(true);
						
					} else if (effect == AudienceEffect.BLOCKBUFF) {
						player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.DARK_PURPLE + 
								"Your sword blocks are 25% more effective for 10 seconds!");
						coloplayer.setBlockBuff(true);
						new BukkitRunnable() {
							@Override
							public void run() {
								if (coloplayer != null) {
									coloplayer.setBlockBuff(false);
								}
							}
						}.runTaskLater(plugin, 200);
					} else if (effect == AudienceEffect.BLASTED_AWAY) {
						player.sendMessage(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.DARK_BLUE +
								"The audience blasted anyone in a 4 block radius away from you!");
						List<Entity> list = Utils.filterPlayersFromList(player.getNearbyEntities(4, 4, 4));
						for (int i = 0; i < list.size(); i++) {
				    		Player victim = (Player) list.get(i);
				    		Vector vec3 = player.getLocation().toVector();
				    		Vector vec4 = victim.getLocation().toVector();
				    		Vector finalVector = vec3.clone().subtract(vec4).normalize().multiply(-2).setY(0.5);
				    		victim.setVelocity(finalVector);
				    		victim.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "AUDIENCE! " + ChatColor.DARK_GRAY + 
								"You were blasted away by " + player.getName() + "!");
				    	}
						Utils.sendbeegExplosion(player.getLocation());
						player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1, 1);
					} else if (effect == AudienceEffect.HOT_POTATO) {
						player.getInventory().addItem(RedactedGive.HotPotato(1));
						hotPotatoMode = true;
						hotPotatoCountdown = 10;
						hotPotatoVictim = player;
					}
					Utils.sendAudienceEffectParticles(player.getLocation().add(0, 1, 0), effect.getOpinion());
					this.cancel();
				}
			}
			
		}.runTaskTimer(plugin, 0, 2);
	}

}

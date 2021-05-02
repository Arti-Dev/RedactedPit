package com.articreep.redactedpit;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.articreep.redactedpit.colosseum.AudienceOpinion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;

public class Utils {
	/**
     * Checks whether the location is inside the Colosseum
     *
     * @param loc Location
     * @return true if inside, false if outside
     */
	public static boolean isInColosseum(Location loc) {
		if (!(loc.getWorld() == Bukkit.getWorld("redacted2"))) return false;
		int locX = loc.getBlockX();
		int locY = loc.getBlockY();
		int locZ = loc.getBlockZ();
		//Test if player is in main launcher ignore box
		// -4 98 10 to -23 30 23 - ignore zone for main launcher
		if (-23 <= locX && locX <= -4 && 30 <= locY && locY <= 98 && 10 <= locZ && locZ <= 23) return false;
		//Test if player is in side launcher ignore box
		//-4 98 98 to -25 30 75 - ignore zone for side launcher
		if (-25 <= locX && locX <= -4 && 30 <= locY && locY <= 98 && 75 <= locZ && locZ <= 98) return false;
		//If the player is not in either of the ignore boxes, check if they are in the colosseum boundaries
		//-4 98 10 to -100 30 98 - Colosseum boundaries
		if (-100 <= locX && locX <= -4 && 30 <= locY && locY <= 98 && 10 <= locZ && locZ <= 98) {
			return true;
		}
		return false;
	}
	
	/**
	 * Sends an action bar with designated text
	 * 
	 * @param player Player to send actionbar to
	 * @param string String
	 */
	public static void sendActionBar(Player player, String string) {
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + string + "\"}"), (byte) 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	/**
     * Sends a particle packet at specified location (3 particles per packet)
     *
     * @param plugin Plugin object
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param particle Designated particle type
     */
	public static void sendGenericParticle(Main plugin, double x, double y, double z, EnumParticle particle) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) x, (float) y, (float) z, 0, 0, 0, 0, 3);
		new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				for(Player online : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
                }
				i++;
				if (i > 2) {
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 5);
		return;
	}
	
	public static void sendGenericParticle(Main plugin, Location loc, EnumParticle particle) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 3);
		new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				for(Player online : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
                }
				i++;
				if (i > 2) {
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 5);
		return;
	}
	
	public static void sendGenericParticleOnHead(Main plugin, Player player, EnumParticle particle) {
		new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				Location loc = player.getLocation().add(0, 1, 0);
				PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 3);
				for(Player online : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
                }
				i++;
				if (i > 2) {
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 5);
		return;
	}
	
	/**
	 * Sends a pre-determined redstone particle at specified location
	 * 
	 * @param plugin Plugin object
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 */
	public static void sendRedstoneParticle(Main plugin, double x, double y, double z) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (float) x, (float) y, (float) z, 0, 0, 0, 0, 3);
		new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				for(Player online : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
                }
				i++;
				if (i > 2) {
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 5);
		return;
	}
	
	/**
	 * Connects two Locations with the specified particle
	 * 
	 * @param plugin Plugin object
	 * @param loc1 First location
	 * @param loc2 Second location
	 * @param particle Designated particle type
	 */
	public static void connectPoints(Main plugin, Location loc1, Location loc2, EnumParticle particle) {
		double distance = loc1.distance(loc2);
		double inbetween = 0.2;
		Vector vec1 = loc1.toVector();
		Vector vec2 = loc2.toVector();
		Vector vector = vec2.clone().subtract(vec1).normalize().multiply(inbetween);
		for (double covered = 0; covered < distance; vec1.add(vector)) {
			Utils.sendGenericParticle(plugin, vec1.getX(), vec1.getY(), vec1.getZ(), particle);
			covered += inbetween;
		}
	}
	
	/**
	 * Sends a pre-determined smoke particle at specified location
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 */
	public static void sendSmokeParticle(double x, double y, double z) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_LARGE, true, (float) x, (float) y, (float) z, 0.5F, 0, 0.5F, 0, 20);
		for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
		}
		return;
	}
	
	public static void sendAudienceEffectParticles(Location loc, AudienceOpinion opinion) {
		EnumParticle particle = null;
		if (opinion == AudienceOpinion.POSITIVE) {
			particle = EnumParticle.HEART;
		} else if (opinion == AudienceOpinion.NEUTRAL || opinion == AudienceOpinion.NEGATIVE) {
			particle = EnumParticle.SMOKE_NORMAL;
		}
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true, (float) loc.getX(), (float) loc.getY() + 1, (float) loc.getZ(), 0.25F, 0, 0.25F, 0, 4);
		for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
		}
		return;
	}
	
	/**
	 * Sends a pre-determined explosion particle at specified location
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 */
	public static void sendExplosion(float x, float y, float z) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_LARGE, true, x, y, z, 0, 0, 0, 0, 1);
		for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
		}
		return;
	}
	
	/**
	 * Sends a pre-determined BIG explosion particle at specified location
	 * 
	 * @param x X coordinate
	 * @param y Y coordinate
	 * @param z Z coordinate
	 */
	public static void sendbeegExplosion(float x, float y, float z) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_HUGE, true, x, y, z, 0, 0, 0, 0, 1);
		for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
		}
		return;
	}
	
	public static void sendbeegExplosion(Location loc) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_HUGE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 1);
		for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
		}
		return;
	}
	/**
	 * Formats a long in milliseconds to a human-readable time value
	 * 
	 * @param millis time in milliseconds
	 * @return Human-readable string
	 */
	public static String formattime(long millis) {
		String formattedtime = String.format("%02d:%02d:%02d:%03d", //This formats the time correctly
				TimeUnit.MILLISECONDS.toHours(millis),
			    TimeUnit.MILLISECONDS.toMinutes(millis) - 
			    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
			    TimeUnit.MILLISECONDS.toSeconds(millis) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
			    millis - 
			    TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
		return formattedtime;
		
	}
	
	/**
     * Returns a random element from the given list
     *
     * @param list List
     * @param <T> Type of object
     * @return Random element
     */
    public static <T> T getRandomElement(List<T> list) {
    	int index = (int) (Math.random() * list.size());
    	return list.get(index);
    }
    
    public static <T> List<T> filterPlayersFromList(List<T> list) {
    	for (int i = 0; i < list.size(); i++) {
    		if (!(list.get(i) instanceof Player)) {
    			list.remove(i);
    		}
    	}
    	return list;
    }
}

package com.articreep.redactedpit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.articreep.redactedpit.colosseum.AudienceOpinion;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

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
	 * Sends a title to the player with designated title and subtitle
	 * @param player Player
	 * @param title Title
	 * @param subtitle Subtitle
	 * @param fadeIn Time it takes to fade in in ticks
	 * @param stay Time it stays on the screen in ticks
	 * @param fadeOut Time it takes to fade out in ticks
	 */
	public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		PacketPlayOutTitle titlepacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, ChatSerializer.a("{\"text\": \"" + title + "\"}"));
		PacketPlayOutTitle subtitlepacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\": \"" + subtitle + "\"}"));
		PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlepacket);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitlepacket);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
	}
	/**
	 * Sends a title to the player with designated title and subtitle
	 * @param player Player
	 * @param title Title
	 * @param fadeIn Time it takes to fade in in ticks
	 * @param stay Time it stays on the screen in ticks
	 * @param fadeOut Time it takes to fade out in ticks
	 */
	public static void sendTitle(Player player, String title, int fadeIn, int stay, int fadeOut) {
		PacketPlayOutTitle titlepacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, ChatSerializer.a("{\"text\": \"" + title + "\"}"));
		PacketPlayOutTitle length = new PacketPlayOutTitle(fadeIn, stay, fadeOut);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlepacket);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
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
	}
	
	public static void sendbeegExplosion(Location loc) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_HUGE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 1);
		for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
		}
	}
	/**
	 * Formats a long in milliseconds to a human-readable time value
	 * 
	 * @param millis time in milliseconds
	 * @return Human-readable string
	 */
	public static String formattime(long millis) {
		return String.format("%02d:%02d:%02d:%03d", //This formats the time correctly
				TimeUnit.MILLISECONDS.toHours(millis),
			    TimeUnit.MILLISECONDS.toMinutes(millis) -
			    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
			    TimeUnit.MILLISECONDS.toSeconds(millis) -
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
			    millis -
			    TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
		
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

	// Nice little method to create a gui item with a custom name, and description
	public static ItemStack createGuiItem(final Material material, final String name, final String... lore) {
		final org.bukkit.inventory.ItemStack item = new ItemStack(material, 1);
		final ItemMeta meta = item.getItemMeta();

		// Set the name of the item
		meta.setDisplayName(name);

		// Set the lore of the item
		meta.setLore(Arrays.asList(lore));


		item.setItemMeta(meta);

		return item;
	}

	/**
	 * Gets blocks around a block by radius. Does not include the center block, nor any air blocks.
	 * @param center The location of the center block
	 * @param radius The radius
	 * @return List containing all of the blocks around the center
	 */
	public static ArrayList<Block> getBlocksAround(Location center, int radius) {
    	ArrayList<Block> list = new ArrayList<Block>();
		for (double x = center.getX() - radius; x <= center.getX() + radius; x++) {
			for (double z = center.getZ() - radius; z <= center.getZ() + radius; z++) {
				Location loc = new Location(center.getWorld(), x, center.getY(), z);
				if (loc.getBlock().getType().isSolid()) {
					list.add(loc.getBlock());
				}
			}
		}
		list.remove(center.getBlock());
		return list;
	}

	public static void sendSandParticle(double x, double y, double z) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.BLOCK_CRACK, true, (float) x, (float) y, (float) z, 0, 0, 0, 0, 20, 12, 1);
		for(Player online : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void sendSandParticle(Location loc) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.BLOCK_CRACK, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 20, 12, 1);
		for(Player online : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void sendPumpkinParticle(double x, double y, double z) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.BLOCK_CRACK, true, (float) x, (float) y, (float) z, 0, 0, 0, 0, 20, 91, 1);
		for(Player online : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public static void sendLavaParticle(Location loc) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.LAVA, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0, 0, 0, 0, 20);
		for(Player online : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
		}
	}


}

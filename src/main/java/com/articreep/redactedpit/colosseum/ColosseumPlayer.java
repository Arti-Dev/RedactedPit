package com.articreep.redactedpit.colosseum;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.articreep.redactedpit.commands.Debug;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ColosseumPlayer {
	// The Player Object will contain your health.
	// There SHOULD NOT be ANY Citizens NPC instances of this class!!
	private Player player;
	public UUID playerUUID;
	public int kills;
	public int killstreak;
	public int deaths;
	private int deathstreak;
	// All hits only count if they are on players
	public int hitsDealt;
	public int hitsTaken;
	private int inactive;
	private double damageDealt;
	private double damageTaken;
	public int secondsInside;
	public int secondsOutside;
	public int crowdMemory;
	private AudienceOpinion crowdOpinion;
	private int crowdOpinionLength;
	public boolean largeKB;
	public boolean blockBuff;
	
	public ColosseumPlayer(Player player) {
		this.player = player;
		playerUUID = player.getUniqueId();
		kills = 0;
		deaths = 0;
		deathstreak = 0;
		hitsDealt = 0;
		hitsTaken = 0;
		damageDealt = 0;
		damageTaken = 0;
		inactive = 0;
		secondsInside = 0;
		secondsOutside = 0;
		crowdMemory = ThreadLocalRandom.current().nextInt(30,40);
		crowdOpinion = AudienceOpinion.NEUTRAL;
		crowdOpinionLength = 0;
		largeKB = false;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public void increaseKillCount(int integer) {
		this.kills += integer;
	}
	
	public void increaseKillStreakCount(int integer) {
		// If Killstreak reaches 2 or more the deathstreak is reset
		this.killstreak += integer;
		if (killstreak >= 2) {
			this.deathstreak = 0;
		}
	}
	
	public void increaseDeathCount(int integer) {
		this.deaths += integer;
	}
	
	public void increaseDeathStreakCount(int integer) {
		this.deathstreak += integer;
	}
	
	public void increaseHitsDealt(int integer) {
		this.hitsDealt += integer;
		this.inactive = 0;
	}
	
	public void increaseHitsTaken(int integer) {
		this.hitsTaken += integer;
		this.inactive = 0;
	}
	
	public void increaseDamageDealt(double number) {
		this.damageDealt += number;
	}
	
	public void increaseDamageTaken(double number) {
		this.damageTaken += number;
	}
	
	public void increaseInactivity(int integer) {
		this.inactive += integer;
	}
	
	public void increaseSecondsInside(int integer) {
		this.secondsInside += integer;
		if (Debug.debug) Bukkit.getLogger().info(
				"[DEBUG] " + player.getName() + " is now at " + this.secondsInside + " seconds inside the colo");
	}
	
	public void increaseSecondsOutside(int integer) {
		this.secondsOutside += integer;
		if (Debug.debug) Bukkit.getLogger().info(
				"[DEBUG] " + player.getName() + " is now at " + this.secondsOutside + " seconds outside the colo");
	}
	
	public double getHealth() {
		BigDecimal bd = BigDecimal.valueOf(player.getHealth());
	    bd = bd.setScale(2, RoundingMode.DOWN);
	    return bd.doubleValue();
	}
	
	public double getDamageDealt() {
		BigDecimal bd = BigDecimal.valueOf(damageDealt);
	    bd = bd.setScale(2, RoundingMode.DOWN);
	    return bd.doubleValue();
	}
	
	public double getDamageTaken() {
		BigDecimal bd = BigDecimal.valueOf(damageTaken);
	    bd = bd.setScale(2, RoundingMode.DOWN);
	    return bd.doubleValue();
	}
	
	public int getDeathStreak() {
		return deathstreak;
	}
	
	public boolean getlargeKB() {
		return largeKB;
	}
	
	public int getInactivity() {
		return inactive;
	}
	
	public void setLargeKB(boolean boo) {
		largeKB = boo;
	}
	
	public boolean getBlockBuff() {
		return blockBuff;
	}
	
	public void setBlockBuff(boolean boo) {
		blockBuff = boo;
	}
	
	public void setCrowdOpinion(AudienceOpinion opinion) {
		crowdOpinion = opinion;
	}
	
	public AudienceOpinion getOpinion() {
		return crowdOpinion;
	}
	
	public int getOpinionLength() {
		return crowdOpinionLength;
	}
	
	public void setOpinionLength(int integer) {
		crowdOpinionLength = integer;
	}
	
	public void increaseOpinionLength(int integer) {
		crowdOpinionLength += integer;
	}
	
	public void clearValues() {
		kills = 0;
		deaths = 0;
		killstreak = 0;
		deathstreak = 0;
		hitsDealt = 0;
		hitsTaken = 0;
		inactive = 0;
		damageDealt = 0;
		damageTaken = 0;
		secondsInside = 0;
		secondsOutside = 0;
		crowdMemory = ThreadLocalRandom.current().nextInt(30,40);
		crowdOpinion = AudienceOpinion.NEUTRAL;
		crowdOpinionLength = 0;
	}
	
	public void clearSecondsOutside() {
		secondsOutside = 0;
	}
	
	public void clearStreakCount() {
		this.killstreak = 0;
	}
	// You can send Player A's stats to Player B if you specify it
	public void sendAllValues(Player player) {
		player.sendMessage(ChatColor.GOLD + this.player.getName() + "'s" + ChatColor.WHITE + " Colosseum Stats");
		player.sendMessage(ChatColor.GOLD + "None of these stats are saved permanently. " +
				"The audience will eventually forget you if you leave for too long - if 'Seconds Outside' reaches 'Crowd Memory'!");
		player.sendMessage("Health: " + getHealth());
		player.sendMessage("Kills: " + kills);
		player.sendMessage("Killstreak: " + killstreak);
		player.sendMessage("Deaths: " + deaths);
		player.sendMessage("Deathstreak: " + deaths);
		player.sendMessage("Hits Dealt: " + hitsDealt);
		player.sendMessage("Hits Taken: "+ hitsTaken);
		player.sendMessage("Inactivity: " + inactive);
		player.sendMessage("Damage Dealt: " + getDamageDealt());
		player.sendMessage("Damage Taken: " + getDamageTaken());
		player.sendMessage("Seconds Inside: " + secondsInside);
		player.sendMessage("Seconds Outside: " + secondsOutside);
		player.sendMessage("Crowd Memory: " + crowdMemory);
		player.sendMessage("Crowd Opinion: " + crowdOpinion);
		player.sendMessage("Crowd Opinion Length: " + crowdOpinionLength);
	}
	
	
}

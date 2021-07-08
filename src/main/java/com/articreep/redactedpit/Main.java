package com.articreep.redactedpit;

import com.articreep.redactedpit.colosseum.ColosseumRunnable;
import com.articreep.redactedpit.commands.*;
import com.articreep.redactedpit.content.ContentExpansion;
import com.articreep.redactedpit.content.ContentListeners;
import com.articreep.redactedpit.listeners.LauncherListeners;
import com.articreep.redactedpit.listeners.Listeners;
import com.articreep.redactedpit.listeners.RaceListeners;
import com.articreep.redactedpit.listeners.GUIListeners;
import com.articreep.redactedpit.treasure.TreasureListeners;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
	private File playerDataFile;
	private FileConfiguration playerConfig;

	@Override
	public void onEnable() {
		saveDefaultConfig(); //Saves default config that is found in src/main/resources
		createPlayerData();
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		getServer().getPluginManager().registerEvents(new RaceListeners(this), this);
		getServer().getPluginManager().registerEvents(new GUIListeners(this), this);
		getServer().getPluginManager().registerEvents(new LauncherListeners(this), this);
		TreasureListeners treasureListeners = new TreasureListeners(this);
		treasureListeners.runTaskTimerAsynchronously(this, 0, 20);
		getServer().getPluginManager().registerEvents(treasureListeners, this);
		try {
			getServer().getPluginManager().registerEvents(new ContentListeners(this), this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ColosseumRunnable coloRunnable = new ColosseumRunnable(this);
		coloRunnable.runTaskTimer(this, 20, 20);
		// Small check to make sure that PlaceholderAPI is installed
		if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
			new ContentExpansion(this).register();
		}

		getServer().getPluginManager().registerEvents(coloRunnable, this);
		getCommand("spikeaxeminer").setExecutor(new SpikeaxeMiner(this));
		getCommand("content").setExecutor(new ContentCommand());
		getCommand("spawn").setExecutor(new Spawn(this));
		getCommand("questbook").setExecutor(new QuestBook());
		getCommand("forcerespawn").setExecutor(new ForceRespawn());
		getCommand("sendup").setExecutor(new SendUp());
		getCommand("resetcontent").setExecutor(new ResetContent());
		getCommand("redacteddebug").setExecutor(new Debug(this));
		getCommand("togglejumppads").setExecutor(new ToggleJumpPads());
		getCommand("importgold").setExecutor(new ImportGold(this));
		getCommand("tradetimewarp").setExecutor(new TradeTimeWarp(this));
		getCommand("redactedgive").setExecutor(new RedactedGive(this));
		getCommand("cancelrace").setExecutor(new CancelRace());
		getCommand("tradedivineglass").setExecutor(new TradeDivineGlass(this));
		getCommand("tradingmaster").setExecutor(new TradingGUIs(this));
		getCommand("tradeSunStone").setExecutor(new TradeSunStone(this));
		getCommand("tradevoidcharm").setExecutor(new TradeVoidCharm(this));
		getCommand("coloinfo").setExecutor(new ColoInfo());
		getLogger().info("RedactedPit has been loaded!");
	}
    @Override
    public void onDisable() {
		for (Player onlineplayer : Bukkit.getOnlinePlayers()) {
			try {
				ContentListeners.getRedactedPlayer(onlineplayer).saveData();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	getLogger().info("Disabling RedactedPit!");
    }

	public FileConfiguration getPlayerConfig() {
		return this.playerConfig;
	}

	public File getDataFile() {
		return playerDataFile;
	}

    private void createPlayerData() {
		playerDataFile = new File(getDataFolder(), "playerdata.yml");
		if (!playerDataFile.exists()) {
			playerDataFile.getParentFile().mkdirs();
			saveResource("playerdata.yml", false);
		}

		playerConfig = new YamlConfiguration();
		try {
			playerConfig.load(playerDataFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}




}
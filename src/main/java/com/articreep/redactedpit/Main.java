package com.articreep.redactedpit;

import com.articreep.redactedpit.colosseum.ColosseumRunnable;
import com.articreep.redactedpit.commands.*;
import com.articreep.redactedpit.content.ContentExpansion;
import com.articreep.redactedpit.content.ContentListeners;
import com.articreep.redactedpit.listeners.LauncherListeners;
import com.articreep.redactedpit.listeners.Listeners;
import com.articreep.redactedpit.listeners.RaceListeners;
import com.articreep.redactedpit.listeners.TradingListeners;
import com.articreep.redactedpit.treasure.TreasureListeners;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

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
		getServer().getPluginManager().registerEvents(new TradingListeners(this), this);
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
		this.getCommand("resetcontent").setExecutor(new ResetContent());
		this.getCommand("redacteddebug").setExecutor(new Debug(this));
		this.getCommand("togglejumppads").setExecutor(new ToggleJumpPads());
		this.getCommand("importgold").setExecutor(new ImportGold(this));
		this.getCommand("tradetimewarp").setExecutor(new TradeTimeWarp(this));
		this.getCommand("redactedgive").setExecutor(new RedactedGive(this));
		this.getCommand("cancelrace").setExecutor(new CancelRace());
		this.getCommand("tradedivineglass").setExecutor(new TradeDivineGlass(this));
		this.getCommand("tradingmaster").setExecutor(new TradingGUIs(this));
		this.getCommand("tradeSunStone").setExecutor(new TradeSunStone(this));
		this.getCommand("tradevoidcharm").setExecutor(new TradeVoidCharm(this));
		this.getCommand("redactedgive").setTabCompleter(new RedactedGiveTabComplete());
		this.getCommand("coloinfo").setExecutor(new ColoInfo());
		getLogger().info("RedactedPit has been loaded!");
	}
    @Override
    public void onDisable() {
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
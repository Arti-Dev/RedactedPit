package com.articreep.redactedpit;

import com.articreep.redactedpit.colosseum.ColosseumRunnable;
import com.articreep.redactedpit.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		saveDefaultConfig(); //Saves default config that is found in src/main/resources
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		getServer().getPluginManager().registerEvents(new RaceListeners(this), this);
		getServer().getPluginManager().registerEvents(new TradingListeners(this), this);
		ColosseumRunnable colorunnable = new ColosseumRunnable(this);
		colorunnable.runTaskTimer(this, 20, 20);
		getServer().getPluginManager().registerEvents(colorunnable, this);
		this.getCommand("redacteddebug").setExecutor(new Debug(this));
		this.getCommand("togglejumppads").setExecutor(new ToggleJumpPads());
		this.getCommand("redactedgive").setExecutor(new RedactedGive(this));
		this.getCommand("cancelrace").setExecutor(new CancelRace());
		this.getCommand("tradedivineglass").setExecutor(new TradeDivineGlass(this));
		this.getCommand("tradingmaster").setExecutor(new TradingGUIs(this));
		this.getCommand("tradeSunStone").setExecutor(new TradeSunStone(this));
		this.getCommand("redactedgive").setTabCompleter(new RedactedGiveTabComplete());
		this.getCommand("coloinfo").setExecutor(new ColoInfo());
		getLogger().info("RedactedPit has been loaded!");
	}
    @Override
    public void onDisable() {
    	getLogger().info("Disabling RedactedPit!");
    }
    
    
}
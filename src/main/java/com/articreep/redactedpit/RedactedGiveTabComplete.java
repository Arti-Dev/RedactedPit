package com.articreep.redactedpit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;


public class RedactedGiveTabComplete implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> commands = new ArrayList<>();
		if (args.length == 1) {
			commands.add("AncientArtifact");
			commands.add("DivineGlass");
			commands.add("TimeWarpPearl");
			commands.add("SunStone");
			commands.add("VoidCharm");
		}
		final List<String> completions = new ArrayList<>();
		StringUtil.copyPartialMatches(args[0], commands, completions);
		Collections.sort(completions);
		return completions;
	}

}

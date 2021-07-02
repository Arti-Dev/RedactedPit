package com.articreep.redactedpit;

import com.articreep.redactedpit.commands.RedactedGive;
import com.articreep.redactedpit.content.Content;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ContentCommandTabComplete implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		final ArrayList<String> strings = new ArrayList<>();
		final List<String> completions = new ArrayList<>();
		if (args.length == 1) {
			strings.add("add");
			strings.add("remove");
			StringUtil.copyPartialMatches(args[0], strings, completions);
		} else if (args.length == 2) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				strings.add(player.getName());
			}
			StringUtil.copyPartialMatches(args[1], strings, completions);
		} else if (args.length == 3) {
			for (Content content : Content.values()) {
				strings.add(content.toString());
			}
			StringUtil.copyPartialMatches(args[2], strings, completions);
		}
		Collections.sort(completions);
		return completions;
	}

}

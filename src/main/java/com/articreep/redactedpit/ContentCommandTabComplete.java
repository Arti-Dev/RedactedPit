package com.articreep.redactedpit;

import com.articreep.redactedpit.commands.RedactedGive;
import com.articreep.redactedpit.content.Content;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ContentCommandTabComplete implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> commands = new ArrayList<>();
		if (args.length == 0) {
			List<String> operations = new ArrayList<String>() {{
				add("add");
				add("remove");
			}};
			commands.addAll(operations);
		} else if (args.length == 2) {
			ArrayList<String> contents = new ArrayList<String>();
			for (Content content : Content.values()) {
				contents.add(content.toString());
			}
			commands.addAll(contents);
		}
		final List<String> completions = new ArrayList<>();
		StringUtil.copyPartialMatches(args[0], commands, completions);
		Collections.sort(completions);
		return completions;
	}

}

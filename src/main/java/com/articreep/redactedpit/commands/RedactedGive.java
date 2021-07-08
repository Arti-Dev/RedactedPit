package com.articreep.redactedpit.commands;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.articreep.redactedpit.Main;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.util.StringUtil;

public class RedactedGive implements CommandExecutor, TabCompleter {
	Main plugin;
	public RedactedGive(Main plugin) {
		this.plugin = plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			// Prepare to store the player's inputs
			String itemname = null;
			int number = 0;
			//Check number of arguments
			if (args.length < 1) {
				sendErrorMessage(player, "Not enough arguments.");
				return true;
			// If the player only specified an item just default to 1
			} else if (args.length == 1) { 
				number = 1;
			} else if (args.length > 2) {
				sendErrorMessage(player, "Too many arguments.");
				return true;
			}
			ArrayList<String> list = createList();
			//There are exactly two arguments? Check if the first argument is valid.
			for (int i = 0; i < list.size(); i++) { //loop through array
				if (args[0].equalsIgnoreCase(list.get(i))) { //if the argument is equal to something in the list
					itemname = list.get(i);
					break;
				}
				if (i == list.size() - 1) { //If the list is gone entirely through
					sendErrorMessage(player, "Invalid item!");
					return true;
				}
			}
			// Check if the second argument is an integer
			// If player only specified the item skip processing of second argument as it doesn't exist
			if (args.length != 1) {
				try { 
					number = Integer.parseInt(args[1]); //Second argument
				} catch (NumberFormatException e) {
					sendErrorMessage(player, "Please enter an integer!");
					return true;
				} //Won't be checking for negatives because that's funny
			}
			//Both arguments are valid. Give the player their items.
			Method method = null; //Initialize variable because Eclipse told me to
			try { 
				method = RedactedGive.class.getDeclaredMethod(itemname, int.class); //get the class that I specify with the itemname variable
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			ItemStack finalitem = null;
			try {
				finalitem = (ItemStack) method.invoke(null, number); // run the method and pass the number argument to it
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			// Send message telling player what they obtained
			if (number < 0) {
				player.sendMessage(ChatColor.GREEN + "\"Removing \"" + finalitem.getItemMeta().getDisplayName() + ChatColor.GRAY + " x" + Math.abs(number));
			} else if (number > 0) {
				player.sendMessage(ChatColor.GREEN + "Giving " + finalitem.getItemMeta().getDisplayName() + ChatColor.GRAY + " x" + number);
			}
			// Add item to player's inventory
			player.getInventory().addItem(finalitem);
			return true;
		}
		return false;
	}
	// Generic error message
	public static void sendErrorMessage(Player player, String message) {
		player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + message);
		player.sendMessage(ChatColor.RED + "Usage: /redactedgive <item> <quantity>");
		player.sendMessage(ChatColor.RED + "Acceptable items include: AncientArtifact, DivineGlass, TimeWarpPearl, SunStone, TRexTooth, VoidCharm, ArcheologistShovel, Spikeaxe");
	}
	
	// Makes the list that we check against. Kind of like "registering" the methods.
	// TODO Whenever a new item is added, ALWAYS register them here!
	public static ArrayList<String> createList() {
		ArrayList<String> list = new ArrayList<>();
		list.add("AncientArtifact");
		list.add("DivineGlass");
		list.add("TimeWarpPearl");
		list.add("SunStone");
		list.add("VoidCharm");
		list.add("HotPotato");
		list.add("TRexTooth");
		list.add("ArcheologistShovel");
		list.add("Spikeaxe");
		return list;
	}

	// TODO All item methods go here now!!

	public static ItemStack AncientArtifact(int quantity) {
		final ItemStack item = new ItemStack(Material.DOUBLE_PLANT, quantity, (short) 0);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(ChatColor.YELLOW + "Ancient Artifact");

        // Set the lore of the item
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Kept on death", "", ChatColor.GRAY + "A valuable relic of the past.", 
        		ChatColor.GRAY + "Trade it for unique " + ChatColor.YELLOW + "items", ChatColor.GRAY + "during the " + ChatColor.AQUA + "Time Travel " 
        + ChatColor.GRAY + "map."));
        

        item.setItemMeta(meta);

        return item;
	}

	public static ItemStack ArcheologistShovel(int quantity) {
		final ItemStack item = new ItemStack(Material.GOLD_SPADE);
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatColor.YELLOW + "Archeologist Shovel");
		meta.setLore(Arrays.asList("",
				ChatColor.GRAY + "Find " + ChatColor.YELLOW + "treasures " + ChatColor.GRAY + "with this shovel.",
				ChatColor.GRAY + "Right click to point towards",
				ChatColor.GRAY + "the nearest " + ChatColor.YELLOW + "treasure" + ChatColor.GRAY + ".",
				ChatColor.GRAY + "Treasures may contain " + ChatColor.YELLOW + "Ancient Artifacts"));
		meta.spigot().setUnbreakable(true);

		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack DivineGlass(int quantity) {
		final ItemStack item = new ItemStack(Material.STAINED_GLASS, quantity, (short) 9);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(ChatColor.AQUA + "Divine Glass");

        // Set the lore of the item
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Kept on death", "", ChatColor.GRAY + "Breaks after " + ChatColor.AQUA + "10", 
        		ChatColor.AQUA + "seconds" + ChatColor.GRAY + ". Fragile!"));
        

        item.setItemMeta(meta);

        return item;
	}
	
	public static ItemStack TimeWarpPearl(int quantity) { //This currently uses NMS, will change so we don't use NMS at all
		final ItemStack item = new ItemStack(Material.ENDER_PEARL, quantity);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(ChatColor.AQUA + "Time Warp Pearl");

        // Set the lore of the item
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Kept on death", "", ChatColor.GRAY + "Teleports you back to", 
        		ChatColor.GRAY + "your previous location after", ChatColor.DARK_PURPLE + "3 seconds"));
        

        item.setItemMeta(meta);

        return item;
	}
	
	public static ItemStack SunStone(int quantity) {
		final ItemStack item = new ItemStack(Material.GOLD_BLOCK, quantity);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(ChatColor.YELLOW + "Sun Stone");

        // Set the lore of the item
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Kept on death", "", ChatColor.GRAY + "Used to power the " + ChatColor.AQUA +
        		"KOTH ", ChatColor.GRAY + "on the " + ChatColor.YELLOW + "Sun Pyramid"));
        
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);

        return item;
	}

	public static ItemStack VoidCharm(int quantity) {
		final ItemStack item = new ItemStack(Material.BEDROCK, quantity);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Void Charm");

        // Set the lore of the item
        meta.setLore(Arrays.asList(ChatColor.GRAY + "Kept on death", "", ChatColor.GRAY + "Launches you out of", 
        		ChatColor.GRAY + "the void and deals " + ChatColor.RED + "3\u2764", ChatColor.AQUA + "CREATIVE mode bypasses!"));
        

        item.setItemMeta(meta);

        return item;
	}
	
	public static ItemStack HotPotato(int quantity) {
		final ItemStack item = new ItemStack(Material.BAKED_POTATO, quantity);
		final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(ChatColor.RED + "Hot Potato");

        // Set the lore of the item
        meta.setLore(Arrays.asList("Never gonna give you up",
        		ChatColor.RED + "This item's functionality was removed!"));
        

        item.setItemMeta(meta);

        return item;
	}

	public static ItemStack TRexTooth(int quantity) {
		ItemStack item = new ItemStack(Material.GHAST_TEAR, quantity);
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatColor.WHITE + "T-Rex Tooth");
		meta.setLore(Arrays.asList(ChatColor.GRAY + "Lives: " + ChatColor.GREEN + "8" + ChatColor.GRAY + "/8",
				"", ChatColor.GRAY + "Grants " + ChatColor.YELLOW + "+10% " + ChatColor.GRAY + "walk speed.",
				ChatColor.GRAY + "(Up to +20%)"));
		item.setItemMeta(meta);
		net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = (nmsStack.hasTag()) ? nmsStack.getTag() : new NBTTagCompound();
		NBTTagList modifiers = new NBTTagList();
		NBTTagCompound damage = new NBTTagCompound();
		damage.set("AttributeName", new NBTTagString("generic.attackDamage"));
		damage.set("Name", new NBTTagString("generic.attackDamage"));
		damage.set("Amount", new NBTTagDouble(6.75));
		damage.set("Operation", new NBTTagInt(0));
		damage.set("UUIDLeast", new NBTTagInt(894654));
		damage.set("UUIDMost", new NBTTagInt(2872));
		modifiers.add(damage);

		NBTTagCompound speed = new NBTTagCompound();
		speed.set("AttributeName", new NBTTagString("generic.movementSpeed"));
		speed.set("Name", new NBTTagString("generic.movementSpeed"));
		speed.set("Amount", new NBTTagFloat(0.1F));
		speed.set("Operation", new NBTTagInt(1));
		speed.set("UUIDLeast", new NBTTagInt(38061));
		speed.set("UUIDMost", new NBTTagInt(170237));
		modifiers.add(speed);

		compound.set("AttributeModifiers", modifiers);
		nmsStack.setTag(compound);
		item = CraftItemStack.asBukkitCopy(nmsStack);
		return item;

	}

	public static ItemStack Spikeaxe(int quantity) {
		ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE, quantity);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "Spikeaxe");
		meta.setLore(Arrays.asList(ChatColor.GRAY + "Knocks your opponent", ChatColor.GRAY + "downwards when you", ChatColor.GRAY + "crit them."));
		meta.spigot().setUnbreakable(true);
		item.setItemMeta(meta);
		return item;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> commands = new ArrayList<>();
		if (args.length == 1) {
			commands.addAll(createList());
		}
		final List<String> completions = new ArrayList<>();
		StringUtil.copyPartialMatches(args[0], commands, completions);
		Collections.sort(completions);
		return completions;
	}
}

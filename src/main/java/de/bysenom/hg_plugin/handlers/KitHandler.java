package de.bysenom.hg_plugin.handlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KitHandler {
    private static final Map<UUID, Boolean> ninjaEnabledMap = new HashMap<>();
    private static final Map<UUID, Boolean> anchorEnabledPlayers = new HashMap<>();

    // Method to enable anchor for a specific player
    public static boolean enableAnchor(Player player) {
        // Disable ninja for the player
        disableNinja(player);
        // Set the anchor enabled status for the player
        anchorEnabledPlayers.put(player.getUniqueId(), true);
        return false;
    }

    // Method to disable anchor for a specific player
    public static void disableAnchor(Player player) {
        // Set the anchor disabled status for the player
        anchorEnabledPlayers.put(player.getUniqueId(), false);
    }

    // Method to check if anchor is enabled for a specific player
    public static boolean isAnchorEnabled(Player player) {
        // Check if the player's UUID exists in the anchor enabled players map
        return anchorEnabledPlayers.getOrDefault(player.getUniqueId(), false);
    }

    // Method to enable ninja for a player
    public static boolean enableNinja(Player player) {
        // Disable anchor for the player
        disableAnchor(player);
        // Set the ninja enabled status for the player
        ninjaEnabledMap.put(player.getUniqueId(), true);
        return false;
    }

    // Method to disable ninja for a player
    public static void disableNinja(Player player) {
        // Set the ninja disabled status for the player
        ninjaEnabledMap.put(player.getUniqueId(), false);
    }

    // Method to check if ninja is enabled for a player
    public static boolean isNinjaEnabled(Player player) {
        return ninjaEnabledMap.getOrDefault(player.getUniqueId(), false);
    }

    // Method to check if an item is an anchor for a specific player
    public static boolean isAnchor(ItemStack item, Player player) {
        // Implement logic to check if the item is an anchor for the player
        // For example:
        // return item.getType() == Material.ANVIL && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Anchor Kit");
        return false; // Placeholder
    }

    // Method to check if an item is a ninja item for a specific player
    public static boolean isNinja(ItemStack item, Player player) {
        // Implement logic to check if the item is a ninja item for the player
        // For example:
        // return item.getType() == Material.LEATHER_BOOTS && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(ChatColor.DARK_PURPLE + "Ninja Kit");
        return false; // Placeholder
    }
}

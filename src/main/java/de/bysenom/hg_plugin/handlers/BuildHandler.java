package de.bysenom.hg_plugin.handlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BuildHandler extends JavaPlugin implements Listener {

    private static boolean allowBlockPlacing = true;
    private static boolean allowBlockBreaking = true;

    // Method to allow block placing
    public static void allowBlockPlacing(Player player) {
        allowBlockPlacing = true;
    }

    // Method to disallow block placing
    public static void disallowBlockPlacing(Player player) {
        allowBlockPlacing = false;
    }

    // Method to allow block breaking
    public static void allowBlockBreaking(Player player) {
        allowBlockBreaking = true;
    }

    // Method to disallow block breaking
    public static void disallowBlockBreaking(Player player) {
        allowBlockBreaking = false;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Check if block breaking is allowed
        if (!allowBlockBreaking) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        // Check if block placing is allowed
        if (!allowBlockPlacing) {
            event.setCancelled(true);
        }
    }
}

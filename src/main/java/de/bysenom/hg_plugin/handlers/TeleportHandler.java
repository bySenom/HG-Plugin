package de.bysenom.hg_plugin.handlers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class TeleportHandler {

    private static final Map<String, Location> locations = new HashMap<>();

    // Method to add a new location
    public static void addLocation(String name, Location location) {
        locations.put(name, location);
    }

    // Method to teleport the player to a specific location by name
    public static void teleportToLocation(Player player, String locationName) {
        Location location = locations.get(locationName);
        if (location != null) {
            player.teleport(location);
        } else {
            player.sendMessage("The specified location doesn't exist.");
        }
    }

    // Method to initialize the locations
    public static void initializeLocations() {
        // Initialize Bukkit if necessary (may not be necessary in all environments)
        if (!Bukkit.getServer().getOnlinePlayers().isEmpty()) {
            Bukkit.getServer().getConsoleSender().sendMessage("Initializing Bukkit...");
        }

        // Get the world reference
        World world = Bukkit.getWorld("world");

        // Add the first location
        Location firstLocation = new Location(world, -209, 69, -228); // Example location coordinates
        addLocation("LobbySpawn", firstLocation);

        // Add the second location
        Location secondLocation = new Location(world, -200, 70, 300); // Example location coordinates
        addLocation("SecondLocation", secondLocation);
    }
}

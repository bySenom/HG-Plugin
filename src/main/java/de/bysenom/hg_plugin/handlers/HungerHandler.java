package de.bysenom.hg_plugin.handlers;

import org.bukkit.entity.Player;

public class HungerHandler {

    // Function to disable hunger for a player
    public static void disableHunger(Player player) {
        if (player != null) {
            player.setFoodLevel(20); // Set food level to maximum
            player.setSaturation(20); // Set saturation to maximum
        }
    }

    // Function to enable hunger for a player
    public static void enableHunger(Player player) {
        if (player != null) {
            // You might set the food level and saturation to default values here
            // For example, you could set food level to 20 and saturation to 5
            player.setFoodLevel(20); // Set food level to maximum
            player.setSaturation(5); // Set saturation to a non-maximum value
        }
    }
}

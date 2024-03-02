package de.bysenom.hg_plugin.handlers;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MovementHandler {

    // Function to disable movement for a player
    public static void disableMovement(Player player) {
        if (player != null) {
            player.setWalkSpeed(0); // Set walk speed to 0
            player.setFlySpeed(0); // Set fly speed to 0
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 250)); // Disable jumping
        }
    }

    // Function to enable movement for a player
    public static void enableMovement(Player player) {
        if (player != null) {
            player.setWalkSpeed(0.2f); // Set walk speed to default (0.2)
            player.setFlySpeed(0.1f); // Set fly speed to default (0.1)
            player.removePotionEffect(PotionEffectType.JUMP); // Remove the jump boost effect
        }
    }
}

package de.bysenom.hg_plugin.handlers;

import org.bukkit.entity.Player;

public class InvincibilityHandler {

    //InvincibilityHandler.removeInvincibility(player); for removing the Invincibility
    //InvincibilityHandler.makeInvincible(player); for giving the Invincibility

    // Function to make a player invincible
    public static void makeInvincible(Player player) {
        if (player != null) {
            player.setInvulnerable(true);
        }
    }

    // Function to remove invincibility from a player
    public static void removeInvincibility(Player player) {
        if (player != null) {
            player.setInvulnerable(false);
        }
    }
}

package de.bysenom.hg_plugin.kits;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Ninja implements Listener {

    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 20000; // 10 seconds cooldown

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        // Check if the player sneaks (shifts)
        if (event.isSneaking()) {
            // Check if the player is on cooldown
            if (!hasCooldownExpired(player)) {
                long remainingCooldown = getRemainingCooldown(player);
                player.sendMessage(ChatColor.AQUA + "[BlackLotus] " + ChatColor.RED + "Fähigkeit ist in " + ChatColor.WHITE + remainingCooldown + ChatColor.RED + " Sekunden bereit!");
                return;
            }

            // Find the nearest enemy
            Entity nearestEnemy = findNearestEnemy(player);
            if (nearestEnemy != null) {
                // Teleport the player behind the nearest enemy
                teleportBehindEnemy(player, nearestEnemy);

                // Set cooldown for the player
                setCooldown(player);
            }
        }
    }

    // Method to find the nearest enemy
    private Entity findNearestEnemy(Player player) {
        Entity nearestEnemy = null;
        double minDistanceSquared = Double.MAX_VALUE;

        for (Entity entity : player.getWorld().getEntities()) {
            // Check if the entity is an enemy (you may need to adjust this logic)
            if (entity instanceof Player && !entity.equals(player)) {
                double distanceSquared = entity.getLocation().distanceSquared(player.getLocation());
                if (distanceSquared < minDistanceSquared) {
                    minDistanceSquared = distanceSquared;
                    nearestEnemy = entity;
                }
            }
        }

        return nearestEnemy;
    }

    // Method to teleport the player behind the enemy
    private void teleportBehindEnemy(Player player, Entity enemy) {
        // Get the direction the enemy is facing
        Vector enemyDirection = enemy.getLocation().getDirection().normalize();

        // Calculate the destination location behind the enemy
        Location destination = enemy.getLocation().subtract(enemyDirection.multiply(2)); // Adjust the multiplier as needed

        // Teleport the player behind the enemy
        player.teleport(destination);

        // Make the player face the enemy
        Location playerLocation = player.getLocation();
        playerLocation.setDirection(enemyDirection);
        player.teleport(playerLocation);

        // Play teleport sound
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
    }


    // Method to set cooldown for the player
    private void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + COOLDOWN_TIME);
    }

    // Method to check if the player's cooldown has expired
    private boolean hasCooldownExpired(Player player) {
        return !cooldowns.containsKey(player.getUniqueId()) || cooldowns.get(player.getUniqueId()) <= System.currentTimeMillis();
    }

    // Method to get the remaining cooldown time for the player
    private long getRemainingCooldown(Player player) {
        if (cooldowns.containsKey(player.getUniqueId())) {
            long remainingTime = cooldowns.get(player.getUniqueId()) - System.currentTimeMillis();
            return Math.max(0, remainingTime / 1000); // Convert milliseconds to seconds
        }
        return 0;
    }
}




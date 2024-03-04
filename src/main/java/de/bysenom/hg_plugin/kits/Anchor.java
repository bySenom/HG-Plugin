package de.bysenom.hg_plugin.kits;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Anchor implements Listener {

    private final JavaPlugin plugin;

    public Anchor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            // Check if the damage cause is knockback
            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                // Play anchor sound
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);

                // Cancel the knockback by setting player velocity to zero
                final Vector originalVelocity = player.getVelocity();
                player.setVelocity(new Vector());

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.setVelocity(originalVelocity);
                    }
                }.runTaskLater(plugin, 1L); // Adjust the delay as needed
            }
        }
    }
}
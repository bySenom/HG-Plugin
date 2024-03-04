package de.bysenom.hg_plugin.kits;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class Anchor implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            // Check if the damage cause is knockback
            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                // Cancel the knockback
                event.setCancelled(true);
                // Play anchor sound
                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
        }
    }
}
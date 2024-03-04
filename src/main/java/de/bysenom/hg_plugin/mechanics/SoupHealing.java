package de.bysenom.hg_plugin.mechanics;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SoupHealing implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Check if the player right-clicked with mushroom soup
        if (item != null && item.getType() == Material.MUSHROOM_STEW && player.getInventory().contains(item)) {
            // Consume the soup
            if (player.getHealth() < player.getMaxHealth()) {
                // Heal the player by 3.5 hearts
                double newHealth = Math.min(player.getHealth() + 7.0, player.getMaxHealth());
                player.setHealth(newHealth);

                // Remove one mushroom soup from the player's hand
                if (item.getAmount() > 1) {
                    item.setAmount(item.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.BOWL)); // Replace mushroom soup with an empty bowl
                }
            }
        }
    }
}

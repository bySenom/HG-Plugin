package de.bysenom.hg_plugin.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class ItemHandler implements Listener {

    private final JavaPlugin plugin;

    public ItemHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check if the player right-clicked with the player head item
        if (item.getType() == Material.PLAYER_HEAD) {
            // Open a custom GUI instead of a chest
            openCustomGUI(player);
        }
    }

    // Method to open a custom GUI
    private void openCustomGUI(Player player) {
        Inventory gui = Bukkit.createInventory(new CustomHolder(), 9, "Custom GUI");

        // Add items to the GUI
        gui.setItem(4, createPlayerHead(player.getUniqueId())); // Pass the player's UUID

        // Play open chest sound
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);

        // Open the GUI for the player
        player.openInventory(gui);
    }

    // Method to create a player head ItemStack
    public ItemStack createPlayerHead(UUID playerUUID) {
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();

        // Set the owning player to the specified player
        Player player = Bukkit.getPlayer(playerUUID);
        if (player != null) {
            meta.setOwningPlayer(player);
        } else {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerUUID));
        }

        // Set the display name
        meta.setDisplayName(ChatColor.RESET + player.getName() + "'s Stats"); // Assuming you're using ChatColor from Bukkit

        playerHead.setItemMeta(meta);

        // Put the item in the last slot of the hotbar
        PlayerInventory inventory = player.getInventory();
        inventory.setItem(8, playerHead);

        return playerHead;
    }

    // Custom InventoryHolder for the custom GUI
    private class CustomHolder implements InventoryHolder {
        @Override
        public Inventory getInventory() {
            return null;
        }
    }
}
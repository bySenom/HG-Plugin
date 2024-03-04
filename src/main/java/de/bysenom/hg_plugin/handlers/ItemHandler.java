package de.bysenom.hg_plugin.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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

            // Set the player head item in the last slot of the hotbar
            ItemStack playerHeadItem = createPlayerHead(player.getUniqueId());
            PlayerInventory playerInventory = player.getInventory();
            playerInventory.clear(8); // Clear the last slot of the hotbar
            playerInventory.setItem(8, playerHeadItem); // Set the player head item in the last slot
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        // Cancel the event to prevent dropping items
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        event.setCancelled(true);
        // Check if the clicked inventory belongs to the player
        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() == player) {
            // Cancel the event to prevent item moving
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        openCustomGUI(player);

        // Check if the dragged inventory belongs to the player
        if (event.getView().getTopInventory().getHolder() == player) {
            // Cancel the event to prevent item moving
            event.setCancelled(true);
        }
    }

    // Method to open a custom GUI
    private void openCustomGUI(Player player) {
        CustomHolder customHolder = new CustomHolder(player);
        Inventory gui = Bukkit.createInventory(customHolder, 9, "Custom GUI");

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
        meta.setDisplayName(ChatColor.RESET + player.getName() + "'s Stats");

        playerHead.setItemMeta(meta);

        // Clear the last slot of the hotbar and set the player head item
        PlayerInventory inventory = player.getInventory();
        inventory.clear(8); // Clear the last slot of the hotbar
        inventory.setItem(8, playerHead); // Set the player head item in the last slot

        return playerHead;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof CustomHolder) {
            CustomHolder customHolder = (CustomHolder) holder;
            Player player = customHolder.getPlayer();

            // Give the player the player head after closing the custom GUI
            ItemStack playerHeadItem = createPlayerHead(player.getUniqueId());
            PlayerInventory playerInventory = player.getInventory();
            playerInventory.clear(8); // Clear the last slot of the hotbar
            playerInventory.setItem(8, playerHeadItem); // Set the player head item in the last slot
        }
    }

    // Custom InventoryHolder for the custom GUI
    private class CustomHolder implements InventoryHolder {
        private final Player player;

        public CustomHolder(Player player) {
            this.player = player;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }

        public Player getPlayer() {
            return player;
        }
    }
}
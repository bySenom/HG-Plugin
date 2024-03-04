package de.bysenom.hg_plugin.handlers;

import org.bukkit.*;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
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

        // Check if the player right-clicked with the chest item
        if (item.getType() == Material.CHEST) {
            // Open the Kits Chest GUI instead of a chest
            openKitsChestGUI(player);

            // Give the player the kits chest after closing the Kits Chest GUI
            ItemStack kitsChestItem = createKitsChest(player.getUniqueId());
            PlayerInventory playerInventory = player.getInventory();
            playerInventory.clear(0); // Clear the first slot of the hotbar
            playerInventory.setItem(0, kitsChestItem); // Set the kits chest item in the first slot
        }

        // Check if the player right-clicked with the anchor item
        if (item.getType() == Material.ANVIL && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.GRAY + "Anchor Kit") && KitHandler.isAnchorEnabled(player)) {
                // Activate the Anchor Kit for the player
                KitHandler.enableAnchor(player);

                // Inform the player that the Anchor Kit is now active
                player.sendMessage(ChatColor.AQUA + "[BlackLotus] " + ChatColor.GRAY + "Anchor Kit" + ChatColor.RED + " wurde ausgewählt!");

                // Close the GUI
                player.closeInventory();

                // Prevent further interaction with the anchor item
                event.setCancelled(true);
            }
        }

        // Check if the player right-clicked with the ninja item
        if (item.getType() == Material.LEATHER_BOOTS && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.DARK_PURPLE + "Ninja Kit") && KitHandler.isNinjaEnabled(player)) {
                // Activate the Ninja Kit for the player
                KitHandler.enableNinja(player);
                // Inform the player that the Ninja Kit is now active
                player.sendMessage(ChatColor.AQUA + "[BlackLotus] " + ChatColor.DARK_PURPLE + "Ninja Kit" + ChatColor.RED + " wurde ausgewählt!");

                // Close the GUI
                player.closeInventory();

                // Prevent further interaction with the ninja item
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack droppedItem = event.getItemDrop().getItemStack();
        if (droppedItem != null && (droppedItem.getType() == Material.PLAYER_HEAD || droppedItem.getType() == Material.CHEST || droppedItem.getType() == Material.LEATHER_BOOTS || droppedItem.getType() == Material.ANVIL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null && (clickedItem.getType() == Material.PLAYER_HEAD || clickedItem.getType() == Material.CHEST || clickedItem.getType() == Material.LEATHER_BOOTS || clickedItem.getType() == Material.ANVIL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        ItemStack draggedItem = event.getOldCursor();
        if (draggedItem != null && (draggedItem.getType() == Material.PLAYER_HEAD || draggedItem.getType() == Material.CHEST || draggedItem.getType() == Material.LEATHER_BOOTS || draggedItem.getType() == Material.ANVIL)) {
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

    private void openKitsChestGUI(Player player) {
        KitsChestHolder kitsChestHolder = new KitsChestHolder(player);
        Inventory gui = Bukkit.createInventory(kitsChestHolder, 9, "Kits Chest GUI");

        // Add items to the GUI
        gui.setItem(4, createKitsChest(player.getUniqueId())); // Add the Kits Chest item

        // Add the Anchor item
        ItemStack anchorItem = createAnchorItem();
        gui.setItem(0, anchorItem); // Set the Anchor item in the first slot

        // Add the Ninja Kit item
        ItemStack ninjaItem = createNinjaItem();
        gui.setItem(1, ninjaItem); // Set the Ninja Kit item in the second slot

        // Play open chest sound
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, SoundCategory.MASTER, 1.0f, 1.0f);

        // Open the GUI for the player
        player.openInventory(gui);
    }

    @EventHandler
    public void onKitsChestClick(InventoryClickEvent event) {
        // Check if the clicked item is the Anchor Kit item
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null && clickedItem.getType() == Material.ANVIL && clickedItem.hasItemMeta()) {
            ItemMeta meta = clickedItem.getItemMeta();
            if (meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.GRAY + "Anchor Kit")) {
                // Activate the Anchor Kit for the player who clicked
                Player player = (Player) event.getWhoClicked();
                KitHandler.enableAnchor(player);

                // Inform the player that the Anchor Kit is now active
                player.sendMessage(ChatColor.AQUA + "[BlackLotus] " + ChatColor.GRAY + "Anchor Kit" + ChatColor.RED + " wurde ausgewählt!");

                // Prevent further interaction with the item
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onNinjaKitClick(InventoryClickEvent event) {
        // Check if the clicked item is the Ninja Kit item
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null && clickedItem.getType() == Material.LEATHER_BOOTS && clickedItem.hasItemMeta()) {
            ItemMeta meta = clickedItem.getItemMeta();
            if (meta.hasDisplayName() && meta.getDisplayName().equals(ChatColor.DARK_PURPLE + "Ninja Kit")) {
                // Activate the Ninja Kit for the player who clicked
                Player player = (Player) event.getWhoClicked();
                KitHandler.enableNinja(player);

                // Inform the player that the Ninja Kit is now active
                player.sendMessage(ChatColor.AQUA + "[BlackLotus] " + ChatColor.DARK_PURPLE + "Ninja Kit" + ChatColor.RED + " wurde ausgewählt!");

                // Prevent further interaction with the item
                event.setCancelled(true);
            }
        }
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

    public ItemStack createKitsChest(UUID playerUUID) {
        ItemStack kitsChest = new ItemStack(Material.CHEST);
        ItemMeta meta = kitsChest.getItemMeta();

        // Set the display name and color
        meta.setDisplayName(ChatColor.DARK_PURPLE + "KITS");

        kitsChest.setItemMeta(meta);

        // Retrieve the player
        Player player = Bukkit.getPlayer(playerUUID);

        if (player != null) {
            // Clear the specified slot of the player's inventory and set the kits chest item
            PlayerInventory inventory = player.getInventory();
            inventory.clear(0); // Clear the first slot of the hotbar
            inventory.setItem(0, kitsChest); // Set the kits chest item in the first slot
        }

        return kitsChest;
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
        } else if (holder instanceof KitsChestHolder) {
            KitsChestHolder chestHolder = (KitsChestHolder) holder;
            Player player = chestHolder.getPlayer();

            // Give the player the kits chest after closing the Kits Chest GUI
            ItemStack kitsChestItem = createKitsChest(player.getUniqueId());
            PlayerInventory playerInventory = player.getInventory();
            playerInventory.clear(0); // Clear the first slot of the hotbar
            playerInventory.setItem(0, kitsChestItem); // Set the kits chest item in the first slot
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

    public KitsChestHolder createKitsChestHolder(Player player) {
        return new KitsChestHolder(player);
    }

    public class KitsChestHolder implements InventoryHolder {
        private final Player player;

        public KitsChestHolder(Player player) {
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


    // Add Items down here

    private ItemStack createAnchorItem() {
        ItemStack anchorItem = new ItemStack(Material.ANVIL); // Assuming ANCHOR is the material for the Anchor item
        ItemMeta meta = anchorItem.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Anchor Kit");
        // You can set other properties of the item, such as lore, enchantments, etc., if needed
        anchorItem.setItemMeta(meta);
        return anchorItem;
    }

    // Method to create the ninja item (anchor item)
    private ItemStack createNinjaItem() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();

        // Set display name
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Ninja Kit");

        // Set boots color to blue
        meta.setColor(Color.BLUE);

        // You can set other properties of the item, such as lore, enchantments, etc., if needed

        boots.setItemMeta(meta);
        return boots;
    }

}
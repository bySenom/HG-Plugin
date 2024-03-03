package de.bysenom.hg_plugin.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ItemHandler implements Listener {

    private static final Map<ItemStack, ItemInteraction> interactActions = new HashMap<>();

    private final Plugin plugin;


    public static ItemStack getWoodenChestItem() {
        ItemStack woodenChestItem = new ItemStack(Material.CHEST);
        ItemMeta meta = woodenChestItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Kits");
            woodenChestItem.setItemMeta(meta);
        }
        return woodenChestItem;
    }

    public static ItemStack getPlayerHeadItem(Plugin plugin) {
        ItemStack playerHeadItem = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = playerHeadItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Player Head");
            playerHeadItem.setItemMeta(meta);
        }

        // Add a custom tag to the item to identify it as the player head item
        NamespacedKey key = new NamespacedKey(plugin, "player_head_item");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "player_head_item");

        // Register right-click action to open a double chest
        registerInteractAction(playerHeadItem, event -> {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR) {
                // Open a double chest for the player
                Player player = event.getPlayer();
                Inventory doubleChest = Bukkit.createInventory(player, 54, "Double Chest"); // Create a double chest inventory
                player.openInventory(doubleChest); // Open the double chest for the player
            }
        });

        return playerHeadItem;
    }

    public ItemHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    public static boolean createWoodenChestItem(Plugin plugin) {
        System.out.println("Creating wooden chest item...");
        ItemStack woodenChestItem = new ItemStack(Material.CHEST);
        ItemMeta meta = woodenChestItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Kits");
            woodenChestItem.setItemMeta(meta);
        }

        // Register right-click action
        registerInteractAction(woodenChestItem, event -> {
            if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR ||
                    event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_AIR) {
                Player player = event.getPlayer();
                PlayerInventory inventory = player.getInventory();

                // Set the wooden chest item in the player's main hand
                inventory.setItemInMainHand(woodenChestItem);
                player.updateInventory(); // Update the player's inventory
            }
        });
        return true;
    }

    public static boolean createPlayerHeadItem(Plugin plugin) {
        System.out.println("Creating player head item...");
        ItemStack playerHeadItem = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = playerHeadItem.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("Player Head");
            playerHeadItem.setItemMeta(meta);
        }

        // Register right-click action
        registerInteractAction(playerHeadItem, event -> {
            if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR ||
                    event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_AIR) {
                // Handle player head item interaction
                Player player = event.getPlayer();
                PlayerInventory inventory = player.getInventory();

                // Get the first slot of the hotbar
                int firstHotbarSlot = inventory.firstEmpty();

                // Check if there is an empty slot in the hotbar
                if (firstHotbarSlot != -1) {
                    // Set the player head item in the player's main hand
                    inventory.setItemInMainHand(playerHeadItem);
                    player.updateInventory(); // Update the player's inventory
                } else {
                    // Hotbar is full, inform the player
                    player.sendMessage(ChatColor.RED + "Your hotbar is full. You cannot receive the item.");
                }
            }
        });
        return true;
    }


    // Listener to handle item interactions
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                NamespacedKey key = new NamespacedKey(plugin, "wooden_chest_item");
                if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    if (interactActions.get(item).debugExecute(event)) {
                        event.getPlayer().getInventory().setItemInMainHand(item);
                        event.getPlayer().updateInventory();
                    }
                }
                key = new NamespacedKey(plugin, "player_head_item");
                if (meta.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    if (interactActions.get(item).debugExecute(event)) {
                        event.getPlayer().getInventory().setItemInMainHand(item);
                        event.getPlayer().updateInventory();
                    }
                }
            }
        }
    }

    // Method to register interact action for an item
    private static void registerInteractAction(ItemStack item, ItemInteraction action) {
        System.out.println("Registering interact action for item: " + item.getType());
        interactActions.put(item, action);
    }

    // Functional interface for item interaction actions
    private interface ItemInteraction {
        void execute(PlayerInteractEvent event);
        // Print debug message when executing the action
        default boolean debugExecute(PlayerInteractEvent event) {
            System.out.println("Executing item interaction action for item: " + event.getItem().getType());
            execute(event);
            return false;
        }
    }

    private interface ItemInteractionHandler {
        boolean execute(PlayerInteractEvent event);
        default boolean debugExecute(PlayerInteractEvent event) {
            System.out.println("Executing item interaction action for item: " + event.getItem().getType());
            return execute(event);
        }
    }

}

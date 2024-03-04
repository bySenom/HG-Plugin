package de.bysenom.hg_plugin.handlers;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvClearHandler {
    public static void clearInventory(Inventory inventory) {
        ItemStack[] contents = inventory.getContents();
        for (int i = 0; i < contents.length; i++) {
            contents[i] = null;
        }
        inventory.setContents(contents);
    }
}

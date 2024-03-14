package de.bysenom.hg_plugin.commands;

import de.bysenom.hg_plugin.handlers.WorldManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveCommand implements CommandExecutor {
    private final WorldManager worldManager;

    public SaveCommand(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("hgsaveworld")) {
            // Check for required permissions or other conditions if needed
            Player player = (Player) sender;
            String playerName = player.getName();

            if (!sender.hasPermission("hgplugin.save")) {
                sender.sendMessage("You don't have permission to use this command.");
                return true;
            }

            // Call the save method in WorldManager
            worldManager.save(playerName);
            sender.sendMessage("[HG-BlackLotus] World saved successfully.");
            return true;
        }
        return false;
    }
}

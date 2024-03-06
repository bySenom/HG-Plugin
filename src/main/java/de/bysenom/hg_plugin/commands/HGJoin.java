package de.bysenom.hg_plugin.commands;

import de.bysenom.hg_plugin.handlers.ItemHandler;
import de.bysenom.hg_plugin.handlers.LobbyCountdown;
import de.bysenom.hg_plugin.handlers.ScoreBoardHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HGJoin implements CommandExecutor {

    private final LobbyCountdown lobbyCountdown;
    private final JavaPlugin plugin; // Plugin instance

    public HGJoin(JavaPlugin plugin, LobbyCountdown lobbyCountdown) {
        this.plugin = plugin;
        this.lobbyCountdown = lobbyCountdown;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("hgjoin")) {
            return false; // Not our command, let other plugins handle it
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen.");
            return true; // Command is handled, no need to continue processing
        }

        Player player = (Player) sender;

        // Start the countdown for the player
        lobbyCountdown.startCountdownForAllPlayers();

        return true; // Command is handled, no need to continue processing
    }
}

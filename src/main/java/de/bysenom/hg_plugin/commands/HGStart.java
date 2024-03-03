package de.bysenom.hg_plugin.commands;

import de.bysenom.hg_plugin.handlers.LobbyCountdown;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HGStart implements CommandExecutor {

    private final LobbyCountdown lobbyCountdown; // Instance of LobbyCountdown

    public HGStart(LobbyCountdown lobbyCountdown) {
        this.lobbyCountdown = lobbyCountdown;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!label.equalsIgnoreCase("hgstart")) {
            return false; // Not our command, let other plugins handle it
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen.");
            return true; // Command is handled, no need to continue processing
        }

        Player player = (Player) sender;

        // Change the countdown time to 10 seconds
        if (lobbyCountdown != null) {
            lobbyCountdown.changeCountdownTime(10);
        }

        return true; // Command is handled, no need to continue processing
    }
}

package de.bysenom.hg_plugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Fly implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String [] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen.");
            return true;
        }

        Player player = (Player) sender;

        if (player.getAllowFlight()) {
           player.setAllowFlight(false);
           player.sendMessage("Flying disabled");
        } else {
            player.setAllowFlight(true);
            player.sendMessage("Flying enabled");
        }

        return true;
    }
}

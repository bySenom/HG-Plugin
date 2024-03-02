package de.bysenom.hg_plugin.mechanics;

import de.bysenom.hg_plugin.handlers.LobbyCountdown;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Lobby {

    private final LobbyCountdown lobbyCountdown;

    public Lobby(LobbyCountdown lobbyCountdown) {
        this.lobbyCountdown = lobbyCountdown;
    }

    public void startCountdownForAllPlayers() {
        // Get all online players and start the countdown for each player
        for (Player player : Bukkit.getOnlinePlayers()) {
            lobbyCountdown.startCountdownForAllPlayers();
        }
    }

    public void syncCountdownForPlayer(Player player) {
        // Start the countdown for the newly joined player
        lobbyCountdown.startCountdownForAllPlayers();
    }
}

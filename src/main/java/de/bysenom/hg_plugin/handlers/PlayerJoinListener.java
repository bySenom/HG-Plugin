package de.bysenom.hg_plugin.handlers;

import de.bysenom.hg_plugin.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final LobbyCountdown lobbyCountdown;

    public PlayerJoinListener(LobbyCountdown lobbyCountdown) {
        this.lobbyCountdown = lobbyCountdown;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Start the countdown when a player joins
        if (lobbyCountdown != null) {
            lobbyCountdown.startCountdownForAllPlayers();
        } else {
            // Handle case where lobbyCountdown is not initialized
            event.getPlayer().sendMessage("Lobby countdown is not initialized.");
        }
    }
}

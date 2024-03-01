package de.bysenom.hg_plugin.handlers;

import de.bysenom.hg_plugin.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerJoinListener implements Listener {

    private final LobbyCountdown lobbyCountdown;

    public PlayerJoinListener(LobbyCountdown lobbyCountdown) {
        this.lobbyCountdown = lobbyCountdown;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (lobbyCountdown != null) {
            lobbyCountdown.startCountdown();
        } else {
            // Handle case where lobbyCountdown is not initialized
            event.getPlayer().sendMessage("Lobby countdown is not initialized.");
        }
    }
}

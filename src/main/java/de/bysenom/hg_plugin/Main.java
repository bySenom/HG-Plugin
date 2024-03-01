package de.bysenom.hg_plugin;

import de.bysenom.hg_plugin.commands.Fly;
import de.bysenom.hg_plugin.handlers.LobbyCountdown;
import de.bysenom.hg_plugin.handlers.PlayerJoinListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements CommandExecutor, Listener {

    private LobbyCountdown lobbyCountdown;

    @Override
    public void onEnable() {
        // Initialize LobbyCountdown
        LobbyCountdown lobbyCountdown = new LobbyCountdown();


        PlayerJoinListener playerJoinListener = new PlayerJoinListener(lobbyCountdown);
        getServer().getPluginManager().registerEvents(playerJoinListener, this);







        getCommand("fly").setExecutor(new Fly());
        getLogger().info("Plugin erfolgreich geladen!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin erfolgreich beendet!");
    }
}




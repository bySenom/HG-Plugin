package de.bysenom.hg_plugin;

import de.bysenom.hg_plugin.commands.Fly;
import de.bysenom.hg_plugin.commands.HGJoin;
import de.bysenom.hg_plugin.commands.HGStart;
import de.bysenom.hg_plugin.handlers.LobbyCountdown;
import de.bysenom.hg_plugin.handlers.BuildHandler;
import de.bysenom.hg_plugin.mechanics.Lobby;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements CommandExecutor, Listener {

    private LobbyCountdown lobbyCountdown;
    private Lobby lobby;


    @Override
    public void onEnable() {

        // Initialize LobbyCountdown and Lobby
        HGStart hgStartCommand = new HGStart(lobbyCountdown);
        LobbyCountdown lobbyCountdown = new LobbyCountdown(this);
        lobby = new Lobby(lobbyCountdown);





        // Initialize LobbyCountdown
        lobbyCountdown = new LobbyCountdown(this);

        HGJoin hgJoin = new HGJoin(lobbyCountdown);

        getServer().getPluginManager().registerEvents(new BuildHandler(), this);

        getCommand("hgstart").setExecutor(hgStartCommand);
        getCommand("hgjoin").setExecutor(hgJoin);
        getCommand("fly").setExecutor(new Fly());
        getLogger().info("Plugin erfolgreich geladen!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin erfolgreich beendet!");
    }
}




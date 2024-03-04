package de.bysenom.hg_plugin;

import de.bysenom.hg_plugin.commands.Fly;
import de.bysenom.hg_plugin.commands.HGJoin;
import de.bysenom.hg_plugin.commands.HGStart;
import de.bysenom.hg_plugin.handlers.ItemHandler;
import de.bysenom.hg_plugin.handlers.KitHandler;
import de.bysenom.hg_plugin.handlers.LobbyCountdown;
import de.bysenom.hg_plugin.handlers.BuildHandler;
import de.bysenom.hg_plugin.kits.Anchor;
import de.bysenom.hg_plugin.kits.Ninja;
import de.bysenom.hg_plugin.mechanics.Lobby;
import de.bysenom.hg_plugin.mechanics.SoupHealing;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements CommandExecutor, Listener {

    private LobbyCountdown lobbyCountdown;
    private Lobby lobby;


    public void onEnable() {
        // Initialize LobbyCountdown and Lobby
        LobbyCountdown lobbyCountdown = new LobbyCountdown(this);
        lobby = new Lobby(lobbyCountdown);
        ItemHandler itemHandler = new ItemHandler(this);
        HGStart hgStartCommand = new HGStart(lobbyCountdown);
        HGJoin hgJoin = new HGJoin(this, lobbyCountdown);
        getServer().getPluginManager().registerEvents(new ItemHandler(this), this);
        // Register your event listener

        getServer().getPluginManager().registerEvents(new BuildHandler(), this);
        getServer().getPluginManager().registerEvents(new SoupHealing(), this);
        getServer().getPluginManager().registerEvents(new Anchor(this), this);
        getServer().getPluginManager().registerEvents(new Ninja(), this);

        getCommand("hgstart").setExecutor(hgStartCommand);
        getCommand("hgjoin").setExecutor(hgJoin);
        getCommand("fly").setExecutor(new Fly());
        getLogger().info("Plugin erfolgreich geladen!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin erfolgreich beendet!");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer(); // Get the player involved in the event
        ItemStack item = event.getItem();
        if (item != null) {
            if (KitHandler.isAnchor(item, player)) {
                // Handle anchor item
                if (KitHandler.isAnchorEnabled(player)) {
                    // Anchor is enabled for this player
                } else {
                    // Anchor is not enabled for this player
                }
            } else if (KitHandler.isNinja(item, player)) {
                // Handle ninja item
                if (KitHandler.isNinjaEnabled(player)) {
                    // Ninja is enabled for this player
                } else {
                    // Ninja is not enabled for this player
                }
            }
        }
    }
}




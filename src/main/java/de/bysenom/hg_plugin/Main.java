package de.bysenom.hg_plugin;

import de.bysenom.hg_plugin.commands.SaveCommand;
import me.clip.placeholderapi.PlaceholderAPI;

import de.bysenom.hg_plugin.commands.Fly;
import de.bysenom.hg_plugin.commands.HGJoin;
import de.bysenom.hg_plugin.commands.HGStart;
import de.bysenom.hg_plugin.handlers.*;
import de.bysenom.hg_plugin.kits.Anchor;
import de.bysenom.hg_plugin.kits.Ninja;
import de.bysenom.hg_plugin.mechanics.Lobby;
import de.bysenom.hg_plugin.mechanics.SoupHealing;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Main extends JavaPlugin implements CommandExecutor, Listener {

    private LobbyCountdown lobbyCountdown;
    private Lobby lobby;
    private ScoreBoardHandler scoreBoardHandler;
    private WorldManager worldManager;
    private static Main instance;


    public void onEnable() {
        // Assign the current instance to the static variable 'instance'
        instance = this;

        try {
            StatisticHandler statisticHandler = new StatisticHandler(this); // Replace "your-plugin-name" with your actual plugin name
            System.out.println("Statistic folder and file created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to create statistic folder and file!");
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            /*
             * We inform about the fact that PlaceholderAPI isn't installed and then
             * disable this plugin to prevent issues.
             */
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
            return; // Ensure the plugin is disabled before proceeding further
        }

        // Initialize LobbyCountdown and Lobby
        Main mainInstance = Main.getInstance();
        LobbyCountdown lobbyCountdown = new LobbyCountdown(this);
        worldManager = new WorldManager(this); // Initialize worldManager after LobbyCountdown creation
        worldManager.load("world", "HG-Plugin/saves"); // Load world after initializing worldManager
        scoreBoardHandler = new ScoreBoardHandler();
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


        // No need to register ScoreBoardHandler as an event listener
        // getServer().getPluginManager().registerEvents(new ScoreBoardHandler(), this);

        getCommand("hgstart").setExecutor(hgStartCommand);
        getCommand("hgjoin").setExecutor(hgJoin);
        getCommand("fly").setExecutor(new Fly());
        getLogger().info("Plugin erfolgreich geladen!");
        getCommand("hgsaveworld").setExecutor(new SaveCommand(worldManager));
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin erfolgreich beendet!");
    }

    // Getter method for WorldManager if needed
    public WorldManager getWorldManager() {
        return worldManager;
    }

    public static Main getInstance() {
        return instance;
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




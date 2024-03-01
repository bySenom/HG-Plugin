package de.bysenom.hg_plugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin {

    private PlayerScoreboard playerScoreboard;

    @Override
    public void onEnable() {
        playerScoreboard = new PlayerScoreboard(this);

        // Schedule initial update after a slight delay
        new BukkitRunnable() {
            @Override
            public void run() {
                playerScoreboard.updatePlayerList();
            }
        }.runTaskLater(this, 20L); // Delay by 1 tick (20 milliseconds)

        // Schedule score updates every 20 ticks (1 second)
        getServer().getScheduler().runTaskTimer(this, () -> playerScoreboard.updatePlayerList(), 20L, 20L);

        getLogger().info("Plugin erfolgreich geladen!");
        getServer().broadcastMessage("Plugin erfolgreich geladen!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin erfolgreich beendet!");
        getServer().broadcastMessage("Plugin erfolgreich beendet!");
    }
}


package de.bysenom.hg_plugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    private PlayerScoreboard playerScoreboard;

    @Override
    public void onEnable() {

        playerScoreboard = new PlayerScoreboard(this);
        getServer().getScheduler().runTaskTimer(this, () -> playerScoreboard.updatePlayerList(), 20L, 20L); // Aktualisiere das Scoreboard alle 20 Ticks (1 Sekunde)

        getLogger().info("Das Plugin wurde erfolgreich geladen!"); // Nachricht in der Konsole anzeigen
        getServer().broadcastMessage("Das Plugin wurde erfolgreich geladen!"); // Nachricht im Chat anzeigen
    }

    @Override
    public void onDisable() {
        getLogger().info("Das Plugin wurde erfolgreich beendet!"); // Nachricht in der Konsole anzeigen
        getServer().broadcastMessage("Das Plugin wurde erfolgreich beendet!"); // Nachricht im Chat anzeigen
    }
}

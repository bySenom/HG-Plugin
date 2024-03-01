package de.bysenom.hg_plugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Das Plugin wurde erfolgreich geladen!"); // Nachricht in der Konsole anzeigen
        getServer().broadcastMessage("Das Plugin wurde erfolgreich geladen!"); // Nachricht im Chat anzeigen
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

package de.bysenom.hg_plugin.handlers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class StatisticHandler {

    private final String pluginName;
    private final File statisticFile;

    private final FileConfiguration config;

    public StatisticHandler(JavaPlugin plugin) throws IOException {
        this.pluginName = plugin.getDescription().getName();
        this.statisticFile = new File(plugin.getDataFolder(), "statistic.yml");
        this.config = YamlConfiguration.loadConfiguration(statisticFile); // Load default config here
        createStatisticsFolder();
    }

    public void createStatisticsFolder() throws IOException {
        File pluginFolder = getDataFolder();
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs(); // Creates the plugin folder if it doesn't exist
        }

        File statisticFile = new File(pluginFolder, "statistic.yml");
        if (!statisticFile.exists()) {
            statisticFile.createNewFile(); // Creates the statistic.yml file if it doesn't exist
        }
    }

    public void savePlayerData(Player player, int data, String kills) throws IOException {
        File playerFile = getPlayerFile(player);
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
        config.set("data", data);
        config.save(playerFile); // Attempt save with error handling
    }

    public int loadPlayerData(Player player, String kills) {
        // Load default value here to avoid potential errors
        int data = 0;
        File playerFile = getPlayerFile(player);
        if (playerFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
            data = config.getInt("data");
        }
        return data;
    }

    private File getPlayerFile(Player player) {
        return new File(statisticFile, player.getUniqueId() + ".yml");
    }

    private File getDataFolder() {
        // Replace "your-plugin-name" with your actual plugin name
        return new File("plugins/" + "HG-Plugin");
    }
}


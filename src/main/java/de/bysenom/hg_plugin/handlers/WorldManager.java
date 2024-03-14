package de.bysenom.hg_plugin.handlers;

import de.bysenom.hg_plugin.Main;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.File;

public class WorldManager {
    private final Main main;

    public WorldManager(Main main) {
        this.main = main;
    }

    public void save(String playerName) {
        if (main == null) {
            // Handle the case where Main instance is null
            return;
        }

        // Get the player object from the player name
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) {
            // Player is offline or doesn't exist
            main.getLogger().warning("Player " + playerName + " is not online or doesn't exist.");
            return;
        }

        // Get the world the player is currently in
        World world = player.getWorld();
        if (world == null) {
            main.getLogger().warning("Player " + playerName + " is not in any world.");
            return;
        }

        // Get the world's folder path
        String worldFolder = world.getWorldFolder().getPath();

        // Define the destination folder path with the name of the world
        String destinationPath = "plugins/HG-Plugin/saves/" + world.getName();

        // Create the destination folder if it doesn't exist
        File folder = new File(destinationPath);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (!created) {
                main.getLogger().warning("Failed to create directory: " + folder.getAbsolutePath());
                return;
            } else {
                main.getLogger().info("Directory created: " + folder.getAbsolutePath());
            }
        } else {
            main.getLogger().info("Destination directory already exists: " + folder.getAbsolutePath());
        }

        try {
            // Copy the world files to the destination folder
            FileUtils.copyDirectory(new File(worldFolder), folder);
            main.getLogger().info("World files copied successfully for player " + playerName);
        } catch (IOException e) {
            main.getLogger().warning("Failed to copy world files for player " + playerName);
            e.printStackTrace();
        }
    }

    public void load(String name, String path) {
        if (main == null) {
            // Handle the case where Main instance is null
            return;
        }

        Bukkit.getServer().unloadWorld(name, true);
        File regionFolder = new File(path + "/region");
        if (regionFolder.exists()) {
            File[] regionFiles = regionFolder.listFiles();
            if (regionFiles != null) {
                for (File f : regionFiles) {
                    f.delete();
                }
            }
        }

        File savesFolder = new File(main.getDataFolder() + "/saves");
        if (savesFolder.exists()) {
            File[] saveFiles = savesFolder.listFiles();
            if (saveFiles != null) {
                for (File f : saveFiles) {
                    try {
                        FileUtils.copyFileToDirectory(f, new File(path + "/region/"));
                        Bukkit.getConsoleSender().sendMessage("[HG-BlackLotus] Copied §e" + f.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        new WorldCreator(name).createWorld();
    }
}


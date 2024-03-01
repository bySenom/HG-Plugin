package de.bysenom.hg_plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class Main extends JavaPlugin implements CommandExecutor, Listener {

    private static final Integer COUNTDOWN_TIME = 60;
    Map<Player, Integer> countdowns = new HashMap<>();
    private PlayerScoreboard playerScoreboard;
    private final int cooldownTime = 60; // Cooldown time in seconds
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        cooldownManager = new CooldownManager();

        // Get the scoreboard manager
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

        // Initialize player scoreboard
        playerScoreboard = new PlayerScoreboard(this, scoreboardManager);

        Command hgCommand = getCommand("hg");
        if (hgCommand != null) {
            ((PluginCommand) hgCommand).setExecutor(this);
        } else {
            getLogger().warning("Command 'hg' not found. Please register it in your plugin.yml!");
        }

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
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("hg")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("join")) {
                countdowns.put(player, COUNTDOWN_TIME);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!countdowns.containsKey(player)) {
                            return; // Player finished or left
                        }

                        int remainingTime = countdowns.get(player);
                        remainingTime--;
                        countdowns.put(player, remainingTime);

                        // Update player display with remaining time

                        if (remainingTime <= 0) {
                            countdowns.remove(player);

                            // Perform actions for end of countdown
                        }
                    }
                }.runTaskTimer(this, 0L, 20L); // Update every second

                return true;
            }
        }

        return false;
    }

    private void updateXPBar(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                int remainingCooldown = cooldownManager.getRemainingCooldown(player);
                float progress = (float) remainingCooldown / cooldownTime;
                int xpBarLevel = (int) (progress * 20); // Maximum XP bar level is 20

                player.setLevel(xpBarLevel);
            }
        }.runTaskTimer(this, 0L, 20L); // Update XP bar every second
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin erfolgreich beendet!");
    }
}




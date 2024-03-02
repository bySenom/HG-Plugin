package de.bysenom.hg_plugin.handlers;

import de.bysenom.hg_plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyCountdown {

    private final int countdownTime = 60; // Countdown time in seconds
    private final Plugin plugin;

    public LobbyCountdown(Plugin plugin) {
        this.plugin = plugin;
    }

    public void startCountdown() {
        // Get all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            startPlayerCountdown(player);
        }
    }

    private void startPlayerCountdown(Player player) {
        new BukkitRunnable() {
            int remainingTime = countdownTime;

            @Override
            public void run() {
                // Update XP bar to show countdown progress
                updateXPBar(player, remainingTime);

                if (remainingTime <= 0) {
                    // Countdown finished, cancel the task
                    cancel();
                } else {
                    // Decrease remaining time
                    remainingTime--;
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Update XP bar every second
    }

    private void updateXPBar(Player player, int remainingTime) {
        float progress = (float) remainingTime / countdownTime;
        int xpBarLevel = (int) (progress * 20); // Maximum XP bar level is 20
        player.setLevel(xpBarLevel);
    }
}


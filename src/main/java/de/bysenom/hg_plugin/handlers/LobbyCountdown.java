package de.bysenom.hg_plugin.handlers;

import de.bysenom.hg_plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyCountdown {

    private final int countdownTime = 60; // Countdown-Zeit in Sekunden
    private final Plugin plugin;
    private boolean countdownRunning = false;
    private BukkitRunnable countdownTask;

    public LobbyCountdown(Plugin plugin) {
        this.plugin = plugin;
    }

    // Starte den Countdown für alle Spieler
    public void startCountdownForAllPlayers() {
        if (!countdownRunning) {
            // Starte den Countdown mit voller verbleibender Zeit für alle Spieler
            startCountdown(countdownTime);
            countdownRunning = true;
        }
    }

    // Starte den Countdown mit einer spezifischen verbleibenden Zeit
    public void startCountdown(int remainingTime) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            startPlayerCountdown(player, remainingTime);
        }
    }

    private void startPlayerCountdown(Player player, int remainingTime) {
        countdownTask = new BukkitRunnable() {
            int timeLeft = remainingTime;

            @Override
            public void run() {
                // Update XP-Bar, um den Countdown-Fortschritt anzuzeigen
                updateXPBar(timeLeft);

                // Erzeuge die Nachricht basierend auf der verbleibenden Zeit
                String message = getMessageForTimeLeft(timeLeft);

                // Spiele Soundeffekte für die letzten 5 Sekunden
                if (timeLeft <= 5 && timeLeft > 0) {
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0f, 1.0f);

                    // Sende einen grünen Titel
                    player.sendTitle(ChatColor.GREEN + message, "", 10, 20, 10);
                }

                // Überprüfe, ob die Zeit abgelaufen ist
                if (timeLeft == 0) {
                    // Spiele den Sound für das Level-Up ab
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1.0f, 1.0f);
                    // Breche die Aufgabe ab
                    cancel();
                }

                // Verringere die verbleibende Zeit
                timeLeft--;
            }

            private String getMessageForTimeLeft(int timeLeft) {
                switch (timeLeft) {
                    case 5:
                        return "5";
                    case 4:
                        return "4";
                    case 3:
                        return "3";
                    case 2:
                        return "2";
                    case 1:
                        return "1";
                    case 0:
                        player.sendTitle(ChatColor.GREEN + "GO!", "", 20, 40, 20); // Sende einen größeren Titel für "GO!"
                        return "GO!";
                    default:
                        return "";
                }
            }
        };
        countdownTask.runTaskTimer(plugin, 0L, 20L); // Aktualisiere die XP-Bar alle Sekunde
    }

    // Aktualisiere die XP-Bar aller Spieler basierend auf der verbleibenden Zeit
    private void updateXPBar(int remainingTime) {
        float progress = (float) remainingTime / countdownTime;
        int xpBarLevel = (int) (progress * 60); // Maximale XP-Bar-Stufe beträgt 60
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setLevel(xpBarLevel);
        }
    }

    // Stopp den laufenden Countdown
    public void stopCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }
        countdownRunning = false;
    }
}


package de.bysenom.hg_plugin.mechanics;

import de.bysenom.hg_plugin.handlers.InvincibilityHandler;
import de.bysenom.hg_plugin.handlers.ScoreBoardHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer {

    private static Plugin plugin;
    private static boolean countdownRunning = false;
    private static BukkitRunnable countdownTask;
    private int countdownTime = 3600;

    public GameTimer(Plugin plugin) {
        this.plugin = plugin;
    }

    // Start the countdown for all players
    public void startCountdownForAllPlayers() {
        if (!countdownRunning) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                gameCountdown(countdownTime, player);
            }
            countdownRunning = true;
        } else {
            Bukkit.broadcastMessage(ChatColor.RED + "The countdown is already active!");
        }
    }

    // Start the countdown with a specific remaining time
    public static void gameCountdown(int remainingTimeTwo, Player player) {
        ScoreBoardHandler scoreBoardHandler = new ScoreBoardHandler(); // Create an instance of ScoreBoardHandler

        // Delete the existing scoreboard
        scoreBoardHandler.deleteScoreboard(player);

        // Display the scoreboard
        scoreBoardHandler.displayScoreboard(player);
        countdownTask = new BukkitRunnable() {
            int protectionTimeLeft = remainingTimeTwo;

            @Override
            public void run() {
                if (protectionTimeLeft == 3600) {

                    player.sendMessage(ChatColor.AQUA + "[BlackLotus] " + ChatColor.RED + "Schutzzeit endet in " + ChatColor.GREEN + "60 " + ChatColor.RED + "Sekunden!");

                    // Apply invincibility to all players at the start of the countdown
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        InvincibilityHandler.makeInvincible(player);
                    }
                }

                if (protectionTimeLeft == 3570) {

                    player.sendMessage(ChatColor.AQUA + "[BlackLotus] " + ChatColor.RED + "Schutzzeit endet in " + ChatColor.GREEN + "30 " + ChatColor.RED + "Sekunden!");

                }

                // Schutzzeit Announcer
                if (protectionTimeLeft == 3550) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage(ChatColor.AQUA + "[BlackLotus] " + ChatColor.RED + "Schutzzeit endet in " + ChatColor.GREEN + "10 " + ChatColor.RED + "Sekunden!");
                    }
                }

                boolean messageSent = false;

                if (protectionTimeLeft <= 3545 && protectionTimeLeft > 3540 && !messageSent) {
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0f, 1.0f);

                    // Send a chat message with the server name in a different color
                    String message = getMessageForProtectionTimeLeft(protectionTimeLeft);
                    if (!message.isEmpty()) {
                        player.sendMessage(ChatColor.AQUA + "[BlackLotus] " + ChatColor.RED + "Schutzzeit endet in " + ChatColor.GREEN + message + ChatColor.RED + " Sekunden!");
                    }

                    // Set the messageSent variable to true to indicate that the message has been sent
                    messageSent = true;
                }

                // Remove invincibility from all players after 1 minutes
                if (protectionTimeLeft == 3540) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        InvincibilityHandler.removeInvincibility(player);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                        player.sendMessage(ChatColor.AQUA + "[BlackLotus] " + ChatColor.RED + "Die Schutzzeit ist beendet!");
                    }
                }

                if (protectionTimeLeft == 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                    }
                    cancel();
                    countdownRunning = false;
                }

                protectionTimeLeft--;
            }
            private String getMessageForProtectionTimeLeft(int protectionTimeLeft) {
                switch (protectionTimeLeft) {
                    case 3545:
                        return "5";
                    case 3544:
                        return "4";
                    case 3543:
                        return "3";
                    case 3542:
                        return "2";
                    case 3541:
                        return "1";
                    case 3540:
                        return "1";
                    default:
                        return "";
                }
            }
        };
        countdownTask.runTaskTimer(plugin, 0L, 20L); // 20 ticks = 1 second
    }

    // Stop the running countdown
    public void stopCountdown() {
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTask = null;
        }
        countdownRunning = false;
    }
}


package de.bysenom.hg_plugin.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class LobbyCountdown {

    private int countdownTime = 60; // Countdown-Zeit in Sekunden
    private final Plugin plugin;
    private boolean countdownRunning = false;
    private BukkitRunnable countdownTask;
    private ScoreBoardHandler scoreBoardHandler;




    public LobbyCountdown(Plugin plugin) {
        this.plugin = plugin;
        this.scoreBoardHandler = new ScoreBoardHandler();
    }

    // Starte den Countdown für alle Spieler
    public void startCountdownForAllPlayers() {
        if (!countdownRunning) {
            // Starte den Countdown mit voller verbleibender Zeit für alle Spieler
            startCountdown(countdownTime);
            countdownRunning = true;
        } else {
            // Wenn der Countdown läuft, sende eine Nachricht, dass der Countdown bereits aktiv ist
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "[BlackLotus] " + ChatColor.RED + "Der Countdown läuft bereits!");
        }
    }

    // Starte den Countdown mit einer spezifischen verbleibenden Zeit
    public void startCountdown(int remainingTime) {
        startCountdownWithTime(remainingTime, remainingTime); // Call the method with remainingTime as both arguments

    }

    public void startCountdownWithTime(int remainingTime, int newTime) {

        for (Player player : Bukkit.getOnlinePlayers()) {
            startPlayerCountdown(player, remainingTime, newTime); // Pass remainingTime and newTime as arguments
        }
    }

    // Ändere die Countdown-Zeit
    public void changeCountdownTime(int newTime) {
        if (!countdownRunning) {
            countdownTime = newTime;
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Countdown-Zeit wurde auf " + newTime + " Sekunden geändert.");
        } else {
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "[BlackLotus] " + ChatColor.RED + "Der Countdown läuft bereits!");
        }
    }

    private void startPlayerCountdown(Player player, int remainingTime, int newTime) {
        TeleportHandler.initializeLocations();
        TeleportHandler.teleportToLocation(player, "LobbySpawn");

        if (scoreBoardHandler == null) {
            scoreBoardHandler = new ScoreBoardHandler();
        }

        // Delete the existing scoreboard
        ScoreBoardHandler.deleteScoreboard();

        // Display the scoreboard
        scoreBoardHandler.displayScoreboard();

        UUID playerUUID = player.getUniqueId();
        ItemHandler itemHandler = new ItemHandler((JavaPlugin) plugin);  // Assuming 'plugin' is your JavaPlugin instance
        ItemStack playerHeadItem = itemHandler.createPlayerHead(playerUUID);
        player.getInventory().setItem(8, playerHeadItem);  // Place the player head item in the last slot of the hotbar


        ItemStack kitsChestItem = itemHandler.createKitsChest(player.getUniqueId());
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.clear(0); // Clear the first slot of the hotbar
        playerInventory.setItem(0, kitsChestItem); // Set the kits chest item in the first slot

        countdownTask = new BukkitRunnable() {
            int timeLeft = remainingTime;


            @Override
            public void run() {
                ScoreBoardHandler scoreBoardHandler = new ScoreBoardHandler();
                scoreBoardHandler.updateScoreboard();
                BuildHandler.disallowBlockPlacing();
                BuildHandler.disallowBlockBreaking();
                HungerHandler.disableHunger(player);
                //MovementHandler.disableMovement(player);
                InvincibilityHandler.makeInvincible(player);
                // Update XP-Bar, um den Countdown-Fortschritt anzuzeigen
                updateXPBar(timeLeft);

                // Erzeuge die Nachricht basierend auf der verbleibenden Zeit
                String message = getMessageForTimeLeft(timeLeft);

                boolean messageSent = false;

                if (timeLeft <= 5 && timeLeft > 0 && !messageSent) {
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0f, 1.0f);

                    // Sende einen grünen Titel
                    player.sendTitle(ChatColor.GREEN + message, "", 10, 20, 10);

                    // Send a chat message with the server name in a different color
                    player.sendMessage(ChatColor.AQUA + "[BlackLotus] " + ChatColor.GREEN + "Spiel startet in: " + timeLeft);

                    // Set the messageSent variable to true to indicate that the message has been sent
                    messageSent = true;
                }

                // Überprüfe, ob die Zeit abgelaufen ist
                if (timeLeft == 0) {
                    // Spiele den Sound für das Level-Up ab
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1.0f, 1.0f);
                    // Breche die Aufgabe ab
                    cancel();
                    countdownRunning = false; // Setze countdownRunning auf false, um den Countdown neu zu starten

                    BuildHandler.allowBlockBreaking();
                    BuildHandler.allowBlockPlacing();
                    HungerHandler.enableHunger(player);
                    InvincibilityHandler.removeInvincibility(player);
                    //MovementHandler.enableMovement(player);
                    Inventory playerInventory = player.getInventory();
                    InvClearHandler.clearInventory(playerInventory);
                    ScoreBoardHandler.deleteScoreboard();
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


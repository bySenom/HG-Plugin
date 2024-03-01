package de.bysenom.hg_plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public class PlayerScoreboard implements Listener {

    private final Scoreboard scoreboard;

    public PlayerScoreboard(JavaPlugin plugin, ScoreboardManager scoreboardManager) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        // Initialize scoreboard immediately
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("PlayerList", "dummy");
        objective.setDisplayName("Spieler");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Update the scoreboard only when it's ready
        if (scoreboard != null) {
            updatePlayerList();
            player.setScoreboard(scoreboard); // Set the scoreboard for the player
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) { // Handle player leaving
        Player player = event.getPlayer();
        if (scoreboard != null) {
            Objects.requireNonNull(scoreboard.getTeam(player.getName())).removeEntry(player.getName()); // Remove from team
        }
    }

    public void updatePlayerList() {
        if (scoreboard == null) {
            return; // Abbruch, wenn das Scoreboard noch nicht initialisiert ist
        }

        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective != null) {
            objective.unregister(); // Unregister unnecessary previous objective
        }

        // Create a new objective using the recommended method
        Objective newObjective = scoreboard.registerNewObjective("PlayerList", Criterias.DEATHS, "Spieler");
        newObjective.setDisplaySlot(DisplaySlot.SIDEBAR); // No need to set display name separately

        for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
            Team team = scoreboard.getTeam(onlinePlayer.getName());
            if (team == null) {
                team = scoreboard.registerNewTeam(onlinePlayer.getName());
            }
            team.addEntry(onlinePlayer.getName());
            newObjective.getScore(onlinePlayer.getName()).setScore(Bukkit.getServer().getOnlinePlayers().size());
        }
    }
}


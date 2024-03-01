package de.bysenom.hg_plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerScoreboard implements Listener {

    private Scoreboard scoreboard;

    public PlayerScoreboard(JavaPlugin plugin) {
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
            updatePlayerList(player);
            player.setScoreboard(scoreboard); // Set the scoreboard for the player
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) { // Handle player leaving
        Player player = event.getPlayer();
        if (scoreboard != null) {
            scoreboard.getTeam(player.getName()).removeEntry(player.getName()); // Remove from team
        }
    }

    public void updatePlayerList(Player player) {
        if (scoreboard == null) {
            return; // Abbruch, wenn das Scoreboard noch nicht initialisiert ist
        }

        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective != null) {
            objective.unregister(); // Unregister unnecessary previous objective
        }

        Objective newObjective = scoreboard.registerNewObjective("PlayerList", "dummy");
        newObjective.setDisplayName("Spieler");
        newObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

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


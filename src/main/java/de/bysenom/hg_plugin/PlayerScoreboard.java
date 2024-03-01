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

        // Initialize scoreboard using the provided scoreboard manager
        this.scoreboard = scoreboardManager != null ? scoreboardManager.getNewScoreboard() : null;

        if (scoreboard == null) {
            plugin.getLogger().warning("Failed to create scoreboard. Scoreboard features might not be available.");
        } else {
            // Initialize the scoreboard (consider moving this to a separate method for clarity)
            Objective objective = scoreboard.registerNewObjective("PlayerList", Criterias.DEATHS, "Spieler");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
                Team team = scoreboard.getTeam(onlinePlayer.getName());
                if (team == null) {
                    team = scoreboard.registerNewTeam(onlinePlayer.getName());
                }
                team.addEntry(onlinePlayer.getName());
                objective.getScore(onlinePlayer.getName()).setScore(Bukkit.getServer().getOnlinePlayers().size());
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Update the scoreboard only when it's ready and the player has a valid entry
        if (scoreboard != null && scoreboard.getEntryTeam(player.getName()) != null) {
            updatePlayerList();
            player.setScoreboard(scoreboard);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (scoreboard != null) {
            Objects.requireNonNull(scoreboard.getTeam(player.getName())).removeEntry(player.getName());
        }
    }

    public void updatePlayerList() {
        if (scoreboard == null) {
            return;
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


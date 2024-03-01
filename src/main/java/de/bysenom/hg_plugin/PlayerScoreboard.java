package de.bysenom.hg_plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerScoreboard implements Listener {

    private Scoreboard scoreboard;

    public PlayerScoreboard(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents((Listener) this, plugin); // Registriere den Listener für Events
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
            scoreboard = scoreboardManager.getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("PlayerList", "dummy", "Spieler");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            updatePlayerList();
        }, 20L); // Verzögere die Initialisierung um 1 Sekunde (20 Ticks)
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayerList(); // Rufe updatePlayerList() auf, wenn ein Spieler beitritt
    }

    public void updatePlayerList() {
        if (scoreboard == null) {
            return; // Abbruch, wenn das Scoreboard noch nicht initialisiert ist
        }

        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        if (objective != null) {
            objective.unregister();
        }

        Objective newObjective = scoreboard.registerNewObjective("PlayerList", "dummy", "Spieler");
        newObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int playerCount = Bukkit.getServer().getOnlinePlayers().size();
        newObjective.getScore("Spieler:").setScore(playerCount);

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Team team = scoreboard.getTeam(player.getName());
            if (team == null) {
                team = scoreboard.registerNewTeam(player.getName());
            }
            team.addEntry(player.getName());
        }

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            scoreboard.getObjective(DisplaySlot.SIDEBAR).getScore(player.getName()).setScore(playerCount);
        }
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
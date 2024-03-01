package de.bysenom.hg_plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class PlayerScoreboard {

    private Scoreboard scoreboard;

    public PlayerScoreboard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("PlayerList", "dummy", "Spieler");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        updatePlayerList();
    }

    public void updatePlayerList() {
        Objective objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        objective.unregister();

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


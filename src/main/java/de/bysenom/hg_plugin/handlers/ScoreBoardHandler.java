package de.bysenom.hg_plugin.handlers;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreBoardHandler {
    private Scoreboard scoreboard;

    private Objective objective; // Declare the objective variable


    public void displayScoreboard() {
        // Replace "yourServerName" with your actual server name
        String serverName = "§lBLACKLOTUS.NET"; // Prepend '§l' for bold text
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        // Correct: using the correct variable name "objective" with a space
        Objective existingObjective = board.registerNewObjective("sb", "dummy", " " + serverName);

        // Either retrieve the existing objective or create a new one
        objective = board.getObjective("sb");
        if (objective == null) {
            // If objective doesn't exist, create a new one
            objective = board.registerNewObjective("sb", "dummy", " " + serverName);
            // ... (remaining code to set scoreboard lines)
        }

        Objective objective = board.getObjective("sb"); // Check if an objective with the name 'sb' already exists
        if (objective == null) {
            // If it doesn't exist, register a new one
            objective = board.registerNewObjective("sb", "dummy", " " + serverName); // Use the bold server name string directly
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            // Scoreboard lines
            objective.getScore(" ").setScore(10); // Blank line for spacing
            objective.getScore("       ").setScore(9); // Blank line for spacing
            objective.getScore(" ").setScore(8); // Blank line for spacing
            objective.getScore("").setScore(7); // Blank line
            objective.getScore(" ").setScore(6); // Blank line for spacing
            objective.getScore("§7Kills:").setScore(5); // Line with text "KILLS:"
            objective.getScore("§E%KILLANZAHL%").setScore(4); // Line with placeholder for kill count
            objective.getScore(" ").setScore(3); // Blank line for spacing
            objective.getScore("").setScore(2); // Blank line
            objective.getScore(" ").setScore(1); // Blank line for spacing
            objective.getScore("§7Players:").setScore(0); // Line with text "Players:"
            objective.getScore("§7Players: " + PlaceholderAPI.setPlaceholders(null, "%online_players%")).setScore(-1);
            objective.getScore(" ").setScore(-2); // Blank line for spacing
            objective.getScore("§7Kit:").setScore(-3); // Line with text "Kit:"
            objective.getScore("§E%kit%").setScore(-4); // Line with placeholder for kit name
            // Update player count using the objective
            updatePlayerCount(objective, Bukkit.getServer().getOnlinePlayers().size());
        }
    }

    public static void deleteScoreboard() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = board.getObjective("sb");
        if (objective != null) {
            objective.unregister();
        }
    }

    public ScoreBoardHandler() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = scoreboardManager.getMainScoreboard();
    }

    public void updatePlayerCount(Objective existingObjective, int playerCount) {
        // Use the passed objective here
        existingObjective.getScore("§7Players: " + PlaceholderAPI.setPlaceholders(null, "%online_players%")).setScore(playerCount);
    }
}
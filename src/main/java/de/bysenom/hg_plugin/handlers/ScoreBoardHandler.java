package de.bysenom.hg_plugin.handlers;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

public class ScoreBoardHandler {
    private Scoreboard scoreboard;
    private static int lastPlayerCount = -1;


    public void displayScoreboard() {
        // Replace "yourServerName" with your actual server name
        String serverName = "§lBLACKLOTUS.NET"; // Prepend '§l' for bold text
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        // Check if an objective with the name 'sb' already exists
        Objective objective = board.getObjective("sb");
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

            // Update player count using the objective
            int playerCount = Bukkit.getServer().getOnlinePlayers().size();
            objective.getScore(String.valueOf(playerCount)).setScore(-1);
            objective.getScore("                ").setScore(-2); // Line with text "Players:"
            objective.getScore("§7Kits:").setScore(-3); // Line with text "Players:"
            objective.getScore("§E%KIT%").setScore(-4); // Line with text "Players:"

            // Additional debugging messages
            Bukkit.getLogger().info("Objective 'sb' created and populated.");
        } else {
            // Debug message when the objective is already existing
            Bukkit.getLogger().info("Objective 'sb' already exists. Skipping scoreboard creation.");
            // Update player count using the existing objective
            int playerCount = Bukkit.getServer().getOnlinePlayers().size();
            objective.getScore(PlaceholderAPI.setPlaceholders(null, "%online_players")).setScore(playerCount);
        }
    }

    public static void updateScoreboard() {
        // Get the scoreboard and the 'sb' objective
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = board.getObjective("sb");

        // Check if the 'sb' objective exists
        if (objective != null) {
            // Get the current player count
            int playerCount = Bukkit.getServer().getOnlinePlayers().size();

            // Only update the scoreboard if the player count has changed
            if (playerCount != lastPlayerCount) {
                // Remove the previous score entry for player count
                removePreviousPlayerCountScore(board, objective);

                // Update player count using the objective
                objective.getScore(String.valueOf(playerCount)).setScore(-1);

                // Update the last player count
                lastPlayerCount = playerCount;

                // Additional debugging message
                Bukkit.getLogger().info("Scoreboard updated with player count: " + playerCount);
            }
        } else {
            // Debug message if the 'sb' objective doesn't exist
            Bukkit.getLogger().warning("Unable to update scoreboard. Objective 'sb' does not exist.");
        }
    }

    private static void removePreviousPlayerCountScore(Scoreboard board, Objective objective) {
        for (String entry : board.getEntries()) {
            Score score = objective.getScore(entry);
            if (score.isScoreSet() && score.getScore() == -1) {
                // Remove the previous score entry for player count
                board.resetScores(entry);
                break;
            }
        }
    }


    public static void deleteScoreboard() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = board.getObjective("sb");
        if (objective != null) {
            objective.unregister(); // Unregister the objective
        }
    }

    public ScoreBoardHandler() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        this.scoreboard = scoreboardManager.getMainScoreboard();
    }
}
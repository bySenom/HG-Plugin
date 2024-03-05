package de.bysenom.hg_plugin.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.DisplaySlot;

public class ScoreBoardHandler {

    public void displayScoreboard() {
        // Replace "yourServerName" with your actual server name
        String serverName = "§lBLACKLOTUS.NET"; // Prepend '§l' for bold text
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
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
            objective.getScore("§73. KILLS:").setScore(5); // Line with text "KILLS:"
            objective.getScore("§E4. %KILLANZAHL%").setScore(4); // Line with placeholder for kill count
            objective.getScore(" ").setScore(3); // Blank line for spacing
            objective.getScore("").setScore(2); // Blank line
            objective.getScore(" ").setScore(1); // Blank line for spacing
            objective.getScore("§76. Players:").setScore(0); // Line with text "Players:"
            objective.getScore("§E7. %Playercount%").setScore(-1); // Line with placeholder for player count
            objective.getScore(" ").setScore(-2); // Blank line for spacing
            objective.getScore("§78. Kit:").setScore(-3); // Line with text "Kit:"
            objective.getScore("§E9. %kit%").setScore(-4); // Line with placeholder for kit name
        }
    }

    public static void deleteScoreboard() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = board.getObjective("sb");
        if (objective != null) {
            objective.unregister();
        }
    }
}
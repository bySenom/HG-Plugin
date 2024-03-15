package de.bysenom.hg_plugin.handlers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import de.bysenom.hg_plugin.handlers.KillHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreBoardHandler {
    private final Map<UUID, Scoreboard> playerScoreboards = new HashMap<>();
    private final Map<UUID, Objective> playerObjectives = new HashMap<>();

    public void displayScoreboard(Player player) {
        UUID playerId = player.getUniqueId();

        // Check if the player already has a scoreboard
        if (!playerScoreboards.containsKey(playerId)) {
            // Create a new scoreboard for the player
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            playerScoreboards.put(playerId, scoreboard);
        }

        // Get the player's scoreboard
        Scoreboard scoreboard = playerScoreboards.get(playerId);

        // Check if the player already has an objective
        if (!playerObjectives.containsKey(playerId)) {
            // Create a new objective for the player's scoreboard
            Objective objective = scoreboard.registerNewObjective("sb", "dummy", ChatColor.BOLD + "BLACKLOTUS.NET");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            playerObjectives.put(playerId, objective);

            // Populate the scoreboard lines
            objective.getScore(" ").setScore(10); // Blank line for spacing
            objective.getScore("       ").setScore(9); // Blank line for spacing
            objective.getScore(" ").setScore(8); // Blank line for spacing
            objective.getScore("").setScore(7); // Blank line
            objective.getScore(" ").setScore(6); // Blank line for spacing
            objective.getScore(ChatColor.BOLD + "Kills§7:").setScore(5); // Line with text "KILLS:"
            objective.getScore(" ").setScore(3); // Blank line for spacing
            objective.getScore("").setScore(-2); // Blank line
            objective.getScore(" ").setScore(1); // Blank line for spacing
            objective.getScore(ChatColor.BOLD + "Spieler§7:").setScore(0); // Line with text "Players:"
            objective.getScore(ChatColor.BOLD + "Kit§7:").setScore(-3); // Line with text "Players:"


        }

        // Update player count using the objective
        Objective objective = playerObjectives.get(playerId);
        int playerCount = Bukkit.getServer().getOnlinePlayers().size();
        String coloredPlayerCount = ChatColor.AQUA + String.valueOf(playerCount);
        objective.getScore(coloredPlayerCount).setScore(-1);
        int killCount = 0;
        String coloredKillCount = ChatColor.RED + String.valueOf(killCount);
        KillHandler killHandler = new KillHandler();
        objective.getScore(coloredKillCount).setScore(4);

        // Update selected kit entry for the player
        String selectedKit = getSelectedKit(player);
        if (selectedKit != null) {
            objective.getScore(selectedKit).setScore(-4);
        } else {
            objective.getScore("-").setScore(-4); // Placeholder for other kits
        }

        // Set the scoreboard for the player
        player.setScoreboard(scoreboard);
    }

    public static void updateScoreboard(Player player) {
        new ScoreBoardHandler().displayScoreboard(player);
    }

    private static String getSelectedKit(Player player) {
        boolean ninjaEnabled = KitHandler.isNinjaEnabled(player);
        boolean anchorEnabled = KitHandler.isAnchorEnabled(player);

        if (ninjaEnabled) {
            return "§aNinja";
        } else if (anchorEnabled) {
            return "§bAnchor";
        } else {
            return null; // No kit selected
        }
    }

    public void deleteScoreboard(Player player) {
        UUID playerId = player.getUniqueId();
        if (playerObjectives.containsKey(playerId)) {
            Objective objective = playerObjectives.remove(playerId);
            if (objective != null) {
                objective.unregister(); // Unregister the objective
            }
        }
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }
}

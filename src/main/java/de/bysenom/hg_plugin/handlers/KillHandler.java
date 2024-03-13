package de.bysenom.hg_plugin.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KillHandler {
    private Map<UUID, Integer> playerKills;

    public KillHandler() {
        this.playerKills = new HashMap<>();
    }

    // Method to increment kills for a specific player
    public void increasementKills(UUID playerId) {
        int kills = playerKills.getOrDefault(playerId, 0);
        playerKills.put(playerId, kills + 1);
    }

    // Method to get kills for a specific player
    public int getKills(UUID playerId) {
        return playerKills.getOrDefault(playerId, 0);
    }

    // Method to reset kills for a specific player
    public void resetKills(UUID playerId) {
        playerKills.put(playerId, 0);
    }
}

package de.bysenom.hg_plugin;

import org.bukkit.entity.Player;

class CooldownManager {
    private final java.util.Map<Player, Long> cooldowns = new java.util.HashMap<>();

    boolean isOnCooldown(Player player) {
        return cooldowns.containsKey(player) && cooldowns.get(player) > System.currentTimeMillis();
    }

    void setCooldown(Player player, int seconds) {
        long cooldownTime = System.currentTimeMillis() + (seconds * 1000L);
        cooldowns.put(player, cooldownTime);
    }

    int getRemainingCooldown(Player player) {
        if (cooldowns.containsKey(player)) {
            long remainingTime = cooldowns.get(player) - System.currentTimeMillis();
            return (int) (remainingTime / 1000);
        }
        return 0;
    }
}

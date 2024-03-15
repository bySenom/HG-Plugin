package de.bysenom.hg_plugin.handlers;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class AttributeHandler {

    private static boolean attackSpeedModified = false;
    private static final double DEFAULT_ATTACK_SPEED = 4.0; // Default attack speed value

    public static void enableAttackSpeedModifier(double attackSpeed) {
        if (!attackSpeedModified) {
            // Loop through all online players and set their attack speed
            for (Player player : Bukkit.getOnlinePlayers()) {
                setAttackSpeed(player, attackSpeed);
            }
            attackSpeedModified = true;
        }
    }

    public static void disableAttackSpeedModifier() {
        if (attackSpeedModified) {
            // Reset attack speed for all players to default value
            for (Player player : Bukkit.getOnlinePlayers()) {
                resetAttackSpeed(player);
            }
            attackSpeedModified = false;
        }
    }

    private static void setAttackSpeed(Player player, double attackSpeed) {
        if (player != null && attackSpeed >= 0.0 && attackSpeed <= 1024.0) {
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(attackSpeed);
        }
    }

    private static void resetAttackSpeed(Player player) {
        if (player != null) {
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(DEFAULT_ATTACK_SPEED);
        }
    }
}

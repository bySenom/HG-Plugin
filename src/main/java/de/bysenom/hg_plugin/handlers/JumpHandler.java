package de.bysenom.hg_plugin.handlers;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JumpHandler implements Listener {

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        // Check if movement is disabled globally
        if (MovementHandler.isMovementDisabled()) {
            // Cancel the jump event to prevent jumping
            event.setCancelled(true);
        }
    }
}

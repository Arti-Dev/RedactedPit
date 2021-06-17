package com.articreep.redactedpit.listeners;

import com.articreep.redactedpit.UtilBoundingBox;
import com.articreep.redactedpit.commands.RedactedGive;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TreasureListeners implements Listener {
    UtilBoundingBox treasureBox = new UtilBoundingBox(102, 47, 1, -20, 43, -87);

    @EventHandler
    public void onShovelClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().isSimilar(RedactedGive.ArcheologistShovel(1))) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR) {
                player.sendMessage(treasureBox.randomLocation().toString());
            }
        }
    }
}

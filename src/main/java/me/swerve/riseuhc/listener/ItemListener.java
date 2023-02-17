package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.game.UHCGame;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ItemListener implements Listener {

    @EventHandler
    public void onItemBurn(EntityCombustEvent e) {
        if (UHCManager.getInstance().getGame() == null) return;
        if (UHCManager.getInstance().getGame().getCurrentBorder() > 100) return;

        if (e.getEntity().getType() == EntityType.DROPPED_ITEM) e.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentState() != UHCPlayer.PlayerState.PLAYING) e.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        if (UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentState() != UHCPlayer.PlayerState.PLAYING) e.setCancelled(true);
    }
}

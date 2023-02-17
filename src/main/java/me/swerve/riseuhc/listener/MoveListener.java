package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.util.BorderUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        UHCPlayer p = UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId());
        if (p.getCurrentState() == UHCPlayer.PlayerState.PLAYING || p.getCurrentState() == UHCPlayer.PlayerState.SPECTATING) BorderUtil.updatePlayer(e.getPlayer());

        if (p.isInPractice()) if (p.getPlayerObject().getLocation().getY() > 110) p.sendToPractice();
    }
}
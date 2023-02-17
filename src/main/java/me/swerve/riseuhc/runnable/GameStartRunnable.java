package me.swerve.riseuhc.runnable;

import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.scoreboard.ScoreBoardManager;
import me.swerve.riseuhc.util.SitUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartRunnable extends BukkitRunnable {

    public GameStartRunnable() {
        UHCManager.getInstance().setCurrentGameState(UHCManager.GameState.STARTING);
    }

    public void run() {
        if (ScoreBoardManager.getGameCountDown() <= 0) {
            unSitAllPlayers();
            for (Player player : Bukkit.getOnlinePlayers()) player.playSound(player.getLocation(), Sound.valueOf("ENDERDRAGON_GROWL"), .5f, 1);
            UHCManager.getInstance().setCurrentGameState(UHCManager.GameState.PLAYING);
            cancel();
        }

        ScoreBoardManager.setGameCountDown(ScoreBoardManager.getGameCountDown() - 1);
    }

    private void unSitAllPlayers() {
        for (UHCPlayer player : UHCPlayer.getUhcPlayers().values()) if (player.getCurrentState() == UHCPlayer.PlayerState.PLAYING) SitUtil.unSitPlayer(player.getPlayerObject());
    }
}

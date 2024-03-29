package me.swerve.riseuhc.util;

import me.swerve.riseuhc.manager.TeamManager;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.team.Team;

import java.util.Comparator;
import java.util.List;

public class SortUtil {
    public static void sortUHCPlayersByGold(List<UHCPlayer> players) {
        players.sort(Comparator.comparingInt(UHCPlayer::getCurrentGoldMined));
    }

    public static void sortUHCPlayersByDiamonds(List<UHCPlayer> players) {
        players.sort(Comparator.comparingInt(UHCPlayer::getCurrentDiamondsMined));
    }
}

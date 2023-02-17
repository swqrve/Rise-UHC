package me.swerve.riseuhc.util;

import me.swerve.riseuhc.player.UHCPlayer;

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

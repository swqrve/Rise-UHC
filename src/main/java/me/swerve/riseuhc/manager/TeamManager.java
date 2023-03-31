package me.swerve.riseuhc.manager;

import lombok.Getter;
import me.swerve.riseuhc.team.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TeamManager {
    @Getter private static TeamManager instance;

    @Getter private final List<Team> teamsList = new ArrayList<>();

    public TeamManager() {
        instance = this;
    }

    public void sortTeams() {
        teamsList.sort(Comparator.comparingInt(Team::getTeamNumber));
    }
}

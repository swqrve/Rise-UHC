package me.swerve.riseuhc.util;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class KillTopPlayer implements Comparable<KillTopPlayer> {
    @Getter private static final List<KillTopPlayer> killers = new ArrayList<>();

    @Getter @Setter private String name;
    @Getter @Setter private int kills;

    @Getter @Setter private boolean isDead = false;

    public KillTopPlayer(String name, int kills) {
        this.name = name;
        this.kills = kills;

        killers.add(this);
    }

    @Override
    public int compareTo(@NotNull KillTopPlayer o) {
        return o.getKills() > this.kills ? 1 : 0;
    }
}

package me.swerve.riseuhc.util;

import lombok.Getter;

public class ScatterLocation {

    @Getter private final int x;
    @Getter private final int y;
    @Getter private final int z;

    public ScatterLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

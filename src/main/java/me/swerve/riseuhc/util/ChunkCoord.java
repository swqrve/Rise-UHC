package me.swerve.riseuhc.util;

import lombok.Getter;

public class ChunkCoord {
    // Are getters slower then just public calls?
    @Getter public final int x;
    @Getter public final int z;

    public ChunkCoord(int x, int z) {
        this.x = x;
        this.z = z;
    }
}

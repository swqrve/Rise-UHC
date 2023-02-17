package me.swerve.riseuhc.runnable.action;

import lombok.Getter;

public class ValidatedTime {
    @Getter
    private final int time;
    @Getter
    private final Action action;

    public ValidatedTime(int time, Action action) {
        this.time = time;
        this.action = action;
    }
}

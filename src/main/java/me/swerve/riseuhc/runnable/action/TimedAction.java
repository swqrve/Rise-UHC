package me.swerve.riseuhc.runnable.action;

import me.swerve.riseuhc.runnable.GameRunnable;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import java.util.List;

public class TimedAction {
    private final List<ValidatedTime> validatedTimes = new ArrayList<>();
    public enum ActionType { ENABLE_PVP, SHRINK_BORDER, FINAL_HEAL }

    public TimedAction(GameRunnable runnable) {
        runnable.getTimedActions().add(this);
    }

    public TimedAction addAction(int time, String message, TimedAction.ActionType actionType) {
        validatedTimes.add(new ValidatedTime(time, new Action(ChatColor.translateAlternateColorCodes('&', message), actionType)));
        return this;
    }

    public TimedAction addAction(int time, String message) {
        validatedTimes.add(new ValidatedTime(time, new Action(ChatColor.translateAlternateColorCodes('&', message), null)));
        return this;
    }

    public void update(int gameTime) {
        validatedTimes.forEach(time -> { if (time.getTime() <= gameTime) time.getAction().executeAction(); });
        validatedTimes.removeIf(time -> time.getTime() <= gameTime);
    }
}


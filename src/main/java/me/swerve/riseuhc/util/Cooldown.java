package me.swerve.riseuhc.util;
import java.util.UUID;

public class Cooldown {

    private UUID uniqueId = UUID.randomUUID();

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    private long start = System.currentTimeMillis();

    private long expire;

    private boolean notified;


    public Cooldown(long duration) {
        this.expire = this.start + duration;
        if (duration == 0L)
            this.notified = true;
    }

    public long getRemaining() {
        return this.expire - System.currentTimeMillis();
    }

    public boolean hasExpired() {
        return (System.currentTimeMillis() - this.expire >= 0L);
    }

}

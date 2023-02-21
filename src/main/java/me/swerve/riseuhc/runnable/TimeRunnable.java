package me.swerve.riseuhc.runnable;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeRunnable extends BukkitRunnable {
    public void run() {
        for (World world : Bukkit.getWorlds()) world.setTime(1000);
    }
}

package me.swerve.riseuhc.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        if (e.getWorld().getName().contains("uhc")) e.setCancelled(true);
    }
}

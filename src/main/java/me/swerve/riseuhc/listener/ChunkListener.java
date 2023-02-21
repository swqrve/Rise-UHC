package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.player.logger.CombatLogger;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener {

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent e) {
        for (CombatLogger logger : CombatLogger.getLoggers().values()) if (chunkEquals(e.getChunk(), logger.getChunk()) || chunkEquals(e.getChunk(), logger.getLogger().getLocation().getChunk())) e.setCancelled(true);
    }

    public boolean chunkEquals(Chunk chunkOne, Chunk chunkTwo) {
        return chunkOne.getWorld().getName().equalsIgnoreCase(chunkTwo.getWorld().getName())
                && chunkOne.getX() == chunkTwo.getX() && chunkOne.getZ() == chunkTwo.getZ();
    }
}

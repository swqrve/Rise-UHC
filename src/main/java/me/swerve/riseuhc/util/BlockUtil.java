package me.swerve.riseuhc.util;

import lombok.Getter;
import me.swerve.riseuhc.player.UHCPlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockUtil {
    @Getter private static final BlockFace[] faces = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

    public static int amountOfAttachedBlocks(Block block, UHCPlayer player, int totalSize) {
        player.getCountedBlocks().add(block);

        for (BlockFace face : faces) {
            if (player.getCountedBlocks().contains(block.getRelative(face))) continue;
            if (block.getRelative(face).getType() == block.getType()) {
                totalSize += amountOfAttachedBlocks(block.getRelative(face), player, totalSize);
                totalSize++;
            }
        }

        return totalSize;
    }
}

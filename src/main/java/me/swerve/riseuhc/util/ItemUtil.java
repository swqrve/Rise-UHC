package me.swerve.riseuhc.util;

import me.swerve.riseuhc.RiseUHC;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {
    public static void dropItem(ItemStack item, Location loc) {
        BlockPosition blockPos = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
        WorldServer smoothWorld = ((CraftWorld) loc.getWorld()).getHandle();

        Bukkit.getScheduler().runTaskLater(RiseUHC.getInstance(), () -> Block.a(smoothWorld, blockPos, CraftItemStack.asNMSCopy(item)), 2L);
    }
}

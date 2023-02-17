package me.swerve.riseuhc.util;


import lombok.Getter;
import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BorderUtil {

   @Getter private static final List<Integer> blockBypassIDS = Arrays.asList(32, 106, 17, 162, 18, 161, 0, 81, 175, 31, 37, 38, 39, 40);
   private static final Random rand = new Random();
   private static Material borderBlockType = Material.BEDROCK;

    public static void updatePlayer(Entity p, boolean onBorderShrink) {
        if (UHCManager.getInstance().getGame() == null) return;

        boolean isPlayer = p instanceof Player;

        UHCPlayer player = null;
        if (isPlayer) player = UHCPlayer.getUhcPlayers().get(p.getUniqueId());
        if (isPlayer && player.getCurrentState() == UHCPlayer.PlayerState.LOBBY) return;

        int limit = UHCManager.getInstance().getGame().getCurrentBorder();
        if (isPlayer) if (player.getCurrentState() == UHCPlayer.PlayerState.SPECTATING) if (!player.getPlayerObject().hasPermission("uhc.modspectate")) limit = 100;

        boolean teleportNeeded = false;

        double playerX = p.getLocation().getX();
        double playerZ = p.getLocation().getZ();

        if (Math.abs(playerX) >= limit) {
            teleportNeeded = true;
            if (playerX > 0) playerX = limit - 3;
            else playerX = -limit + 3;
        }

        if (Math.abs(playerZ) >= limit) {
            teleportNeeded = true;
            if (playerZ > 0) playerZ = limit - 3;
            else playerZ = -limit + 3;
        }

        if (teleportNeeded) {
            double playerY = 0;
            if (!onBorderShrink) playerY = p.getLocation().getY();
            if (playerY == 0) playerY = getHighestBlockY((int) playerX, (int) playerZ, Bukkit.getWorld("uhc_world")) + 2;

            if (onBorderShrink && (limit == 500 || limit == 100)) {
                playerX = rand.nextInt(limit) * (rand.nextBoolean() ? -1 : 1);
                playerZ = rand.nextInt(limit) * (rand.nextBoolean() ? -1 : 1);
            }

            if (!Bukkit.getWorld("uhc_world").getBlockAt((int) playerX, (int) playerY, (int) playerZ).isEmpty()) playerY = getHighestBlockY((int) playerX, (int) playerZ, Bukkit.getWorld("uhc_world")) + 1;

            if (isPlayer) ((Player) p).playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 0.1f, 1);
            if (isPlayer) p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou've been teleported. You can't walk into border!"));
            p.teleport(new Location(Bukkit.getWorld("uhc_world"), playerX, playerY, playerZ, p.getLocation().getYaw(), p.getLocation().getPitch()));
        }
    }

    public static void updatePlayer(Entity p) {
        updatePlayer(p, false);
    }

    public static void createBedrockWall(int radius) {
        World uhcWorld = Bukkit.getWorld("uhc_world");
        final List<BlockLocation> blockLocations = new ArrayList<>();

        for (int i = radius; i >= -radius; i--) for (int j = 0; j < 2; j++) {
            if (j == 1) blockLocations.add(new BlockLocation(i, getHighestBlockY(i, radius, uhcWorld), radius));
            else blockLocations.add(new BlockLocation(i, getHighestBlockY(i, -radius, uhcWorld), -radius));
        }

        for (int i = radius; i >= -radius; i--) for (int j = 0; j < 2; j++) {
            if (j == 1) blockLocations.add(new BlockLocation(radius, getHighestBlockY(radius, i, uhcWorld), i));
            else blockLocations.add(new BlockLocation(-radius, getHighestBlockY(-radius, i, uhcWorld), i));
        }

        createBlocks(blockLocations, uhcWorld);
    }

    private static void createBlocks(List<BlockLocation> locations, World world) {
        new BukkitRunnable() {
            public void run() {
                for (int j = 0; j < 5000; j++) {
                    if (locations.size() < 1) {
                        cancel();
                        return;
                    }

                    Block block = world.getBlockAt(locations.get(0).x, locations.get(0).y, locations.get(0).z);
                    block.setType(borderBlockType);

                    for (int i = 0; i < 4; i++) {
                        block = block.getRelative(BlockFace.UP);
                        block.setType(borderBlockType);
                    }

                    locations.remove(0);
                }
            }
        }.runTaskTimer(RiseUHC.getInstance(), 0, 1);
    }

    public static int nextBorder(int currentBorder) {
        if (currentBorder - 500 > 0) return currentBorder - 500;
        switch (currentBorder) {
            case 500:
                return 100;
            case 100:
                return 50;
            case 50:
                return 25;
        }
        return 25;
    }

    public static int getHighestBlockY(int x, int z, World world) {
        for (int i = 155; i > 0; i--) {
            if (blockBypassIDS.contains(world.getBlockAt(x, i, z).getType().getId())) continue;
            return i;
        }

        return world.getHighestBlockYAt(x, z);
    }
}


class BlockLocation {
    int x;
    int y;
    int z;

    public BlockLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}

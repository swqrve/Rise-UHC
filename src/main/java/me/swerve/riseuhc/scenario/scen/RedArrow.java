package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RedArrow extends Scenario {
    public RedArrow() {
        super("Red Arrows", (Material.REDSTONE_BLOCK), Collections.singletonList(
                "&f- When a player dies a large red arrow pointing down appears in the sky"
        ));
    }
    @EventHandler
    public void playerDeath(PlayerDeathEvent e) {
        Location center = new Location(e.getEntity().getWorld(), e.getEntity().getLocation().getX(), 90, e.getEntity().getLocation().getY());

        List<Block> arrowBlockPositions = new ArrayList<>();
        for (int i = 0; i < 5; i++) arrowBlockPositions.add(e.getEntity().getWorld().getBlockAt(new Location(e.getEntity().getWorld() ,center.getX(), center.getY() + i, center.getZ())));

        arrowBlockPositions.add(e.getEntity().getWorld().getBlockAt(new Location(e.getEntity().getWorld() ,center.getX() + 1, center.getY()  + 1, center.getZ())));
        arrowBlockPositions.add(e.getEntity().getWorld().getBlockAt(new Location(e.getEntity().getWorld() ,center.getX() + 2, center.getY()  + 2, center.getZ())));
        arrowBlockPositions.add(e.getEntity().getWorld().getBlockAt(new Location(e.getEntity().getWorld() ,center.getX() - 1, center.getY()  + 1, center.getZ())));
        arrowBlockPositions.add(e.getEntity().getWorld().getBlockAt(new Location(e.getEntity().getWorld() ,center.getX() - 2, center.getY()  + 2, center.getZ())));

        for (Block wool : arrowBlockPositions) {
            wool.setType(Material.WOOL);
            ((Wool) wool).setColor(DyeColor.RED);
        }
    }
}

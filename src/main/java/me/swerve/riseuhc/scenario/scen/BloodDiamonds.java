package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;

public class BloodDiamonds extends Scenario {
    public BloodDiamonds() {
        super("Blood Diamonds", Material.DIAMOND, Arrays.asList(
                "&f- When this scenario is enabled, when a player mines diamond ore",
                "&f- they take a half a heart of damage"
        ));
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.DIAMOND_ORE) {
            e.getPlayer().setHealth(e.getPlayer().getHealth() - 1);
        }
    }
}

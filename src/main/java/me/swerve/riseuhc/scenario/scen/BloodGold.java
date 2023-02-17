package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;

public class BloodGold extends Scenario {
    public BloodGold() {
        super("Blood Gold", Material.GOLD_ORE, Arrays.asList(
                "&f- When this scenario is enabled, when a player mines gold ore",
                "&f- they take a half a heart of damage"
        ));
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.GOLD_ORE) {
            e.getPlayer().setHealth(e.getPlayer().getHealth() - 1);
        }
    }
}

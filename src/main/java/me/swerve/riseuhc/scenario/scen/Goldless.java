package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class Goldless extends Scenario {
    public Goldless() {
        super("Goldless", Material.GOLD_ORE, Arrays.asList(
                "&f- When a player mines a gold ore. Nothing drops. Gold is dropped",
                "&f- on death."
        ));
    }

    @EventHandler
    public void onDeathScenario(PlayerDeathEvent e) {
        e.getDrops().add(new ItemStack(Material.GOLD_INGOT, 8));
    }

    @EventHandler
    public void onMineGold(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.GOLD_ORE) {
            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);
        }
    }
}

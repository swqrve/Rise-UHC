package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

public class BedBomb extends Scenario {
    public BedBomb() {
        super("Bed Bombs", Material.BED, Arrays.asList(
                "&f- When this scenario is enabled, when you place a bed",
                "and right click it, they explode in all worlds."
        ));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getType() == Material.BED) {
                TNTPrimed tnt = e.getClickedBlock().getWorld().spawn(e.getClickedBlock().getLocation(), TNTPrimed.class);
                tnt.setFuseTicks(0);
                e.setCancelled(true);
            }
        }
    }
}

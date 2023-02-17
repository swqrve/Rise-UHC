package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.TimeBombUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import java.util.Arrays;

public class TimeBomb extends Scenario {
    public TimeBomb() {
        super("TimeBomb", Material.TNT, Arrays.asList(
                "&f- When a player dies a chest is created with their loot, this chest will",
                "&f- explode after 30 seconds"
        ));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!e.getEntity().getWorld().getName().equals("uhc_world")) return;
        new TimeBombUtil(e.getEntity().getDisplayName(), e.getDrops(), e.getEntity().getLocation());
        e.getDrops().clear();
    }
}

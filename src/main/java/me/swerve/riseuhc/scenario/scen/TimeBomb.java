package me.swerve.riseuhc.scenario.scen;

import lombok.Getter;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.TimeBombUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeBomb extends Scenario {
    @Getter private static final List<TimeBombUtil> activeTimeBombs = new ArrayList<>();
    public TimeBomb() {
        super("TimeBomb", Material.TNT, Arrays.asList(
                "&f- When a player dies a chest is created with their loot, this chest will",
                "&f- explode after 30 seconds"
        ));
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!e.getEntity().getWorld().getName().equals("uhc_world")) return;
        activeTimeBombs.add(new TimeBombUtil(e.getEntity().getDisplayName(), e.getDrops(), e.getEntity().getLocation(), e.getEntity().getKiller()));
        e.getDrops().clear();
    }
}

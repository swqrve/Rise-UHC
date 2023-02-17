package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.Collections;

public class NoFall extends Scenario {
    public NoFall() {
        super("NoFallDamage", Material.FEATHER, Collections.singletonList(
                "&f- When a player would normally take fall damage. Nothing happens."
        ));
    }
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) if (e.getCause() == EntityDamageEvent.DamageCause.FALL) e.setCancelled(true);
    }
}

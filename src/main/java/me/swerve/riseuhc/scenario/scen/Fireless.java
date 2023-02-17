package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import java.util.Collections;

public class Fireless extends Scenario {
    public Fireless() {
        super("Fireless", Material.FLINT_AND_STEEL, Collections.singletonList(
                "&f- Players will not take fire damage."
        ));
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
                e.getCause() == EntityDamageEvent.DamageCause.LAVA)
            e.setCancelled(true);
    }
}

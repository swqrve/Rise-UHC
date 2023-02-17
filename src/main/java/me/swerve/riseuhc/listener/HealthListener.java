package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.attribute.MatchAttribute;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class HealthListener implements Listener {

    @EventHandler
    public void onEntityHeal(EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Horse)) return;
        e.setCancelled((boolean) MatchAttribute.getAttributeFromName("Horse Healing").getCurrentSelection().getValue());
    }
}

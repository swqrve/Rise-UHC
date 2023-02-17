package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Rabbit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class SpawnListener implements Listener {
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        String name = e.getEntity().getWorld().getName().toLowerCase();
        if (name.contains("lobby") || name.contains("practice")) {
            e.setCancelled(true);
            return;
        }

        if (name.contains("uhc") && Bukkit.getWorld("uhc_world").getEntities().size() >= 5000) {
            if (e.getEntity() instanceof Creature) {
                if (e.getEntity().getType() == EntityType.PLAYER) return;
                e.setCancelled(true);
            }

            return;
        }

        if (e.getEntity() instanceof Monster) {
            double mobRate = (double) MatchAttribute.getAttributeFromName("Mob Rate").getCurrentSelection().getValue();
            if (mobRate == 1) return;
            if (mobRate == 0) e.setCancelled(true);
            else if (Scenario.getRandom().nextFloat() > mobRate) e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Rabbit) {
            e.setCancelled(true);
            e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.COW);
        }
    }
}

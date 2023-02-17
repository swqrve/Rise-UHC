package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import java.util.Collections;

public class Bowless extends Scenario {
    public Bowless() {
        super("Bowless", Material.BOW, Collections.singletonList(
                "&f- When a player crafts or tries to shoot a bow nothing happens"
        ));
    }
    @EventHandler
    public void onCraftScenario(CraftItemEvent e) {
        ItemStack item = e.getRecipe().getResult();

        if (item.getType() == Material.BOW) {
            e.setCancelled(true);
            ((Player) e.getWhoClicked()).sendMessage(ChatColor.RED + "Bowless is currently enabled!");
        }
    }
    @EventHandler
    public void shootEvent(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof Player) {
            e.setCancelled(true);
            ((Player) e.getEntity().getShooter()).sendMessage(ChatColor.RED + "Bowless is currently enabled!");
        }
    }
}

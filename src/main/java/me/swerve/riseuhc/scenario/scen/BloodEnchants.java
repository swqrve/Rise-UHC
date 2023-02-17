package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.Arrays;

public class BloodEnchants extends Scenario {
    public BloodEnchants() {
        super("Blood Enchants", Material.ENCHANTED_BOOK, Arrays.asList(
                "&f- When this scenario is enabled, when a player enchants a item",
                "&f- they take a half a heart of damage."
        ));
    }
    @EventHandler
    public void onEnchantItem(EnchantItemEvent e) {
        e.getEnchanter().getPlayer().setHealth(e.getEnchanter().getHealth() - 1);
    }
}

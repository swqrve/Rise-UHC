package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class NoDiamondArmor extends Scenario {
    public NoDiamondArmor() {
        super("No Diamond Armor", Material.DIAMOND_HELMET, Collections.singletonList(
                "&f- When a player tries to craft diamond armor nothing happens"
        ));
    }

    @EventHandler
    public void onCraftEvent(PrepareItemCraftEvent e) {
        Material craftedItem = e.getRecipe().getResult().getType();
        if (craftedItem == Material.DIAMOND_HELMET || craftedItem == Material.DIAMOND_CHESTPLATE || craftedItem == Material.DIAMOND_LEGGINGS || craftedItem == Material.DIAMOND_BOOTS) e.getInventory().setResult(new ItemStack(Material.AIR));
    }
}

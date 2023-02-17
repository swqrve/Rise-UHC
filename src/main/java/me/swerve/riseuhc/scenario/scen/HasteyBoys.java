package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import java.util.Collections;

public class HasteyBoys extends Scenario {
    public HasteyBoys() {
        super("HasteyBoys", Material.DIAMOND_PICKAXE, Collections.singletonList(
                "&fEvery tool that you craft will have Efficiency 3 and Unbreaking 1."
        ));
    }

    @EventHandler
    public void onCraftItem(PrepareItemCraftEvent e) {
        ItemStack item = e.getRecipe().getResult();
        CraftingInventory inv = e.getInventory();
        String name = item.getType().name();

        if ((name.contains("SPADE")) || (name.contains("AXE")) || (name.contains("HOE"))) {
            item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 3);
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            inv.setResult(item);
        }
    }
}

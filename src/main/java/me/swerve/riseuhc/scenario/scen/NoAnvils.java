package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import java.util.Collections;

public class NoAnvils extends Scenario {
    public NoAnvils() {
        super("NoAnvils", Material.ANVIL, Collections.singletonList("" +
                "&f- When a player crafts or tries to use a anvil nothing happens"
        ));
    }
    @EventHandler
    public void onCraftScenario(CraftItemEvent e) {
        ItemStack item = e.getRecipe().getResult();
        if (item.getType() == Material.ANVIL) {
            e.setCancelled(true);
            ((Player) e.getWhoClicked()).sendMessage(ChatColor.RED + "No Anvils is currently enabled!");
        }
    }
    @EventHandler
    public void onAnvilOpen(InventoryOpenEvent e) {
        if (e.getInventory().getType() == InventoryType.ANVIL) {
            ((Player) e.getPlayer()).sendMessage(ChatColor.RED + "No Anvils is currently enabled!"); // Yo spigot is a mess, why do I have to cast to player on e.getPlayer()
            e.setCancelled(true);
        }
    }
}

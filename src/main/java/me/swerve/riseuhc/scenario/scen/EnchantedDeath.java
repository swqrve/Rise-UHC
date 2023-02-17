package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class EnchantedDeath extends Scenario {
    public EnchantedDeath() {
        super("Enchanted Death", Material.ENCHANTMENT_TABLE, Arrays.asList(
                "&f- When this scenario is enabled, when a player dies they will",
                "&f- drop an enchantment table. There's no other way for a player",
                "&f- to get an enchant table."
        ));
    }

    @EventHandler
    public void onCraftScenario(CraftItemEvent e) {
        ItemStack item = e.getRecipe().getResult();
        if (item.getType() == Material.ENCHANTMENT_TABLE) {
            e.setCancelled(true);
            ((Player) e.getWhoClicked()).sendMessage(ChatColor.RED + "Enchanted death is currently enabled!");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        e.getDrops().add(new ItemStack(Material.ENCHANTMENT_TABLE, 1));
    }
}

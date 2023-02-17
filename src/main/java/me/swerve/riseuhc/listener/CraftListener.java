package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.attribute.MatchAttribute;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class CraftListener implements Listener {

    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent e) {
        if (e.getInventory().getResult().getType() != Material.GOLDEN_APPLE) return;
        if (e.getInventory().getResult().getDurability() != 1) return;

        if (!(boolean) MatchAttribute.getAttributeFromName("Notch Apples").getCurrentSelection().getValue()) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
            ((Player) e.getViewers().get(0)).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNotch apples are disabled!"));
        }
    }
}

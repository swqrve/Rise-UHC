package me.swerve.riseuhc.listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class DurabilityListener implements Listener {

    String[] itemBreakFilter = new String[] { "LEGGINGS", "CHESTPLATE", "HELMET", "BOOT" };
    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent e) {
        if (e.getItem().getType() == Material.FISHING_ROD) {
            e.setDamage(1);
        }
    }

    @EventHandler
    public void onItemBreak(PlayerItemBreakEvent e) {
        String itemName = e.getBrokenItem().getType().toString().toLowerCase();
        for (String filter : itemBreakFilter) if (itemName.contains(filter.toLowerCase())) e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_BREAK, 1f, 1f);
    }

}


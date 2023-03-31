package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.TimeBombUtil;
import me.swerve.riseuhc.util.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

public class SafeLoot extends Scenario {
    public SafeLoot() {
        super("SafeLoot", Material.TRIPWIRE_HOOK, Collections.singletonList(
                "&f- When a player dies a chest is created with their loot and only the killer can open the chest for thirty seconds."
        ));
    }

    @EventHandler
    public void onPlayerOpenChest(PlayerInteractEvent e) {
        if (!Scenario.getScenarioByTitle("TimeBomb").isEnabled()) return;
        if (e.getClickedBlock() == null) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock().getType() != Material.CHEST) return;

        Chest chest = (Chest) e.getClickedBlock().getState();
        for (TimeBombUtil bomb : TimeBomb.getActiveTimeBombs()) {
            if (bomb.getTimeBombChest().getChest().getLocation().equals(chest.getLocation()) || bomb.getTimeBombChest().getChest().getBlock().getRelative(BlockFace.EAST).getLocation().equals(chest.getLocation())) {
                if (bomb.getKillerName().equalsIgnoreCase("")) break;
                if (!e.getPlayer().getDisplayName().equalsIgnoreCase(bomb.getKillerName())) {
                    if ((bomb.getTimeBombChest().getCooldown().getRemaining() / 1000) <= 10) break;
                    final String time = TimeUtil.millisToSeconds(bomb.getTimeBombChest().getCooldown().getRemaining() - (1000 * 10));
                    final String context = " second" + ((bomb.getTimeBombChest().getCooldown().getRemaining() / 1000) > 1 ? "s" : "");

                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't open a chest that isn't yours! It will unlock in " + time + context));
                    e.setCancelled(true);
                    break;
                }
            }
        }
    }
}

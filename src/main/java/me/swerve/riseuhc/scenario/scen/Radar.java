package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.event.PvPEnableEvent;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.player.logger.CombatLogger;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.ItemCreator;
import me.swerve.riseuhc.util.TimeBombUtil;
import me.swerve.riseuhc.util.TimeUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Collections;

public class Radar extends Scenario {
    public Radar() {
        super("Radar", Material.COMPASS, Collections.singletonList(
                "&f- When PvP enables, players are given a compass. This compass points towards the nearest player."
        ));
    }

    @EventHandler
    public void onPvPEnable(PvPEnableEvent e) {
        for (UHCPlayer p : UHCPlayer.getUhcPlayers().values()) {
            if (p.getCurrentState() != UHCPlayer.PlayerState.PLAYING) continue;
            p.getPlayerObject().getInventory().addItem(new ItemCreator(Material.COMPASS, 1).setName("&6Radar").getItem());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        if (e.getItem() == null) return;
        if (e.getItem().getType() != Material.COMPASS) return;

        Player p = e.getPlayer();

        UHCPlayer closestPlayer = null;
        CombatLogger closestPlayerLogger = null;

        for (UHCPlayer player : UHCPlayer.getUhcPlayers().values()) {
            if (player.getCurrentState() != UHCPlayer.PlayerState.PLAYING) continue;
            Location loc = player.getPlayerObject().getLocation();

            if (closestPlayer == null && loc.distance(p.getLocation()) < 500) {
                closestPlayer = player;
                continue;
            }

            if (loc.distance(p.getLocation()) < 500 && closestPlayer.getPlayerObject().getLocation().distance(p.getLocation()) < loc.distance(p.getLocation())) closestPlayer = player;
        }

        for (CombatLogger logger : CombatLogger.getLoggers().values()) {
            if (logger.isDead()) continue;
            if (logger.getLogger() == null) continue;
            Location loc = logger.getLogger().getLocation();

            if (closestPlayerLogger == null && loc.distance(p.getLocation()) < 500) {
                closestPlayerLogger = logger;
                continue;
            }

            if (loc.distance(p.getLocation()) < 500 && closestPlayerLogger.getLogger().getLocation().distance(p.getLocation()) < loc.distance(p.getLocation())) closestPlayerLogger = logger;
        }

        if (closestPlayer == null && closestPlayerLogger == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is nobody close to you!"));
            return;
        }

        boolean playerCloser = closestPlayerLogger == null || closestPlayerLogger.getLogger().getLocation().distance(p.getLocation()) >= closestPlayer.getPlayerObject().getLocation().distance(p.getLocation());

        if (playerCloser) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', closestPlayer.getDisplayName() + " is " + closestPlayer.getPlayerObject().getLocation().distance(p.getLocation()) + " blocks away from you."));
            p.setCompassTarget(closestPlayer.getPlayerObject().getLocation());
            return;
        }

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', closestPlayerLogger.getLogger().getName() + " (Logger) is " + closestPlayerLogger.getLogger().getLocation().distance(p.getLocation()) + " blocks away from you."));
        p.setCompassTarget(closestPlayerLogger.getLogger().getLocation());
    }
}

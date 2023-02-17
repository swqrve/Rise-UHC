package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class DoNotDisturb extends Scenario {

    public DoNotDisturb() {
        super("DoNotDisturb", Material.DISPENSER, Arrays.asList(
                "&f- When this scenario is enabled, when a player hits another player",
                "&f- they will be assigned to them."
        ));

        DoNotDisturbRunnable dndTimer = new DoNotDisturbRunnable();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(RiseUHC.getInstance(), dndTimer, 0, 3);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        UUID hitUUID = null;

        if (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (!(((Projectile) e.getDamager()).getShooter() instanceof Player)) return;
            hitUUID = ((Player) ((Projectile) e.getDamager()).getShooter()).getUniqueId();
        }

        if (e.getDamager() instanceof Player) hitUUID = e.getDamager().getUniqueId();

        if (hitUUID == e.getEntity().getUniqueId()) return;

        UHCPlayer hitter = UHCPlayer.getUhcPlayers().get(hitUUID);
        UHCPlayer hit = UHCPlayer.getUhcPlayers().get(e.getEntity().getUniqueId());

        e.setCancelled(checkDND(hitter, hit));
    }


    @EventHandler
    public void onPlayerFish(PlayerFishEvent e){
        if(e.getState() != PlayerFishEvent.State.CAUGHT_ENTITY) return;
        if (!(e.getCaught() instanceof Player)) return;

        Player fisher = e.getPlayer();
        Player fished = (Player) e.getCaught();

        if (fisher.getUniqueId() == fished.getUniqueId()) return;
        e.setCancelled(checkDND(UHCPlayer.getUhcPlayers().get(fisher.getUniqueId()), UHCPlayer.getUhcPlayers().get(fished.getUniqueId())));
    }

    public boolean checkDND(UHCPlayer hitter, UHCPlayer hit) {
        if (hitter.getCurrentState() != UHCPlayer.PlayerState.PLAYING || hit.getCurrentState() != UHCPlayer.PlayerState.PLAYING) return true;

        Date coolDownDate = TimeUtil.addSecondsToJavaUtilDate(new Date(), 31);

        if (hitter.getAssignedPlayer() != null) {
            if (hitter.getAssignedPlayer().getUuid() == hit.getUuid()) {
                hitter.setCooldown(coolDownDate);
                hit.setCooldown(coolDownDate);
                return false;
            }

            hitter.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou're not assigned to this player."));
            return true;
        }

        if (hit.getAssignedPlayer() == null) {
            hitter.setAssignedPlayer(hit);
            hit.setAssignedPlayer(hitter);

            hitter.setCooldown(coolDownDate);
            hit.setCooldown(coolDownDate);

            hitter.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are now assigned to " + hit.getDisplayName() + " for thirty seconds!"));
            hit.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are now assigned to " + hitter.getDisplayName() + " for thirty seconds!"));

            return false;
        }

        hitter.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player is assigned, you can't hit them!"));
        return true;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        UHCPlayer player = UHCPlayer.getUhcPlayers().get(e.getEntity().getUniqueId());
        player.setAssignedPlayer(null);
        player.setCooldown(null);

        for (UHCPlayer p : UHCPlayer.getUhcPlayers().values()) {
            if (p.getAssignedPlayer() == null || p.getCooldown() == null) continue;

            if (p.getAssignedPlayer().getUuid() == player.getUuid()) {
                p.setAssignedPlayer(null);
                p.setCooldown(null);
            }
        }
    }
}

class DoNotDisturbRunnable extends BukkitRunnable {

    @Override
    public void run() {
        if (!Scenario.getScenarioByTitle("DoNotDisturb").isEnabled()) return;

        if (UHCManager.getInstance().getCurrentGameState() != UHCManager.GameState.PLAYING) return;

        for (UHCPlayer p : UHCPlayer.getUhcPlayers().values()) {
            if (p.getAssignedPlayer() == null) continue;
            if (p.getCooldown() == null) continue;

            if (TimeUtil.differenceInSeconds(p.getCooldown(), new Date()) <= 0) {
                p.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYour DND expired!"));
                p.setAssignedPlayer(null);
                p.setCooldown(null);
            }
        }
    }
}


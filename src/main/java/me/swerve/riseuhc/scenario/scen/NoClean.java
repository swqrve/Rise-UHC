package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class NoClean extends Scenario {
    private final ArrayList<UUID> noCleanPlayers = new ArrayList<>();

    public NoClean() {
        super("NoClean", Material.EYE_OF_ENDER, Arrays.asList(
                "&f- When a player kills another  player, they become invulnerable for",
                "&f- 20 seconds, or until they hit someone."
        ));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)  {
        if (e.getEntity().getKiller() == null) return;

        Player killer = e.getEntity().getKiller();
        noCleanPlayers.add(killer.getUniqueId());
        killer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou now have no clean for 20 seconds."));

        Bukkit.getScheduler().runTaskLater(RiseUHC.getInstance(), () -> { // TODO: Switch this to add NOCLEAN cooldown to scoreboard
            if (!noCleanPlayers.contains(killer.getUniqueId())) return;
            noCleanPlayers.remove(killer.getUniqueId());
            killer.sendMessage(ChatColor.RED + "Your no clean has expired!");
        }, 20 * 20);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player damagedPlayer = (Player) e.getEntity();
        if (noCleanPlayers.contains(damagedPlayer.getUniqueId())) e.setCancelled(true);
    }

    @EventHandler
    public void onDamagePlayer(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player))  return;
        UUID hitUUID = null;

        if (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (!(((Projectile) e.getDamager()).getShooter() instanceof Player)) return;
            hitUUID = ((Player) ((Projectile) e.getDamager()).getShooter()).getUniqueId();
        }

        if (e.getDamager() instanceof Player) hitUUID = e.getDamager().getUniqueId();

        if (noCleanPlayers.contains(hitUUID)) {
            noCleanPlayers.remove(hitUUID);
            e.getDamager().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou attacked a player so your no clean expired!"));
        }

        if (noCleanPlayers.contains(e.getEntity().getUniqueId())) {
            e.getDamager().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis person still has no clean!"));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent e){
        if(e.getState() != PlayerFishEvent.State.CAUGHT_ENTITY) return;
        if (!(e.getCaught() instanceof Player)) return;

        Player fisher = e.getPlayer();
        if (noCleanPlayers.contains(fisher.getUniqueId())) {
            noCleanPlayers.remove(fisher.getUniqueId());
            fisher.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou attacked a player so your no clean expired!"));
        }
    }

}

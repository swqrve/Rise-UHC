package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.player.logger.CombatLogger;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.awt.*;

public class DamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player p = (Player) e.getEntity();
        UHCPlayer player = UHCPlayer.getUhcPlayers().get(p.getUniqueId());

        if (player.isInvulnerable()) e.setCancelled(true);

        if (player.getCurrentState() != UHCPlayer.PlayerState.PLAYING){
            if (!player.isInPractice()) {
                e.setCancelled(true);
                return;
            }

            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) e.setCancelled(true);

            return;
        }

        if (UHCManager.getInstance().isBorderShrinkResistance()) e.setCancelled(true);
        if (UHCManager.getInstance().getGame() != null) if (UHCManager.getInstance().getGame().getGameTime() < 5) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow && e.getEntity() instanceof Player) {
            Arrow arrow = (Arrow) e.getDamager();

            if (arrow.getShooter() instanceof Player) {
                Player damager = (Player) arrow.getShooter();
                Player damaged = (Player) e.getEntity();

                Bukkit.getScheduler().runTaskLater(RiseUHC.getInstance(), () -> damager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + damaged.getName() + " &fis now at &6" + (int) Math.floor((damaged.getHealth() / 20) * 100)) + "%"), 1);
            }
        }

        if (!(e.getDamager() instanceof Player)) {
            if (e.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) return;
            if (UHCManager.getInstance().getGame() == null) return;
            Projectile proj = (Projectile) e.getDamager();
            if (proj.getShooter() instanceof Player && !UHCManager.getInstance().isPvpEnabled()) e.setCancelled(true);
            return;
        }

        if (e.getEntity() instanceof Zombie) {
            for (CombatLogger logger : CombatLogger.getLoggers().values()) {
                if (e.getEntity().getUniqueId() != logger.getLogger().getUniqueId()) continue;
                if (!UHCManager.getInstance().isPvpEnabled()) e.setCancelled(true);
            }
            return;
        }

        UHCPlayer p = UHCPlayer.getUhcPlayers().get(e.getDamager().getUniqueId());
        if (p.getCurrentState() == UHCPlayer.PlayerState.SPECTATING) {
            e.setCancelled(true);
            return;
        }

        if (!(e.getEntity() instanceof Player)) return;

        if (p.getCurrentState() == UHCPlayer.PlayerState.LOBBY) if (!p.isInPractice()) e.setCancelled(true);

        if (UHCManager.getInstance().getGame() != null) if (!UHCManager.getInstance().isPvpEnabled()) e.setCancelled(true);
        if (e.isCancelled()) return;

        for (UHCPlayer player : UHCPlayer.getUhcPlayers().values()) {
            if (player.getCurrentState() != UHCPlayer.PlayerState.SPECTATING) continue;
            if (!player.getPlayerObject().hasPermission("uhc.modspectate")) continue;
            if (!player.isFightStaffAlert()) continue;

            TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f[&6S&f] &6" + ((Player) e.getDamager()).getDisplayName() +  " &eis fighting &6" + ((Player) e.getEntity()).getDisplayName()));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&6Click to teleport to &f" + ((Player) e.getDamager()).getDisplayName())).create()));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + ((Player) e.getDamager()).getDisplayName()));
            player.getPlayerObject().spigot().sendMessage(component);
        }
    }
}

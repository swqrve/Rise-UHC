package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.command.KillTopCommand;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.ItemCreator;
import me.swerve.riseuhc.util.KillTopPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Objects;

public class DeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        UHCPlayer player = UHCPlayer.getUhcPlayers().get(e.getEntity().getUniqueId());

        player.setRespawnItems(player.getPlayerObject().getInventory().getContents());
        player.setRespawnArmor(player.getPlayerObject().getInventory().getArmorContents());
        player.setRespawnLocation(e.getEntity().getLocation());

        if (!(Scenario.getScenarioByTitle("TimeBomb").isEnabled())) e.getDrops().add(new ItemCreator(Material.SKULL_ITEM, 1).setOwner(e.getEntity().getDisplayName()).getItem());

        if (player.getCurrentState() != UHCPlayer.PlayerState.PLAYING) {
            e.setDeathMessage("");
            e.getDrops().clear();

            Bukkit.getScheduler().scheduleSyncDelayedTask(RiseUHC.getInstance(), () -> {
                Player p = null;
                if (e.getEntity().getKiller() != null) p = e.getEntity().getKiller();

                player.getPlayerObject().spigot().respawn();
                if (player.isInPractice()) {
                    player.sendToPractice();

                    if (p != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou killed " + player.getPlayerObject().getDisplayName() + "!"));
                        p.setHealth(20);
                        p.playSound(p.getLocation(), Sound.valueOf("LEVEL_UP"), 1f, 1);
                    }
                }

            }, 1);
            return;
        }

        if (e.getEntity().getKiller() != null) {
            UHCPlayer.getUhcPlayers().get(e.getEntity().getKiller().getUniqueId()).setCurrentKills(UHCPlayer.getUhcPlayers().get(e.getEntity().getKiller().getUniqueId()).getCurrentKills() + 1);

            boolean found = false;
            for (KillTopPlayer p : KillTopPlayer.getKillers()) {
                if (Objects.equals(ChatColor.stripColor(p.getName()), ChatColor.stripColor(e.getEntity().getKiller().getDisplayName()))) {
                    found = true;
                    p.setKills(p.getKills() + 1);
                    break;
                }
            }

            if (!found) {
                new KillTopPlayer(e.getEntity().getKiller().getDisplayName(), 1);
            }
        }

        for (KillTopPlayer p : KillTopPlayer.getKillers()) if (p.getName().equals(player.getPlayerObject().getName())) p.setDead(true);

        Bukkit.getScheduler().scheduleSyncDelayedTask(RiseUHC.getInstance(), () -> {
            player.getPlayerObject().spigot().respawn();
            player.setSpectator();
        }, 1);

        if (e.getEntity().getKiller() == null) {
            e.getDeathMessage().replaceAll(e.getEntity().getDisplayName(), "&a" + e.getEntity().getDisplayName() + "&7[&f" + UHCPlayer.getUhcPlayers().get(e.getEntity().getUniqueId()).getCurrentKills() + "&7]&c");
            e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&c" + e.getDeathMessage()));
        } else e.setDeathMessage(ChatColor.translateAlternateColorCodes('&', "&a" + e.getEntity().getDisplayName() + "&7[&f" + UHCPlayer.getUhcPlayers().get(e.getEntity().getUniqueId()).getCurrentKills() + "&7]" + " &cwas slain by &a"  + e.getEntity().getKiller().getDisplayName() + "&7[&f" + UHCPlayer.getUhcPlayers().get(e.getEntity().getKiller().getUniqueId()).getCurrentKills() + "&7]."));

    }
}

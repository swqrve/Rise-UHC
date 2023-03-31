package me.swerve.riseuhc.listener;


import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.player.logger.CombatLogger;
import me.swerve.riseuhc.util.KillTopPlayer;
import me.swerve.riseuhc.util.OreUtil;
import me.swerve.riseuhc.util.TimeUtil;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import net.minecraft.server.v1_8_R3.ScoreboardServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Date;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onPlayerConnect(PlayerLoginEvent e) {
        if (UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.SCATTERING) {
            e.setKickMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] You can't join while the server is scattering!"));
            e.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
            return;
        }

        if (e.getPlayer().hasPermission("uhc.override")) return;

        if (UHCManager.getInstance().isWhitelisted() && UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.SCHEDULED) {
            int differenceInSeconds  = TimeUtil.differenceInSeconds(UHCManager.getInstance().getScheduleDate(), new Date());

            String result = String.format("%02d:%02d", (differenceInSeconds / 60) % 60, differenceInSeconds % 60);
            if (differenceInSeconds / 3600 != 0) result = String.format("%02d:%02d:%02d", differenceInSeconds / 3600, (differenceInSeconds / 60) % 60, differenceInSeconds % 60);

            e.setKickMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] The UHC is currently whitelisted. Opening in " + result));
            e.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);

            return;
        }

/*        if (UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.PLAYING) {
            if (UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()) != null) return;
            if (!UHCManager.getInstance().isPvpEnabled()) return;
            e.setKickMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] PvP has been enabled. There are no more late scatters."));
            e.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);

            return;
        }*/

        if (UHCManager.getInstance().isWhitelisted()) {
            if (e.getPlayer().hasPermission("uhc.whitelist")) return;
            e.setKickMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] You can't join while the server is whitelisted."));
            e.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);

            return;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fWelcome to Rise UHC."));
        e.setJoinMessage(null);
        //  e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', "&7[&a+&7] &7" + e.getPlayer().getDisplayName()));

        UHCPlayer player = new UHCPlayer(e.getPlayer());
        for (UHCPlayer p : UHCPlayer.getUhcPlayers().values()) p.hideSpectators();

        if (UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.PLAYING) UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).setSpectator();
        if (CombatLogger.getLoggers().get(e.getPlayer().getUniqueId()) != null) UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).useCombatLogger();

        for (KillTopPlayer p : KillTopPlayer.getKillers()) if (p.getName().equalsIgnoreCase(e.getPlayer().getDisplayName())) {
            player.setCurrentKills(p.getKills());
            break;
        }

        for (OreUtil p : OreUtil.getMiners()) if (p.getName().equalsIgnoreCase(e.getPlayer().getDisplayName())) {
            player.setCurrentDiamondsMined(p.getMinedDiamonds());
            player.setCurrentGoldMined(p.getMinedGold());
            break;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(RiseUHC.getInstance(), () -> {
            Scoreboard scoreboard = e.getPlayer().getScoreboard();

            Objective health = scoreboard.registerNewObjective("tab", "health");
            health.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            health.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4❤"));

            Objective name = scoreboard.registerNewObjective("name", "health");
            name.setDisplaySlot(DisplaySlot.BELOW_NAME);
            name.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4❤"));

            scoreboard.registerNewTeam("color");
            scoreboard.getTeam("color").setPrefix(ChatColor.translateAlternateColorCodes('&', "&c"));
            scoreboard.getTeam("color").addPlayer(e.getPlayer());

            for (Player p : Bukkit.getOnlinePlayers()) {
                e.getPlayer().getScoreboard().getTeam("color").addPlayer(p);
                p.getScoreboard().getTeam("color").addPlayer(e.getPlayer());
            }
        }, 20);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        e.setQuitMessage(null);
       // e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', "&7[&c-&7] &7" + e.getPlayer().getDisplayName()));

        if (UHCManager.getInstance().getGame() != null) if (UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentState() == UHCPlayer.PlayerState.PLAYING) {
            Bukkit.getPluginManager().registerEvents(new CombatLogger(e.getPlayer(), UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentKills()), RiseUHC.getInstance());

            boolean found = false;

            for (OreUtil savedOres : OreUtil.getMiners()) if (savedOres.getName().equalsIgnoreCase(e.getPlayer().getDisplayName())) {
                found = true;
                savedOres.setMinedDiamonds(UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentDiamondsMined());
                savedOres.setMinedGold(UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentGoldMined());
            }

            if (!found) new OreUtil(e.getPlayer().getDisplayName(), UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentDiamondsMined(), UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentGoldMined());
        }

        UHCPlayer.getUhcPlayers().remove(e.getPlayer().getUniqueId());
    }
}

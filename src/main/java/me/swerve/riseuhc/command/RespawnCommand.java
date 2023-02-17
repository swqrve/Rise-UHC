package me.swerve.riseuhc.command;

import me.swerve.riseuhc.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RespawnCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;
        if (!p.hasPermission("uhc.respawn")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /respawn PlayerName"));
            return false;
        }

        String playerName = args[0];

        if (Bukkit.getPlayer(playerName) == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find a player by that name."));
            return false;
        }

        if (!Bukkit.getPlayer(playerName).isOnline()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat player is not available!"));
            return false;
        }

        UHCPlayer player = UHCPlayer.getUhcPlayers().get(Bukkit.getPlayer(playerName).getUniqueId());

        if (player.getCurrentState() == UHCPlayer.PlayerState.PLAYING) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat player is not dead!"));
            return false;
        }

        UHCPlayer.getUhcPlayers().get(Bukkit.getPlayer(playerName).getUniqueId()).respawn();
        return false;
    }
}

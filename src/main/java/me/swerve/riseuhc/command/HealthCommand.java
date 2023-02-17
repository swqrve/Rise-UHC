package me.swerve.riseuhc.command;

import me.swerve.riseuhc.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealthCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou must be a player to use this command."));
            return false;
        }

        Player p = (Player) sender;
        if(!p.hasPermission("uhc.health")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (args.length < 1) {
            sendPlayerHealth(p, sender);
            return false;
        }

        String playerName = args[0];

        if (Bukkit.getPlayer(playerName) == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find a player by that name."));
            return false;
        }

        if (!Bukkit.getPlayer(playerName).isOnline() || UHCPlayer.getUhcPlayers().get(Bukkit.getPlayer(playerName).getUniqueId()).getCurrentState() != UHCPlayer.PlayerState.PLAYING) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat player is not available!"));
            return false;
        }

        sendPlayerHealth(Bukkit.getPlayer(playerName), sender);
        return false;
    }

    private void sendPlayerHealth(Player p, CommandSender sender) { sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Health&f] &6" + p.getDisplayName() +  " &eis at &7" + p.getHealth() +  " §4❤"));  }
}

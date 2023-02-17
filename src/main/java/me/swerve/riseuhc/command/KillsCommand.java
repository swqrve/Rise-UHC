package me.swerve.riseuhc.command;

import me.swerve.riseuhc.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KillsCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou must be a player to use this command."));
            return false;
        }

        Player p = (Player) sender;
        if(!p.hasPermission("uhc.kills")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (args.length < 1) {
            sendPlayerKills(p, sender);
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

        sendPlayerKills(Bukkit.getPlayer(playerName), sender);
        return false;
    }

    private void sendPlayerKills(Player p, CommandSender sender) { sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6Kills&f] &6" + p.getDisplayName() +  " &ehas &7" + UHCPlayer.getUhcPlayers().get(p.getPlayer().getUniqueId()).getCurrentKills() +  " &ekills.")); }
}

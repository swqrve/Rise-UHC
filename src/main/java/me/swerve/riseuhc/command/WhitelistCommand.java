package me.swerve.riseuhc.command;

import me.swerve.riseuhc.manager.UHCManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhitelistCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;
        if (!p.hasPermission("uhc.managewhitelist")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (UHCManager.getInstance().isWhitelisted()) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&aWhitelist is now disabled."));
            UHCManager.getInstance().setWhitelisted(false);

            return false;
        }

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cWhitelist is now enabled."));
        UHCManager.getInstance().setWhitelisted(true);

        return false;
    }
}

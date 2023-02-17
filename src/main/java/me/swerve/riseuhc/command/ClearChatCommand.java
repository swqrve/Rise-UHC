package me.swerve.riseuhc.command;

import me.swerve.riseuhc.manager.UHCManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;
        if (!p.hasPermission("uhc.clearchat")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        Bukkit.broadcastMessage(StringUtils.repeat(" \n", 100));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&aChat has been cleared."));

        return false;
    }
}

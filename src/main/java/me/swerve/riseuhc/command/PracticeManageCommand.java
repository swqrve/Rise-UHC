package me.swerve.riseuhc.command;

import me.swerve.riseuhc.manager.UHCManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PracticeManageCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;
        if (!p.hasPermission("uhc.managepractice")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (UHCManager.getInstance().isPracticeEnabled()) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cPractice is now disabled."));
            UHCManager.getInstance().setPracticeEnabled(false);
            UHCManager.getInstance().clearPractice();

            return false;
        }

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&aPractice is now enabled."));
        UHCManager.getInstance().setPracticeEnabled(true);


        return false;
    }
}

package me.swerve.riseuhc.command;

import me.swerve.riseuhc.manager.UHCManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteChatCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;
        if (!p.hasPermission("uhc.chatmute")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (UHCManager.getInstance().isChatEnabled()) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cChat is now muted."));
            UHCManager.getInstance().setChatEnabled(false);

            return false;
        }

        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&aChat is now unmuted."));
        UHCManager.getInstance().setChatEnabled(true);


        return false;
    }
}

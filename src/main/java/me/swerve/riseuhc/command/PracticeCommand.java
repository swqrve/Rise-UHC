package me.swerve.riseuhc.command;

import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PracticeCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou must be a player to use this command."));
            return false;
        }

        Player p = (Player) sender;
        if(!p.hasPermission("uhc.practice")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        UHCPlayer uhcPlayer = UHCPlayer.getUhcPlayers().get(p.getUniqueId());

        if (!UHCManager.getInstance().isPracticeEnabled()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPractice is disabled."));
            return false;
        }

        if (uhcPlayer.isInPractice()) {
            uhcPlayer.reset();
            return false;
        }

        uhcPlayer.sendToPractice();

        return false;
    }
}

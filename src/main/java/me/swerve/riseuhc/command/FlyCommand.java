package me.swerve.riseuhc.command;

import me.swerve.riseuhc.util.CrashType;
import me.swerve.riseuhc.util.CrashUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (!p.hasPermission("uhc.fly")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (p.isFlying()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou're no longer flying."));
            p.setFlying(false);
            return false;
        }

        if (!p.isFlying()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can now fly."));
            p.setFlying(true);
            return false;
        }

        return false;
    }
}


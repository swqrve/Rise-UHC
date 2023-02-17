package me.swerve.riseuhc.command;

import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.menu.board.SchedulerMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScheduleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou must be a player to use this command."));
            return false;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("uhc.schedule")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (UHCManager.getInstance().getCurrentGameState() != UHCManager.GameState.WAITING) if (UHCManager.getInstance().getCurrentGameState() != UHCManager.GameState.UNWHITELISTED) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fThe game can't be in the current state and be scheduled!"));
            return false;
        }

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fOpening scheduler..."));
        Bukkit.getPluginManager().registerEvents(new SchedulerMenu(p), RiseUHC.getInstance());

        return false;
    }
}

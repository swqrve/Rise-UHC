package me.swerve.riseuhc.command;

import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.menu.board.ManageMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ManageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou must be a player to use this command."));
            return false;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("uhc.manage")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (args.length < 1) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fOpening Management Menu."));
            Bukkit.getPluginManager().registerEvents(new ManageMenu(p, false), RiseUHC.getInstance());
            return false;
        }

        if (Boolean.parseBoolean(args[0])) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fOpening Management Menu. Overriding"));
            Bukkit.getPluginManager().registerEvents(new ManageMenu(p, true), RiseUHC.getInstance());
            return false;
        }

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fOpening Management Menu. If you were trying to override, please use /" + label + " true."));
        Bukkit.getPluginManager().registerEvents(new ManageMenu(p, false), RiseUHC.getInstance());

        return false;
    }
}

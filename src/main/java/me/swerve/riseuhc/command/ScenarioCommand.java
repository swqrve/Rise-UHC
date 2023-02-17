package me.swerve.riseuhc.command;

import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.menu.board.ScenarioMenu;
import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ScenarioCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou must be a player to use this command."));
            return false;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("uhc.scenario")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (Scenario.getEnabledScenarios().size() < 1) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fThere are no enabled scenarios."));
            return false;
        }

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fShowing enabled Scenarios..."));
        Bukkit.getPluginManager().registerEvents(new ScenarioMenu(p), RiseUHC.getInstance());

        return false;
    }
}

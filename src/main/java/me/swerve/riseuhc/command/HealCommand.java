package me.swerve.riseuhc.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to use this command.");
            return false;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("uhc.heal")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo permission."));
            return false;
        }

        if (args.length == 0) {
            p.setHealth(20.0D);
            p.setFoodLevel(20);
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fYou have been healed."));

            return false;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /heal [Player Name / @a]"));
            return false;
        }

        if (args[0].equalsIgnoreCase("@a")) {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                player.setHealth(20.0D);
                player.setFoodLevel(20);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&cYou have been healed by" + p.getName()));
            }
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have healed every player"));
            return false;
        }

        Player target = Bukkit.getServer().getPlayer(args[0]);
        if (target == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find a player by the name of " + args[0] +"!"));
            return false;
        }

        target.setHealth(20.0D);
        target.setFoodLevel(20);
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have healed " + target.getDisplayName() + "."));
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have been healed by " + p.getName()));

        return false;
    }
}

package me.swerve.riseuhc.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (!p.hasPermission("uhc.gamemode")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (!label.equalsIgnoreCase("gm")) {
            String s = label.charAt(label.length() - 1) + "";
            String[] newArgs = new String[] {s};
            if (args.length == 1) newArgs = new String[] {s, args[0] };
            args = newArgs;
        }

        if (args.length < 1) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /gm (c/s/a) <player>"));
            return false;
        }

        GameMode gameMode = null;

        switch (args[0]) {
            case "a":
                gameMode = GameMode.ADVENTURE;
                break;
            case "s":
                gameMode = GameMode.SURVIVAL;
                break;
            case "c":
                gameMode = GameMode.CREATIVE;
                break;
            default:
                break;
        }

        if (gameMode == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /gm (c/s/a) <player>"));
            return false;
        }

        if (args.length < 2) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You've been set to " + gameMode.toString().toLowerCase() + " mode!"));
            p.setGameMode(gameMode);
            return false;
        }

        if (Bukkit.getPlayer(args[1]) == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find a player by the name of " + args[1]));
            return false;
        }

        Bukkit.getPlayer(args[1]).setGameMode(gameMode);
        Bukkit.getPlayer(args[1]).sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You've been set to " + gameMode.toString().toLowerCase() + " mode!"));
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou've set " + args[1] + " to " + gameMode.toString().toLowerCase() + " mode!"));

        return false;
    }
}


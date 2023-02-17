package me.swerve.riseuhc.command;

import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LateScatterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou must be a player to use this command."));
            return false;
        }

        Player p = (Player) sender;
        UHCPlayer player = UHCPlayer.getUhcPlayers().get(p.getUniqueId());

        if (player.getCurrentState() == UHCPlayer.PlayerState.PLAYING) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou're already playing!"));
            return false;
        }

        if (UHCManager.getInstance().getCurrentGameState() != UHCManager.GameState.PLAYING) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is no game running right now.."));
            return false;
        }

        if (UHCManager.getInstance().isPvpEnabled()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPvp has already been enabled. You can no longer late scatter."));
            return false;
        }

        player.lateScatter();
        return false;
    }
}

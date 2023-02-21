package me.swerve.riseuhc.command;

import lombok.Getter;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.util.KillTopPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KillTopCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou must be a player to use this command."));
            return false;
        }

        Player p = (Player) sender;
        if(!p.hasPermission("uhc.killtop")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (UHCManager.getInstance().getGame() == null) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fThere is no game running right now!"));
            return false;
        }

        KillTopPlayer.getKillers().sort(Comparator.comparingInt(KillTopPlayer::getKills));

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&m-------------------------"));
        for (int i = KillTopPlayer.getKillers().size() - 1; i >= KillTopPlayer.getKillers().size() - 10; i--) {
            if (i < 0 || i > KillTopPlayer.getKillers().size() - 1 || KillTopPlayer.getKillers().get(i) == null) continue;
            String playerColorCode = "&a";
            if (KillTopPlayer.getKillers().get(i).isDead()) playerColorCode = "&c&m";
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', playerColorCode + KillTopPlayer.getKillers().get(i).getName() + ": &7" + KillTopPlayer.getKillers().get(i).getKills()));
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&m-------------------------"));

        return false;
    }
}

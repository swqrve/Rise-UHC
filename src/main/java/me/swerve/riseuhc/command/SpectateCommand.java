package me.swerve.riseuhc.command;

import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.world.WorldManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou must be a player to use this command."));
            return false;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("uhc.spec")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        UHCPlayer uhcPlayer = UHCPlayer.getUhcPlayers().get(p.getUniqueId());

        if (uhcPlayer.getCurrentState() == UHCPlayer.PlayerState.SPECTATING) {
            uhcPlayer.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou are no longer spectating."));
            uhcPlayer.reset();
            return false;
        }

        if (!WorldManager.getInstance().isFullyGeneratedUHCWorld()) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fThe UHC World must be generated to spectate."));
            return false;
        }

        if (uhcPlayer.getCurrentState() == UHCPlayer.PlayerState.PLAYING) {
            UHCManager.getInstance().getGame().setInitialPlayerCount(UHCManager.getInstance().getGame().getInitialPlayerCount() - 1);
        }

        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou're now spectating.."));
        uhcPlayer.setSpectator();

        return false;
    }
}

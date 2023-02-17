package me.swerve.riseuhc.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class HelpopReplyCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou must be a player to use this command."));
            return false;
        }

        Player p = (Player) sender;
        if (!p.hasPermission("uhc.helpop")) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNo Permission."));
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /helpopreply <id> <message>"));
            return false;
        }

        int ID = 0;
        try { ID = Integer.parseInt(args[0]); } catch (Exception e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /helpopreply <id> <message>"));
            return false;
        }

        Player helpoper = HelpopCommand.getHelpopIds().get(ID);

        if (!helpoper.isOnline()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat player is no longer online!"));
            return false;
        }

        List<String> helpopResponseMessage = new ArrayList<>(Arrays.asList(args));
        helpopResponseMessage.remove(0);

        String cleanedMessage = helpopResponseMessage.toString().replaceAll(", ", " ").replaceAll("\\[", "").replaceAll("]", "");
        helpoper.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', "&f[&6HelpOp&f] &6" + ((Player) sender).getDisplayName() + ": &f" + cleanedMessage)));
        helpoper.playSound(helpoper.getLocation(), Sound.valueOf("ORB_PICKUP"), .2f, 1);

        return false;
    }

}

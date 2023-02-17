package me.swerve.riseuhc.command;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TwitterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou must be a player to use this command."));
            return false;
        }

        TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &ftwitter.com/RiseGameFeed\n"));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&6Click to Open")).create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/RiseGameFeed\n"));
        ((Player) sender).spigot().sendMessage(component);

        return false;
    }
}

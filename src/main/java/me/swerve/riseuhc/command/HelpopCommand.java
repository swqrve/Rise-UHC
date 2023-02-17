package me.swerve.riseuhc.command;

import lombok.Getter;
import me.swerve.riseuhc.player.UHCPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class HelpopCommand implements CommandExecutor {
    @Getter private static final Map<Integer, Player> helpopIds = new HashMap<>();

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

        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /helpop <Message>"));
            return false;
        }

        List<String> helpopMessage = new ArrayList<>(Arrays.asList(args));

        sendHelpop((Player) sender, helpopMessage);

        return false;
    }


    private void sendHelpop(Player sender, List<String> message) {
        int currentID = helpopIds.size() + 1;

        String cleanedString = message.toString().replaceAll(", ", " ").replaceAll("\\[", "").replaceAll("]", "");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', "&f[&6HelpOp&f] &6You submitted a Helpop Message: &f" + cleanedString)));
        helpopIds.put(currentID, sender);

        for (UHCPlayer player : UHCPlayer.getUhcPlayers().values()) {
            if (player.getPlayerObject() == null) continue;
            if (!player.getPlayerObject().hasPermission("uhc.modspectate")) continue;
            if (player.getCurrentState() != UHCPlayer.PlayerState.SPECTATING) continue;

            TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f[&6HelpOp&f] &6" + sender.getDisplayName() + ": &f" + cleanedString));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&6Click to Reply")).create()));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/helpopreply " + currentID + " "));

            player.getPlayerObject().spigot().sendMessage(component);
        }
    }
}

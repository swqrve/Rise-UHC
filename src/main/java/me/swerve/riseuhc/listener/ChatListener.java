package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage();
        if (e.getPlayer().hasPermission("uhc.commandbypass")) return;
        if (command.startsWith("/me") || command.startsWith("/pl") || command.startsWith("/bukkit") || command.startsWith("/version")) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (e.getPlayer().hasPermission("uhc.talk")) return;

        if (UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentState() != UHCPlayer.PlayerState.PLAYING && UHCManager.getInstance().getGame() != null) {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fYou can't talk unless you're playing!"));
            e.setCancelled(true);
        }

        if (!UHCManager.getInstance().isChatEnabled()) {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cChat is disabled."));
            e.setCancelled(true);
        }
    }
}

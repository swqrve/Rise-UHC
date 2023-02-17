package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalListener implements Listener {

    @EventHandler
    public void onPlayerEnterPortal(PlayerPortalEvent e) {
        if (e.getTo().getWorld() == null) {
            e.setCancelled(true);
            return;
        }

        if (e.getTo().getWorld().getEnvironment() == World.Environment.NETHER) {
            if (!(boolean) MatchAttribute.getAttributeFromName("Nether").getCurrentSelection().getValue()) {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNether is disabled."));
                e.setCancelled(true);
                return;
            }

            if (!UHCManager.getInstance().isPvpEnabled()) {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNether is disabled until PvP."));
                e.setCancelled(true);
            }

            for (UHCPlayer player : UHCPlayer.getUhcPlayers().values()) {
                if (player.getCurrentState() != UHCPlayer.PlayerState.SPECTATING) continue;
                if (!player.getPlayerObject().hasPermission("uhc.modspectate")) continue;
                if (!player.isNetherEnterStaffAlert()) continue;

                TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f[&6S&f] &6" + e.getPlayer().getDisplayName() +  " &eis entering the Nether."));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&6Click to teleport to &f" + e.getPlayer().getDisplayName())).create()));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + e.getPlayer().getDisplayName()));
                player.getPlayerObject().spigot().sendMessage(component);
            }

            return;
        }

        if (e.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
            e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThe End is disabled."));
            e.setCancelled(true);
        }
    }
}

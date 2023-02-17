package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.BlockUtil;
import me.swerve.riseuhc.util.ItemUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

public class BlockListener implements Listener {

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        if (UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentState() != UHCPlayer.PlayerState.PLAYING) {
            if (e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled(true);
            return;
        }

        if (UHCManager.getInstance().getCurrentGameState() != UHCManager.GameState.PLAYING) e.setCancelled(true);

        if (e.getBlock().getType() == Material.DIAMOND_ORE) {
            UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).setCurrentDiamondsMined(UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentDiamondsMined() + 1);

            for (UHCPlayer player : UHCPlayer.getUhcPlayers().values()) {
                if (player.getCurrentState() != UHCPlayer.PlayerState.SPECTATING) continue;
                if (player.getPlayerObject() == null) continue;
                if (!player.getPlayerObject().hasPermission("uhc.modspectate")) continue;
                if (!player.isDiamondStaffAlert()) continue;
                if (player.getCountedBlocks().contains(e.getBlock())) continue;

                int veinSize = BlockUtil.amountOfAttachedBlocks(e.getBlock(), UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()), 0);
                if (veinSize == 0) continue;

                TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f[&6S&f] &6" + e.getPlayer().getDisplayName() +  " &eis mining &bDiamonds&e. &7[V: " + veinSize + "] [T: " + UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentDiamondsMined()+  "]"));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&6Click to teleport to &f" + e.getPlayer().getDisplayName())).create()));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + e.getPlayer().getDisplayName()));
                player.getPlayerObject().spigot().sendMessage(component);
            }

            return;
        }

        if (e.getBlock().getType() == Material.GOLD_ORE) {
            UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).setCurrentGoldMined(UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentGoldMined() + 1);

            for (UHCPlayer player : UHCPlayer.getUhcPlayers().values()) {
                if (player.getCurrentState() != UHCPlayer.PlayerState.SPECTATING) continue;
                if (player.getPlayerObject() == null) continue;
                if (!player.getPlayerObject().hasPermission("uhc.modspectate")) continue;
                if (!player.isGoldStaffAlert()) continue;
                if (player.getCountedBlocks().contains(e.getBlock())) continue;

                int veinSize = BlockUtil.amountOfAttachedBlocks(e.getBlock(), UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()), 0);
                if (veinSize == 0) continue;

                TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f[&6S&f] &6" + e.getPlayer().getDisplayName() +  " &eis mining &6Gold&e. &7[V: " + veinSize + "] [T: " + UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentGoldMined()+  "]"));
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', "&6Click to teleport to &f" + e.getPlayer().getDisplayName())).create()));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + e.getPlayer().getDisplayName()));
                player.getPlayerObject().spigot().sendMessage(component);
            }

            return;
        }

        if (e.getBlock().getType() == Material.REDSTONE_ORE) {
            int redstoneXPRate = (int) MatchAttribute.getAttributeFromName("Redstone XP").getCurrentSelection().getValue();
            e.setExpToDrop(e.getExpToDrop() * redstoneXPRate);

            return;
        }

        if (e.getBlock().getType() == Material.QUARTZ_ORE) {
            int quartsXPRate = (int) MatchAttribute.getAttributeFromName("Quartz XP").getCurrentSelection().getValue();
            e.setExpToDrop(e.getExpToDrop() * quartsXPRate);

            return;
        }

        if (e.getBlock().getType() == Material.LEAVES || e.getBlock().getType() == Material.LEAVES_2) {
            e.getBlock().getDrops().clear();

            if (e.getPlayer().getItemInHand().getType() != Material.SHEARS) return;
            if (Scenario.getRandom().nextInt(100) > ((int) MatchAttribute.getAttributeFromName("Apple Rate").getCurrentSelection().getValue())) ItemUtil.dropItem(new ItemStack(Material.APPLE), e.getBlock().getLocation());
        }

    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent e) {
        e.getBlock().getDrops().removeIf(item -> item.getType() == Material.APPLE);
        if (((int) MatchAttribute.getAttributeFromName("Apple Rate").getCurrentSelection().getValue()) > Scenario.getRandom().nextInt(100)) ItemUtil.dropItem(new ItemStack(Material.APPLE), e.getBlock().getLocation());
    }

/*    public void dropItems(BlockBreakEvent e) {
        if (e.isCancelled()) return;

        if (e.getBlock().getDrops().size() > 0) {
            for (int i = 0; i < e.getBlock().getDrops().size(); i++)
                ItemDropUtil.naturallyDropItem((ItemStack) e.getBlock().getDrops().toArray()[i], e.getBlock().getLocation());
        }
    }*/
}

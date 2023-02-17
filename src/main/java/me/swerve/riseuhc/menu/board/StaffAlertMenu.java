package me.swerve.riseuhc.menu.board;

import me.swerve.riseuhc.menu.Menu;
import me.swerve.riseuhc.menu.Page;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.util.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.Collections;

public class StaffAlertMenu extends Menu {
    public StaffAlertMenu(Player p) {
        super("Staff Alerts", InventoryType.INTERACTABLE, PageInformation.SINGLE_PAGE);

        Page mainPage = new Page(27);

        for (int i = 0; i < 27; i++) {
            int glassData = 0;
            if (i % 2 == 0) glassData = 1;
            mainPage.put(i, new ItemCreator(Material.STAINED_GLASS_PANE, 1).setData(glassData).getItem());
        }

        for (int i = 9; i < 18; i++) mainPage.put(i, new ItemStack(Material.AIR));

        mainPage.put(9, new ItemCreator(Material.WOOL, 1).setData(14).setName("&cDisable All").addLore(Collections.singletonList("&7Click to disable all alerts!")).getItem());
        mainPage.put(17, new ItemCreator(Material.WOOL, 1).setData(5).setName("&cEnable All").addLore(Collections.singletonList("&7Click to enable all alerts!")).getItem());

        mainPage.put(11, new ItemCreator(Material.DIAMOND_ORE, 1).setName("&6Diamond Mine Alerts").addLore(Collections.singletonList("&7Click to toggle diamond alerts!")).getItem());
        mainPage.put(12, new ItemCreator(Material.GOLD_ORE, 1).setName("&6Gold Mine Alerts").addLore(Collections.singletonList("&7Click to toggle gold alerts!")).getItem());

        mainPage.put(14, new ItemCreator(Material.DIAMOND_SWORD, 1).setName("&6Fight Alerts").addLore(Collections.singletonList("&7Click to toggle fight alerts!")).getItem());
        mainPage.put(15, new ItemCreator(Material.NETHER_STAR, 1).setName("&6Nether Enter Alerts").addLore(Collections.singletonList("&7Click to toggle Nether Enter alerts!")).getItem());

        addPage(mainPage);

        updateInventory(p);
    }

    @Override
    public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) {
        UHCPlayer player = UHCPlayer.getUhcPlayers().get(e.getWhoClicked().getUniqueId());

        if (e.getCurrentItem().getType() == Material.WOOL) {
            if (e.getCurrentItem().getData().getData() == (byte) 14) {
                player.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fAll of your staff alerts have been disabled."));
                setValues(player, false);

                return;
            }

            player.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fAll of your staff alerts have been enabled."));
            setValues(player, true);

            return;
        }

        if (e.getCurrentItem().getType() == Material.DIAMOND_ORE) {
            if (player.isDiamondStaffAlert()) {
                player.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fDiamond alerts are now disabled."));
                player.setDiamondStaffAlert(false);
                return;
            }

            player.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fDiamond alerts are now enabled."));
            player.setDiamondStaffAlert(true);
            return;
        }

        if (e.getCurrentItem().getType() == Material.GOLD_ORE) {
            if (player.isGoldStaffAlert()) {
                player.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fGold alerts are now disabled."));
                player.setGoldStaffAlert(false);
                return;
            }

            player.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fGold alerts are now enabled."));
            player.setGoldStaffAlert(true);
            return;
        }

        if (e.getCurrentItem().getType() == Material.DIAMOND_SWORD) {
            if (player.isFightStaffAlert()) {
                player.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fFight alerts are now disabled."));
                player.setFightStaffAlert(false);
                return;
            }

            player.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fFight alerts are now enabled."));
            player.setFightStaffAlert(true);
            return;
        }

        if (e.getCurrentItem().getType() == Material.NETHER_STAR) {
            if (player.isNetherEnterStaffAlert()) {
                player.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNether alerts are now disabled."));
                player.setNetherEnterStaffAlert(false);
                return;
            }

            player.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fNether alerts are now enabled."));
            player.setNetherEnterStaffAlert(true);
        }
    }

    private void setValues(UHCPlayer player, boolean value) {
        player.setDiamondStaffAlert(value);
        player.setGoldStaffAlert(value);
        player.setFightStaffAlert(value);
        player.setNetherEnterStaffAlert(value);
    }

    @Override public void lastChance(Inventory inventory) { }
}

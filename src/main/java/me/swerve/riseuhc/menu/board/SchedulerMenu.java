package me.swerve.riseuhc.menu.board;

import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.menu.Menu;
import me.swerve.riseuhc.menu.Page;
import me.swerve.riseuhc.runnable.SchedulerRunnable;
import me.swerve.riseuhc.util.ItemCreator;
import me.swerve.riseuhc.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.ParseException;
import java.util.*;

public class SchedulerMenu extends Menu {

    public SchedulerMenu(Player p) {
        super("Scheduler", InventoryType.INTERACTABLE, PageInformation.SINGLE_PAGE);

        Page mainPage = new Page(27);

        for (int i = 0; i < 27; i++) {
            int glassData = 0;
            if (i % 2 == 0) glassData = 1;
            mainPage.put(i, new ItemCreator(Material.STAINED_GLASS_PANE, 1).setData(glassData).getItem());
        }

        UHCManager.getInstance().getFormatter().setTimeZone(TimeZone.getTimeZone("UTC"));

        String currentDate = UHCManager.getInstance().getFormatter().format(new Date());
        currentDate = currentDate.substring(0, currentDate.length() - 2) + "00";

        mainPage.put(9, new ItemCreator(Material.WOOL, 1).setData(14).setName("&c-15 Minutes").addLore(Collections.singletonList("&7Remove 15 Minutes")).getItem());
        mainPage.put(10, new ItemCreator(Material.WOOL, 1).setData(14).setName("&c-5 Minutes").addLore(Collections.singletonList("&7Remove 5 Minutes")).getItem());
        mainPage.put(11, new ItemCreator(Material.WOOL, 1).setData(14).setName("&c-1 Minute").addLore(Collections.singletonList("&7Remove 1 Minute")).getItem());

        mainPage.put(13, new ItemCreator(Material.BOOK, 1).setName("&6Time").addLore(Collections.singletonList("&7" + currentDate)).getItem());
        mainPage.put(22, new ItemCreator(Material.ANVIL, 1).setName("&6Schedule").addLore(mainPage.getPageContents().get(13).getItemMeta().getLore()).getItem());

        mainPage.put(15, new ItemCreator(Material.WOOL, 1).setData(5).setName("&4+1 Minute").addLore(Collections.singletonList("&7Add 1 Minute")).getItem());
        mainPage.put(16, new ItemCreator(Material.WOOL, 1).setData(5).setName("&4+5 Minutes").addLore(Collections.singletonList("&7Add 5 Minutes")).getItem());
        mainPage.put(17, new ItemCreator(Material.WOOL, 1).setData(5).setName("&4+15 Minutes").addLore(Collections.singletonList("&7Add 15 Minutes")).getItem());

        mainPage.put(12, new ItemStack(Material.AIR));
        mainPage.put(14, new ItemStack(Material.AIR));

        addPage(mainPage);

        updateInventory(p);
    }

    public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) {
        if (e.getCurrentItem().getType() == Material.BOOK) {
            Date firstDate = new Date();

            Date secondDate = null;
            try { secondDate = UHCManager.getInstance().getFormatter().parse(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getLore().get(0)));
            } catch (ParseException ignored) {}

            if (secondDate == null) {
                ((Player) e.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere was an error!"));
                e.getWhoClicked().closeInventory();
                return;
            }

            ((Player) e.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cDifference in Minutes: " + TimeUtil.differenceInMinutes(secondDate, firstDate)));
            return;
        }

        if (e.getCurrentItem().getType() == Material.WOOL) {
            String itemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
            int toAdd = Integer.parseInt(itemName.split(" ")[0]);

            Date newDate = null;
            try { newDate = UHCManager.getInstance().getFormatter().parse(ChatColor.stripColor(currentPage.getPageContents().get(13).getItemMeta().getLore().get(0))); }
            catch (Exception ignored) {}

            if (newDate == null) {
                ((Player) e.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere was an error!"));
                e.getWhoClicked().closeInventory();
                return;
            }

            newDate = TimeUtil.addMinutesToJavaUtilDate(newDate, toAdd);

            ItemStack stack = currentPage.getPageContents().get(13);
            currentPage.put(13, new ItemCreator(stack.getType(), 1).setName(stack.getItemMeta().getDisplayName()).addLore(Collections.singletonList("&7" + UHCManager.getInstance().getFormatter().format(newDate))).getItem());

            updateInventory((Player) e.getWhoClicked());
            return;
        }

        if (e.getCurrentItem().getType() == Material.ANVIL) {
            Date firstDate = new Date();

            Date secondDate = null;
            try { secondDate = UHCManager.getInstance().getFormatter().parse(ChatColor.stripColor(currentPage.getPageContents().get(13).getItemMeta().getLore().get(0))); }
            catch (ParseException ignored) {}

            if (secondDate == null) {
                ((Player) e.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere was an error!"));
                e.getWhoClicked().closeInventory();
                return;
            }

            if (TimeUtil.differenceInMinutes(secondDate, firstDate) <= 0) {
                ((Player) e.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't schedule a game for " + TimeUtil.differenceInMinutes(secondDate, firstDate) + " minutes in the past!"));
                return;
            }

            UHCManager.getInstance().setScheduleDate(secondDate);
            new SchedulerRunnable().runTaskTimer(RiseUHC.getInstance(), 0, 10);
            e.getWhoClicked().closeInventory();

            return;
        }
    }

    public void lastChance(Inventory inventory) { }
}
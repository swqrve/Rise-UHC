package me.swerve.riseuhc.menu.board;

import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.menu.Menu;
import me.swerve.riseuhc.menu.Page;
import me.swerve.riseuhc.util.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class ConfigMenu extends Menu {

    public ConfigMenu(Player p) {
        super("Config", InventoryType.INTERACTABLE, PageInformation.SINGLE_PAGE);

        Page mainPage = new Page(27);

        for (int i = 0; i < 27; i++) {
            int glassData = 0;
            if (i % 2 == 0) glassData = 1;
            mainPage.put(i, new ItemCreator(Material.STAINED_GLASS_PANE, 1).setData(glassData).getItem());
        }

        List<String> borderInfo = new ArrayList<>(), teamInfo= new ArrayList<>(), healingInfo= new ArrayList<>(), miningInfo= new ArrayList<>(), timingInfo = new ArrayList<>(), netherInfo= new ArrayList<>(), generalInfo = new ArrayList<>();

        for (MatchAttribute attribute : MatchAttribute.getAttributes()) {
            switch (attribute.getSection()) {
                case BORDER:
                    borderInfo.add("&f" + attribute.getName() + ": &6" + attribute.getCurrentSelection().getName());
                    break;
                case TEAM_INFORMATION:
                    teamInfo.add("&f" + attribute.getName() + ": &6" + attribute.getCurrentSelection().getName());
                    break;
                case GENERATION:
                case MINING:
                    miningInfo.add("&f" + attribute.getName() + ": &6" + attribute.getCurrentSelection().getName());
                    break;
                case TIMING:
                    timingInfo.add("&f" + attribute.getName() + ": &6" + attribute.getCurrentSelection().getName());
                    break;
                case GENERAL:
                    generalInfo.add("&f" + attribute.getName() + ": &6" + attribute.getCurrentSelection().getName());
                    break;
                case HEALING:
                    healingInfo.add("&f" + attribute.getName() + ": &6" + attribute.getCurrentSelection().getName());
                    break;
                case NETHER:
                    netherInfo.add("&f" + attribute.getName() + ": &6" + attribute.getCurrentSelection().getName());
                    break;
            }
        }

        mainPage.put(10, new ItemCreator(Material.BEDROCK, 1).setName("&6Border").addLore(borderInfo).getItem());
        mainPage.put(11, new ItemCreator(Material.NAME_TAG, 1).setName("&6Team Info").addLore(teamInfo).getItem());
        mainPage.put(12, new ItemCreator(Material.GOLDEN_APPLE, 1).setName("&6Healing").addLore(healingInfo).getItem());
        mainPage.put(13, new ItemCreator(Material.COAL_ORE, 1).setName("&6Mining").addLore(miningInfo).getItem());
        mainPage.put(14, new ItemCreator(Material.WATCH, 1).setName("&6Timing").addLore(timingInfo).getItem());
        mainPage.put(15, new ItemCreator(Material.NETHER_STAR, 1).setName("&6Nether").addLore(netherInfo).getItem());
        mainPage.put(16, new ItemCreator(Material.ANVIL, 1).setName("&6General Info").addLore(generalInfo).getItem());

        addPage(mainPage);

        updateInventory(p);
    }

    public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) { }

    public void lastChance(Inventory inventory) { }
}
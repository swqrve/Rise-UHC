package me.swerve.riseuhc.menu.board;

import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.menu.Menu;
import me.swerve.riseuhc.menu.Page;
import me.swerve.riseuhc.util.ItemCreator;
import me.swerve.riseuhc.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageMenu extends Menu {
    private final boolean override;

    public ManageMenu(Player p, boolean override) {
        super("UHC Manager", InventoryType.INTERACTABLE, PageInformation.SINGLE_PAGE);
        this.override = override;

        Page mainPage = new Page(18, "main");

        mainPage.put(0, new ItemCreator(Material.NAME_TAG, 1).setName("&6Border").addLore(Collections.singletonList("&7Click to view Border Settings")).getItem());
        mainPage.put(1, new ItemCreator(Material.NAME_TAG, 1).setName("&6Team Information").addLore(Collections.singletonList("&7Click to view Team Information")).getItem());
        mainPage.put(2, new ItemCreator(Material.NAME_TAG, 1).setName("&6Healing").addLore(Collections.singletonList("&7Click to view Healing Settings")).getItem());
        mainPage.put(3, new ItemCreator(Material.NAME_TAG, 1).setName("&6Generate Settings").addLore(Collections.singletonList("&7Click to view Generation Settings")).getItem());
        mainPage.put(9, new ItemCreator(Material.NAME_TAG, 1).setName("&6Mining").addLore(Collections.singletonList("&7Click to view Mining Information")).getItem());
        mainPage.put(10, new ItemCreator(Material.NAME_TAG, 1).setName("&6Timing").addLore(Collections.singletonList("&7Click to view Timing Information")).getItem());
        mainPage.put(11, new ItemCreator(Material.NAME_TAG, 1).setName("&6Misc").addLore(Collections.singletonList("&7Click to view Miscellaneous Information")).getItem());
        mainPage.put(12, new ItemCreator(Material.NAME_TAG, 1).setName("&6Nether").addLore(Collections.singletonList("&7Click to view Nether Information")).getItem());

        mainPage.put(7, new ItemCreator(Material.BOOK, 1).setName("&6Edit Scenarios").addLore(Collections.singletonList("&7Click to edit Scenarios")).getItem());
        mainPage.put(8, new ItemCreator(Material.LEVER, 1).setName("&6Force Start").addLore(Collections.singletonList("&7Click to start the UHC")).getItem());
        mainPage.put(17, new ItemCreator(Material.TRIPWIRE_HOOK, 1).setName("&6Generate World").addLore(Collections.singletonList("&7Click to start generating the world")).getItem());

        addPage(mainPage);

        Page borderPage = new Page(9, "Border"), healingPage = new Page(9, "Healing"), teamInfoPage = new Page(9, "Team Information"), generateInfoPage = new Page(9, "Generate Settings"), miningInfoPage = new Page(9, "Mining"), timingPage = new Page(9, "Timing"), miscPage = new Page(9, "Misc"), netherPage = new Page(9, "Nether");

        addPage(borderPage);
        addPage(teamInfoPage);
        addPage(healingPage);
        addPage(generateInfoPage);
        addPage(miningInfoPage);
        addPage(timingPage);
        addPage(miscPage);
        addPage(netherPage);

        for (MatchAttribute attribute : MatchAttribute.getAttributes()) {
            for (Page page : getPages()) {
                if (page.getIdentifier().equalsIgnoreCase("main")) continue;
                if (page.getIdentifier().equalsIgnoreCase(attribute.getSection().getPageIdentifier())) {
                    ItemCreator creator = new ItemCreator(Material.PAPER, 1).setName("&6" + attribute.getName());
                    ItemStack result = updateDescription(creator, attribute);
                    if (getNextAvailable(page) != -1) page.put(getNextAvailable(page), result);
                }
            }
        }

        for (Page page : getPages()) {
            if (page.getIdentifier().equalsIgnoreCase("main")) continue;
            page.put(8, new ItemCreator(Material.ARROW, 1).setName("&6Back...").getItem());
        }

        updateInventory(p);
    }

    @Override
    public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) {
        if (e.getCurrentItem().getType() == Material.NAME_TAG)  {
            String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

            for (int i = 0; i < getPages().size(); i++) {
                Page page = getPages().get(i);
                if (page.getIdentifier().equalsIgnoreCase(name)) setCurrentPage(i);
            }

            updateInventory((Player) e.getWhoClicked());
            return;
        }

        if (e.getCurrentItem().getType() == Material.ARROW) {
            setCurrentPage(0);

            updateInventory((Player) e.getWhoClicked());
            return;
        }

        if (e.getCurrentItem().getType() == Material.PAPER) {
            MatchAttribute attribute = null;
            for (MatchAttribute atr : MatchAttribute.getAttributes()) if (atr.getName().equalsIgnoreCase(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()))) attribute = atr;
            if (attribute == null) return;

            int index = 0;
            for (int i = 0; i < attribute.getOptions().size(); i++) if (attribute.getOptions().get(i).getName().equals(attribute.getCurrentSelection().getName())) index = i;

            if (e.getClick().toString().toLowerCase().contains("left")) {
                if (index + 1 == attribute.getOptions().size()) index = -1;
                index++;
            } else {
                if (index == 0) index = attribute.getOptions().size();
                index--;
            }

            attribute.setCurrentSelection(attribute.getOptions().get(index));

            for (Page page : getPages()) {
                if (page.getIdentifier().equalsIgnoreCase("main")) continue;
                if (page.getIdentifier().equalsIgnoreCase(attribute.getSection().getPageIdentifier())) {
                    ItemCreator creator = new ItemCreator(Material.PAPER, 1).setName("&6" + attribute.getName());
                    ItemStack result = updateDescription(creator, attribute);

                    int properPosition = 0;
                    for (int i = 0; i < 7; i++) {
                        if (page.getPageContents().get(i) == null) continue;
                        if (ChatColor.stripColor(page.getPageContents().get(i).getItemMeta().getDisplayName()).equalsIgnoreCase(attribute.getName())) properPosition = i;
                    }

                    page.put(properPosition, result);
                }
            }

            updateInventory((Player) e.getWhoClicked());
            return;
        }

        if (e.getCurrentItem().getType() == Material.TRIPWIRE_HOOK) {
            e.getWhoClicked().closeInventory();
            WorldManager.getInstance().generate(override);
            return;
        }

        if (e.getCurrentItem().getType() == Material.LEVER) {
            if (UHCManager.getInstance().getGame() != null) {
                ((Player) e.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cA game has already been started!"));
                return;
            }

            UHCManager.getInstance().startUHC();
            return;
        }

        if (e.getCurrentItem().getType() == Material.BOOK) Bukkit.getPluginManager().registerEvents(new ScenarioEditMenu((Player) e.getWhoClicked(), override), RiseUHC.getInstance());
    }

    @Override public void lastChance(Inventory inventory) { }

    private int getNextAvailable(Page page) {
        int toReturn = -1;
        for (int i = 6; i >= 0; i--) if (page.getPageContents().get(i) == null) toReturn = i;
        return toReturn;
    }

    private ItemStack updateDescription(ItemCreator creator, MatchAttribute attribute) {
        List<String> description = new ArrayList<>();
        description.add("");

        for (int i = 0; i < attribute.getOptions().size(); i++) {
            AttributeValue value = attribute.getOptions().get(i);

            if (value.getName().equals(attribute.getCurrentSelection().getName())) description.add("&7\u25CF &a" + value.getName());
            else description.add("&7\u25CF &c" + value.getName());
        }

        description.add("");
        return creator.addLore(description).getItem();
    }
}
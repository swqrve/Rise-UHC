package me.swerve.riseuhc.menu.board;

import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.menu.Menu;
import me.swerve.riseuhc.menu.Page;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScenarioEditMenu extends Menu {
    private final boolean overrideSaved;
    public ScenarioEditMenu(Player player, boolean overrideSaved) {
        super("Scenarios", InventoryType.INTERACTABLE, PageInformation.MULTI_PAGE, new ItemCreator(Material.STAINED_GLASS_PANE, 1).setData(1).getItem());
        this.overrideSaved = overrideSaved;

        int neededPages = (int) Math.ceil((float) Scenario.getScenarios().size() / 18);

        List<Scenario> toLoad = new ArrayList<>(Scenario.getScenarios());

        for (int i = 0; i < neededPages; i++) {
            Page page = new Page(18);

            for (int b = 0; b < 18; b++) {
                if (toLoad.size() < 1) break;
                Scenario scenario = toLoad.get(0);
                ItemCreator creator = new ItemCreator(scenario.getSymbol(), 1).setName("&6" + scenario.getTitle());

                List<String> description = new ArrayList<>();

                if (scenario.isEnabled()) description.add(ChatColor.translateAlternateColorCodes('&', "&aOn"));
                else description.add(ChatColor.translateAlternateColorCodes('&', "&cOn"));

                if (!scenario.isEnabled()) description.add(ChatColor.translateAlternateColorCodes('&', "&aOff"));
                else description.add(ChatColor.translateAlternateColorCodes('&', "&cOff"));

                creator.addLore(description);

                page.put(b, creator.getItem());
                toLoad.remove(0);
            }

            addPage(page);
        }

        if (neededPages == 0) addPage(new Page(18));
        updateInventory(player);
    }

    @Override
    public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) {
        if (e.getCurrentItem().getType() == Material.AIR) return;

        if (e.getCurrentItem().getType() == Material.ANVIL && ChatColor.stripColor( e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Management Menu")) {
            Bukkit.getPluginManager().registerEvents(new ManageMenu((Player) e.getWhoClicked(), overrideSaved), RiseUHC.getInstance());
            return;
        }

        Scenario scenario = Scenario.getScenarioByTitle(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
        if (scenario == null) return;

        scenario.setState(!scenario.isEnabled());

        int currentPageID = 0;
        for (int i = 0; i < getPages().size(); i++) if (Arrays.equals(getPages().get(i).getPageContents().values().toArray(), currentPage.getPageContents().values().toArray())) currentPageID = i;

        Page page = new Page(currentPage.getPageSize());
        for (Integer keySet : currentPage.getPageContents().keySet()) {
            ItemStack value = currentPage.getPageContents().get(keySet);

            if (ChatColor.stripColor(value.getItemMeta().getDisplayName()).equalsIgnoreCase(ChatColor.stripColor(scenario.getTitle()))) {
                ItemCreator creator = new ItemCreator(scenario.getSymbol(), 1).setName("&6" + scenario.getTitle());

                List<String> description = new ArrayList<>();

                if (scenario.isEnabled()) description.add(ChatColor.translateAlternateColorCodes('&', "&aOn"));
                else description.add(ChatColor.translateAlternateColorCodes('&', "&cOn"));

                if (!scenario.isEnabled()) description.add(ChatColor.translateAlternateColorCodes('&', "&aOff"));
                else description.add(ChatColor.translateAlternateColorCodes('&', "&cOff"));

                creator.addLore(description);

                page.put(keySet, creator.getItem());
            } else page.put(keySet, value);
        }

        getPages().add(currentPageID, page);
        getPages().remove(currentPage);
        updateInventory((Player) e.getWhoClicked());
    }

    @Override public void lastChance(Inventory inventory) {
        inventory.setItem(22, new ItemCreator(Material.ANVIL, 1).setName("&6Management Menu").addLore(Collections.singletonList("&7Click to go to the Management Window")).getItem());
    }

}

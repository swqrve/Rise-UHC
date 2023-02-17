package me.swerve.riseuhc.menu.board;

import me.swerve.riseuhc.menu.Menu;
import me.swerve.riseuhc.menu.Page;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class ScenarioMenu extends Menu {
    public ScenarioMenu(Player player) {
        super("Scenarios", InventoryType.NON_INTERACTABLE, PageInformation.MULTI_PAGE, new ItemCreator(Material.STAINED_GLASS_PANE, 1).setData(1).getItem());

        int neededPages = (int) Math.ceil((float) Scenario.getEnabledScenarios().size() / 9);

        List<Scenario> toLoad = new ArrayList<>(Scenario.getEnabledScenarios());

        for (int i = 0; i < neededPages; i++) {
            Page page = new Page(9);

            for (int b = 0; b < 9; b++) {
                if (toLoad.size() < 1) break;
                Scenario scenario = toLoad.get(0);
                page.put(b, new ItemCreator(scenario.getSymbol(), 1).setName("&6" + scenario.getTitle()).addLore(scenario.getDescription()).getItem());
                toLoad.remove(0);
            }

            addPage(page);
        }

        if (neededPages == 0) addPage(new Page(9));
        updateInventory(player);
    }

    @Override
    public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) {

    }

    @Override public void lastChance(Inventory inventory) { }
}

package me.swerve.riseuhc.menu.board;

import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.menu.Menu;
import me.swerve.riseuhc.menu.Page;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.util.ItemCreator;
import me.swerve.riseuhc.util.SortUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiamondMenu extends Menu {
    public DiamondMenu(Player p) {
        super("Diamond Top", InventoryType.NON_INTERACTABLE, PageInformation.MULTI_PAGE, new ItemCreator(Material.STAINED_GLASS_PANE, 1).setData(1).getItem());

        List<UHCPlayer> playersToSort = new ArrayList<>();

        for (UHCPlayer player : UHCManager.getInstance().getGame().getGamePlayers()) { if (player.getCurrentDiamondsMined() > 1) playersToSort.add(player); }

        if (playersToSort.size() < 1) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is nobody to display!"));
            HandlerList.unregisterAll(this);
            return;
        }

        SortUtil.sortUHCPlayersByDiamonds(playersToSort);

        int neededPages = (int) Math.ceil((float) playersToSort.size() / 18);

        for (int i = 0; i < neededPages; i++) {
            Page page = new Page(18);

            for (int b = 0; b < 18; b++) {
                if (playersToSort.size() < 1) break;
                UHCPlayer player = playersToSort.get(0);

                page.put(b, new ItemCreator(Material.SKULL_ITEM, 1).setName("&6" + player.getPlayerObject().getDisplayName()).setOwner(player.getPlayerObject().getName()).addLore(Arrays.asList("&bDiamonds &7Mined: " + player.getCurrentDiamondsMined(), "&7Click to teleport to the player!")).getItem());
                playersToSort.remove(0);
            }

            addPage(page);
        }

        updateInventory(p);
    }

    @Override public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) { }

    @Override public void lastChance(Inventory inventory) { }
}

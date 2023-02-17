package me.swerve.riseuhc.menu;

import lombok.Getter;
import lombok.Setter;
import me.swerve.riseuhc.util.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Menu implements Listener {
    public enum InventoryType { INTERACTABLE, NON_INTERACTABLE };
    public enum PageInformation { SINGLE_PAGE, MULTI_PAGE }

    @Getter private final List<Page> pages = new ArrayList<>();

    @Getter @Setter private int currentPage = 0;
    @Getter @Setter private String title;

    @Getter private final InventoryType inventoryType;
    @Getter private final PageInformation pageInformation;

    @Getter @Setter private Inventory currentInventory;

    @Getter private final ItemStack fillStack;

    private final ItemStack previousArrow = new ItemCreator(Material.ARROW, 1).setName("&6Previous Page").getItem();
    private final ItemStack nextArrow = new ItemCreator(Material.ARROW, 1).setName("&6Next Page").getItem();

    public Menu(String title, InventoryType type, PageInformation info, ItemStack fillStack) {
        this.title = title;

        this.inventoryType = type;
        this.pageInformation = info;

        this.fillStack = fillStack;
    }

    public Menu(String title, InventoryType type, PageInformation info) {
        this.title = title;

        this.inventoryType = type;
        this.pageInformation = info;

        this.fillStack = null;
    }

    public void addPage(Page page) {
        pages.add(page);
    }

    public void updateInventory(Player player) {
        Page selectedPage = pages.get(currentPage);

        int pageSize = selectedPage.getPageSize();

        boolean hasNextPage = false;
        boolean hasPreviousPage = false;
        if (pageInformation == PageInformation.MULTI_PAGE) {
            pageSize += 9;
            if (pages.size() > currentPage + 1) hasNextPage = true;
            if (currentPage > 0) hasPreviousPage = true;
        }

        if (pageSize % 9 != 0) {
            System.out.println("[Menu] There was an error with the menu api, you used an unsafe inventory size on page " + currentPage);
            return;
        }

        Inventory menu = Bukkit.createInventory(null, pageSize, title);

        for (int key : selectedPage.getPageContents().keySet()) menu.setItem(key, selectedPage.getPageContents().get(key));

        if (pageInformation == PageInformation.MULTI_PAGE) for (int i = pageSize - 9; i < pageSize; i++) menu.setItem(i, fillStack);

        if (hasNextPage) menu.setItem(pageSize - 1, nextArrow);
        if (hasPreviousPage) menu.setItem(pageSize - 9, previousArrow);

        currentInventory = menu;
        lastChance(currentInventory);
        player.openInventory(menu);
    }

    @EventHandler
    public void onPlayerInventoryInteract(InventoryClickEvent e) {
        if (currentInventory == null) return;
        if (!(Arrays.equals(e.getInventory().getContents(), currentInventory.getContents()) && currentInventory.getTitle().equalsIgnoreCase(e.getInventory().getTitle()) && Arrays.equals(currentInventory.getViewers().toArray(), e.getInventory().getViewers().toArray()))) return;

        e.setCancelled(true);

        if (e.getCurrentItem() != null) {
            if (e.getCurrentItem().isSimilar(nextArrow)) {
                setCurrentPage(getCurrentPage() + 1);
                updateInventory((Player) e.getWhoClicked());
                return;
            }

            if (e.getCurrentItem().isSimilar(previousArrow)) {
                setCurrentPage(getCurrentPage() - 1);
                updateInventory((Player) e.getWhoClicked());
                return;
            }

            if (inventoryType == InventoryType.INTERACTABLE) clickedItem(e.getInventory(), e, pages.get(currentPage));
        }
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent e) {
        if (currentInventory == null) return;
        if (!(Arrays.equals(e.getInventory().getContents(), currentInventory.getContents()) && currentInventory.getTitle().equalsIgnoreCase(e.getInventory().getTitle()) && Arrays.equals(currentInventory.getViewers().toArray(), e.getInventory().getViewers().toArray()))) return;

        currentInventory = null;
        HandlerList.unregisterAll(this);
    }

    public abstract void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage);
    public abstract void lastChance(Inventory inventory);
}

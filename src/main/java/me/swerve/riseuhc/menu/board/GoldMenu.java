package me.swerve.riseuhc.menu.board;

import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.menu.Menu;
import me.swerve.riseuhc.menu.Page;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.util.ItemCreator;
import me.swerve.riseuhc.util.SortUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoldMenu extends Menu {
    public GoldMenu(Player p) {
        super("Gold Top", InventoryType.NON_INTERACTABLE, PageInformation.MULTI_PAGE, new ItemCreator(Material.STAINED_GLASS_PANE, 1).setData(1).getItem());

        List<UHCPlayer> playersToSort = new ArrayList<>();

        for (UHCPlayer player : UHCManager.getInstance().getGame().getGamePlayers()) { if (player.getCurrentGoldMined() > 1) playersToSort.add(player); }

        if (playersToSort.size() < 1) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is nobody to display!"));
            HandlerList.unregisterAll(this);
            return;
        }

        SortUtil.sortUHCPlayersByGold(playersToSort);

        int neededPages = (int) Math.ceil((float) playersToSort.size() / 18);

        for (int i = 0; i < neededPages; i++) {
            Page page = new Page(18);

            for (int b = 0; b < 18; b++) {
                if (playersToSort.size() < 1) break;
                UHCPlayer player = playersToSort.get(0);

                page.put(b, new ItemCreator(Material.SKULL_ITEM, 1).setName("&6" + player.getPlayerObject().getDisplayName()).addLore(Arrays.asList("&6Gold &7Mined: " + player.getCurrentGoldMined(), "&7Click to teleport to the player!")).setOwner(player.getPlayerObject().getName()).getItem());
                playersToSort.remove(0);
            }

            addPage(page);
        }

        updateInventory(p);
    }

    @Override public void clickedItem(Inventory inventory, InventoryClickEvent e, Page currentPage) {
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() != Material.SKULL_ITEM) return;

        String playerName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
        for (Player p : Bukkit.getOnlinePlayers()) if (ChatColor.stripColor(p.getDisplayName()).equalsIgnoreCase(playerName)) {
            e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTeleporting you to " + playerName + "!"));
            e.getWhoClicked().teleport(p.getLocation());
            return;
        }

        e.getWhoClicked().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCouldn't find that player, did they log off?"));
    }

    @Override public void lastChance(Inventory inventory) { }
}

package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.RiseUHC;
import net.minecraft.server.v1_8_R3.ContainerEnchantTable;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryView;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class EnchantListener implements Listener {

    private final Random random = new Random();

    @EventHandler
    public void onPrepareEnchant(PrepareItemEnchantEvent e){
        CraftInventoryView view = (CraftInventoryView) e.getView();
        ContainerEnchantTable table = (ContainerEnchantTable) view.getHandle();
        table.f = random.nextInt();

        generateNewCosts(table.costs, random, Math.min(e.getEnchantmentBonus(), 15));

        RiseUHC.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(RiseUHC.getInstance(), ()-> {
            Arrays.fill(table.h, -1);
        }, 1);
    }

    @EventHandler
    public void onItemEnchant(EnchantItemEvent e){
        RiseUHC.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(RiseUHC.getInstance(), ()-> {
            e.getEnchanter().setLevel(e.getEnchanter().getLevel() - (e.getExpLevelCost() - (64 - e.getInventory().getItem(1).getAmount())));
            e.getInventory().setItem(1, new ItemStack(Material.INK_SACK, 64, (short) 4));
        }, 1);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(e.getClickedInventory() != null && e.getClickedInventory().getType().equals(InventoryType.ENCHANTING)) {
            if(e.getRawSlot() == 1) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e){
        if(e.getInventory() != null && e.getInventory().getType().equals(InventoryType.ENCHANTING)) {
            e.getInventory().setItem(1, new ItemStack(Material.INK_SACK, 64, (short) 4));
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e){
        if(e.getInventory() != null && e.getInventory().getType().equals(InventoryType.ENCHANTING)) {
            e.getInventory().setItem(1, null);
        }
    }

    private void generateNewCosts(int[] costs, Random rand, int books){
        int base = (rand.nextInt(8) + 1)
                + (books > 0 ? rand.nextInt((int) Math.floor(books / 2))
                + rand.nextInt(books)
                : 0);
        costs[0] = Math.max(base / 3, 1);
        costs[1] = (base * 2) / 3 + 1;
        int first = Math.min(base, books * 2);
        int last = Math.max(base, books * 2);
        costs[2] = ThreadLocalRandom.current().nextInt(first, last) + 1;// Before v1.1
        // table.costs[2] = Math.max(base, books * 2);//Idk what version
    }

}

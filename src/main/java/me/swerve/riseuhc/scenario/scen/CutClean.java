package me.swerve.riseuhc.scenario.scen;

import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import java.util.Collections;

public class CutClean extends Scenario {
    public CutClean() {
        super("CutClean", Material.IRON_INGOT, Collections.singletonList(
                "&fAll ores and animal food will be dropped in it's smelted version."
        ));
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent e) {
        switch (e.getEntity().getType()) {
            case SHEEP:
                e.getDrops().clear();
                e.getDrops().add(new ItemStack(Material.COOKED_BEEF, Scenario.getRandom().nextInt(3) + 1));
                e.getDrops().add(new ItemStack(Material.WOOL, Scenario.getRandom().nextInt(2) + 1));
                break;
            case COW:
                e.getDrops().clear();
                e.getDrops().add(new ItemStack(Material.COOKED_BEEF, Scenario.getRandom().nextInt(3) + 1));
                e.getDrops().add(new ItemStack(Material.LEATHER));
                break;
            case CHICKEN:
                e.getDrops().clear();
                e.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, Scenario.getRandom().nextInt(3) + 1));
                e.getDrops().add(new ItemStack(Material.FEATHER, Scenario.getRandom().nextInt(2) + 1));
                break;
            case PIG:
                e.getDrops().clear();
                e.getDrops().add(new ItemStack(Material.GRILLED_PORK, Scenario.getRandom().nextInt(3) + 1));
                break;
            case HORSE:
                e.getDrops().clear();
                e.getDrops().add(new ItemStack(Material.LEATHER));
                break;
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        Block block = e.getBlock();
        switch (block.getType()) {
            case GOLD_ORE:
                // TODO: When bare bones is added...
                //if (Scenario.getScenarioByTitle("BareBones").isEnabled()) return;
                if (e.getPlayer().getItemInHand() == null) return;

                e.setCancelled(true);
                block.setType(Material.AIR);
                block.getState().update(true);
                if (e.getPlayer().getItemInHand().getType() == Material.DIAMOND_PICKAXE || e.getPlayer().getItemInHand().getType() == Material.IRON_PICKAXE) {
                    ItemUtil.dropItem(new ItemStack(Material.GOLD_INGOT, 1), block.getLocation());
                    block.getWorld().spawn(block.getLocation(), ExperienceOrb.class).setExperience(2);
                }
                break;
            case IRON_ORE:
                //if (Scenario.getScenarioByTitle("BareBones").isEnabled()) return;
                if (e.getPlayer().getItemInHand() == null) return;
                e.setCancelled(true);

                block.setType(Material.AIR);
                block.getState().update(true);
                ItemUtil.dropItem(new ItemStack(Material.IRON_INGOT, 1), block.getLocation());
                block.getWorld().spawn(block.getLocation(), ExperienceOrb.class).setExperience(2);

                break;
            case GRAVEL:
                e.setCancelled(true);

                block.setType(Material.AIR);
                block.getState().update(true);
                ItemUtil.dropItem(new ItemStack(Material.FLINT, 1), block.getLocation());

                break;
        }
    }
}


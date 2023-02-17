package me.swerve.riseuhc.util;

import de.inventivegames.hologram.Hologram;
import de.inventivegames.hologram.HologramAPI;
import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.manager.UHCManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TimeBombUtil {
    private final String name;
    private final Location location;
    private final List<ItemStack> drops;
    public TimeBombUtil(String name, List<ItemStack> drops, Location deathLocation) {
        this.name = name;
        this.drops = drops;
        this.location = deathLocation;

        handleDeath();
    }

    private void handleDeath() {
        location.getBlock().setType(Material.CHEST);
        location.getBlock().getRelative(BlockFace.EAST).setType(Material.CHEST);
        location.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
        location.clone().add(0, 1, 0).getBlock().getRelative(BlockFace.EAST).setType(Material.AIR);

        Chest chest = (Chest) location.getBlock().getState();

        for (ItemStack item : drops) {
            if (item == null || item.getType() == Material.AIR) continue;
            chest.getInventory().addItem(item);
        }

        chest.getInventory().addItem(UHCManager.getInstance().getGoldenHead());

        new TimeBombChest(name, chest).runTaskTimer(RiseUHC.getInstance(), 0, 20L);
    }

    public static class TimeBombChest extends BukkitRunnable {

        private final String name;
        private final Chest chest;
        private final Hologram hologram;
        private final Cooldown cooldown;

        TimeBombChest(String name, Chest chest) {
            this.name = name;
            this.chest = chest;

            this.cooldown = new Cooldown(30_000);
            this.hologram = HologramAPI.createHologram(chest.getLocation().clone().add(1, 1.5, 0.5), "§c");
            this.hologram.spawn();
            this.hologram.addLineAbove("§e" + name + "'s Corpse");
        }

        @Override
        public void run() {
            final String time = TimeUtil.millisToSeconds(this.cooldown.getRemaining());
            final String context = " second" + ((this.cooldown.getRemaining() / 1000) > 1 ? "s" : "");

            this.hologram.setText("§cExplodes in " + ChatColor.GREEN + time + context);

            if (this.cooldown.hasExpired()) {
                this.cancel();

                for (Hologram holo : this.hologram.getLines()) {
                    holo.despawn();
                }

                HologramAPI.removeHologram(this.hologram);

                this.chest.getBlockInventory().clear();
                if (this.chest.getLocation().getBlock().getRelative(BlockFace.EAST).getType() == Material.CHEST) {
                    Chest chest = (Chest) this.chest.getLocation().getBlock().getRelative(BlockFace.EAST).getState();
                    chest.getBlockInventory().clear();
                    this.chest.getBlock().breakNaturally();
                    chest.getBlock().breakNaturally();
                }
                this.chest.getLocation().getWorld().createExplosion(this.chest.getLocation(), 5f);

                Bukkit.broadcastMessage("§7[§6TimeBomb§7] §f" + this.name + "'s corpse has exploded!");
            }
        }
    }
}

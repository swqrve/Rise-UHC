package me.swerve.riseuhc.player.logger;

import lombok.Getter;
import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.scenario.scen.TimeBomb;
import me.swerve.riseuhc.util.ItemCreator;
import me.swerve.riseuhc.util.KillTopPlayer;
import me.swerve.riseuhc.util.TimeBombUtil;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class CombatLogger implements Listener {
    @Getter private static final Map<UUID, CombatLogger> loggers = new HashMap<>();

    @Getter private Villager logger;

    @Getter private final ItemStack[] contents;
    @Getter private final ItemStack[] armorContents;

    @Getter private final Location spawnLocation;

    @Getter private boolean dead = false;
    private final String playerName;

    @Getter private final int xp;

    @Getter private Chunk chunk;

    public CombatLogger(Player p) {
        this.contents = p.getInventory().getContents();
        this.armorContents = p.getInventory().getArmorContents();

        this.playerName = p.getDisplayName();

        this.spawnLocation = p.getLocation();

        this.xp = p.getTotalExperience();

        this.chunk = p.getLocation().getChunk();

        createLogger(p.getHealth());

        loggers.put(p.getUniqueId(), this);

        Bukkit.getScheduler().scheduleSyncDelayedTask(RiseUHC.getInstance(), () -> {
            if (dead) return;
            logger.setHealth(0);
        }, 10 * (20 * 60));
    }

    private void createLogger(double health) {
        logger = (Villager) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.VILLAGER);
        // setEntityNoAi(logger);

        logger.setProfession(Villager.Profession.PRIEST);

        logger.getEquipment().setHelmetDropChance(100);
        logger.getEquipment().setChestplateDropChance(100);
        logger.getEquipment().setLeggingsDropChance(100);
        logger.getEquipment().setBootsDropChance(100);

        logger.getEquipment().setArmorContents(armorContents);

        logger.setRemoveWhenFarAway(false);
        logger.setCanPickupItems(false);

        logger.setMaxHealth(20.0D);
        logger.setHealth(health);

        logger.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * (20*60), 255));

        logger.setCustomName(playerName);
        logger.setCustomNameVisible(true);
    }

    public void setEntityNoAi(Entity e) {
        net.minecraft.server.v1_8_R3.Entity nms = ((CraftEntity) e).getHandle();
        NBTTagCompound tag = new NBTTagCompound();
        nms.c(tag);
        tag.setBoolean("NoAI", true);

        EntityLiving entity = (EntityLiving) nms;
        entity.a(tag);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (!(e.getEntity() instanceof Villager)) return;

        if (e.getEntity().getUniqueId() == logger.getUniqueId()) {
            e.getDrops().clear();
            dead = true;

            e.getDrops().addAll(Arrays.asList(contents));
            e.getDrops().addAll(Arrays.asList(armorContents));

            if (Scenario.getScenarioByTitle("TimeBomb").isEnabled()) {
                new TimeBombUtil(playerName, e.getDrops(), e.getEntity().getLocation());
                e.getDrops().clear();
            } else e.getDrops().add(new ItemCreator(Material.SKULL_ITEM, 1).setOwner(playerName).getItem());


            if (e.getEntity().getKiller() != null) {
                UHCPlayer.getUhcPlayers().get(e.getEntity().getKiller().getUniqueId()).setCurrentKills(UHCPlayer.getUhcPlayers().get(e.getEntity().getKiller().getUniqueId()).getCurrentKills() + 1);

                boolean found = false;
                for (KillTopPlayer p : KillTopPlayer.getKillers()) {
                    if (Objects.equals(ChatColor.stripColor(p.getName()), ChatColor.stripColor(e.getEntity().getKiller().getDisplayName()))) {
                        found = true;
                        p.setKills(p.getKills() + 1);
                        break;
                    }
                }

                if (!found) new KillTopPlayer(e.getEntity().getKiller().getDisplayName(), 1);

                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c" + playerName + "&6(&fCombat Logger&6) &cwas slain by &a" + e.getEntity().getKiller().getDisplayName() + "&7[&f" + UHCPlayer.getUhcPlayers().get(e.getEntity().getKiller().getUniqueId()).getCurrentKills() + "&7]."));
                UHCPlayer player = UHCPlayer.getUhcPlayers().get(e.getEntity().getKiller().getUniqueId());
                if (player.getPlayerObject() == null) return;
                player.setCurrentKills(player.getCurrentKills() + 1);
                return;
            }

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&c" + e.getEntity().getCustomName() + "&6(&fCombat Logger&6) &chas expired. (10 Minutes)"));
            HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public void onEntityIgnite(EntityCombustEvent e) {
        if (!(e.getEntity() instanceof Villager)) return;
        if (e.getEntity().getUniqueId() == logger.getUniqueId()) e.setCancelled(true);
    }

}

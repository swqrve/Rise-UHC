package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.menu.board.*;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityMountEvent;

import java.util.ArrayList;
import java.util.List;

public class InteractListener implements Listener {

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        if (e.getItem().getType() == Material.GOLDEN_APPLE) {
            if (e.getItem().getDurability() == 1) {
                if (!(boolean) MatchAttribute.getAttributeFromName("Notch Apples").getCurrentSelection().getValue()) {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNotch apples are disabled!"));
                    return;
                }
            }

            if (e.getItem().isSimilar(UHCManager.getInstance().getGoldenHead())) e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1));

            if (!((boolean) MatchAttribute.getAttributeFromName("Absorption").getCurrentSelection().getValue())) Bukkit.getScheduler().runTaskLater(RiseUHC.getInstance(), () -> e.getPlayer().removePotionEffect(PotionEffectType.ABSORPTION), 1L);
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;

        if (UHCPlayer.getUhcPlayers().get(e.getWhoClicked().getUniqueId()).getCurrentState() != UHCPlayer.PlayerState.PLAYING) if (!UHCPlayer.getUhcPlayers().get(e.getWhoClicked().getUniqueId()).isInPractice()) e.setCancelled(true);

        if (e.getCurrentItem().getType().toString().contains("BARDING")) {
            if (!(boolean) MatchAttribute.getAttributeFromName("Horse Armor").getCurrentSelection().getValue()) {
                ((Player) e.getWhoClicked()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHorse armor is disabled."));
                e.setCancelled(true);
            }
        }

        if (e.getCurrentItem().getType() == Material.INK_SACK) {
            if (e.getInventory().getType() != InventoryType.ENCHANTING) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityMount(EntityMountEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getMount() instanceof Horse)) return;

        if (!(boolean) MatchAttribute.getAttributeFromName("Horses").getCurrentSelection().getValue()) {
            ((Player) e.getEntity()).sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHorses are disabled."));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent e) {
        if (UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).isInPractice()) return;
        if (UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentState() != UHCPlayer.PlayerState.PLAYING) e.setCancelled(true);
        if (e.getItem() == null) return;
        if (e.getAction() == Action.LEFT_CLICK_AIR  || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.PHYSICAL) return;

        if (UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentState() == UHCPlayer.PlayerState.LOBBY) {
            UHCPlayer player = UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId());

            if (e.getItem().getType() == Material.DIAMOND_SWORD) {
                if (player.isInPractice()) return;

                if (!UHCManager.getInstance().isPracticeEnabled()) {
                    player.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPractice is disabled."));
                    return;
                }

                player.sendToPractice();
                return;
            }

            if (e.getItem().getType() == Material.BOOK) {
                Bukkit.getPluginManager().registerEvents(new ConfigMenu(e.getPlayer()), RiseUHC.getInstance());
                return;
            }

            if (e.getItem().getType() == Material.SKULL_ITEM) {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cStats are disabled during the beta season."));
                return;
            }

            if (e.getItem().getType() == Material.NAME_TAG) {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLeaderboards are disabled during the beta season."));
                return;
            }

            return;
        }

        if (UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentState() == UHCPlayer.PlayerState.SPECTATING) {
            if (UHCManager.getInstance().getGame() == null) {
                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is no game running right now."));
                return;
            }

            if (e.getItem().getType() == Material.COMPASS) {
                if (e.getPlayer().hasPermission("uhc.spectate")) {
                    Bukkit.getPluginManager().registerEvents(new PlayerMenu(e.getPlayer(), true), RiseUHC.getInstance());
                    return;
                }

                Bukkit.getPluginManager().registerEvents(new PlayerMenu(e.getPlayer(), false), RiseUHC.getInstance());
                return;
            }

            if (e.getItem().getType() == Material.WATCH) {
                if (UHCManager.getInstance().getGame() == null || UHCManager.getInstance().getGame().getGamePlayers().size() < 1) {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCouldn't find a player."));
                    return;
                }

                if (e.getPlayer().hasPermission("uhc.spectate")) {
                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cTeleporting to a random player.."));
                    e.getPlayer().teleport(UHCManager.getInstance().getGame().getGamePlayers().get(Scenario.getRandom().nextInt(UHCManager.getInstance().getGame().getGamePlayers().size() - 1)).getPlayerObject().getLocation());
                    return;
                }

                List<UHCPlayer> playersWithinLimits = new ArrayList<>();
                UHCManager.getInstance().getGame().getGamePlayers().forEach(player -> {
                    if (player.getPlayerObject().getLocation().distance(new Location(Bukkit.getWorld("uhc_world"), 0, 60, 0)) < 100) playersWithinLimits.add(player);
                });

                if (playersWithinLimits.size() > 1) {
                    e.getPlayer().teleport(playersWithinLimits.get(Scenario.getRandom().nextInt(playersWithinLimits.size())).getPlayerObject().getLocation());
                    return;
                }

                e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere are no players within 100x100!"));
                return;
            }

            if (e.getItem().getType() == Material.TRIPWIRE_HOOK) Bukkit.getPluginManager().registerEvents(new StaffAlertMenu(e.getPlayer()), RiseUHC.getInstance());
            if (e.getItem().getType() == Material.GOLD_ORE) Bukkit.getPluginManager().registerEvents(new GoldMenu(e.getPlayer()), RiseUHC.getInstance());
            if (e.getItem().getType() == Material.DIAMOND_ORE) Bukkit.getPluginManager().registerEvents(new DiamondMenu(e.getPlayer()), RiseUHC.getInstance());
        }
    }


    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e) {
        if (UHCPlayer.getUhcPlayers().get(e.getPlayer().getUniqueId()).getCurrentState() != UHCPlayer.PlayerState.SPECTATING) return;
        if (!e.getPlayer().hasPermission("uhc.modspectate")) return;

        if (e.getRightClicked() instanceof Player) {
            Player spec = e.getPlayer();
            Player rightClicked = (Player) e.getRightClicked();

            Inventory inv = Bukkit.createInventory(null, 45, rightClicked.getDisplayName() + "'s Inventory");
            for (int b = 0; b < 36; b++) inv.setItem(b, rightClicked.getInventory().getItem(b));

            ItemStack health = new ItemStack(Material.REDSTONE_BLOCK);
            ItemMeta meta = health.getItemMeta();

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cHealth: " + (int) rightClicked.getHealth()));
            health.setItemMeta(meta);
            inv.setItem(36, health);

            if (rightClicked.getInventory().getHelmet()     != null) inv.setItem(38, rightClicked.getInventory().getHelmet());
            if (rightClicked.getInventory().getChestplate() != null) inv.setItem(39, rightClicked.getInventory().getChestplate());
            if (rightClicked.getInventory().getLeggings()   != null) inv.setItem(40, rightClicked.getInventory().getLeggings());
            if (rightClicked.getInventory().getBoots()      != null) inv.setItem(41, rightClicked.getInventory().getBoots());

            ItemStack potions = new ItemStack(Material.GLASS_BOTTLE);
            ItemMeta potionMeta = potions.getItemMeta();
            potionMeta.setDisplayName(ChatColor.YELLOW + "Active Potion Effects: ");

            ArrayList<String> list = new ArrayList<>();
            for (PotionEffect effect : rightClicked.getActivePotionEffects()) list.add(ChatColor.translateAlternateColorCodes('&', "&cPotion: " + effect.getType().getName().toLowerCase() + " Level: "  + effect.getAmplifier() + 1 + " Time Left: " + effect.getDuration()));

            potionMeta.setLore(list);
            potions.setItemMeta(potionMeta);
            inv.setItem(43, potions);

            spec.openInventory(inv);
        }
    }
}

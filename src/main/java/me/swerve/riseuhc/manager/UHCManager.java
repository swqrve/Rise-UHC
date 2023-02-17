package me.swerve.riseuhc.manager;

import lombok.Getter;
import lombok.Setter;
import me.swerve.riseuhc.game.UHCGame;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.util.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.SimpleDateFormat;
import java.util.*;

@Getter @Setter
public class UHCManager {
    public enum GameState { WAITING, STARTING, SCHEDULED, GENERATING, UNWHITELISTED, SCATTERING, PLAYING, ENDING }

    @Getter private static UHCManager instance;

    private boolean whitelisted;
    private GameState currentGameState;

    protected UHCGame game;

    private final Map<Integer, ItemStack> startingInventory = new HashMap<>();
    private final Map<Integer, ItemStack> spectatorInventory = new HashMap<>();
    private final Map<Integer, ItemStack> modSpectatorInventory = new HashMap<>();
    private final Map<Integer, ItemStack> hubInventory = new HashMap<>();
    private final Map<Integer, ItemStack> practiceInventory = new HashMap<>();


    private boolean pvpEnabled = false;

    private boolean borderShrinkResistance;

    private final ItemStack goldenHead = new ItemCreator(Material.GOLDEN_APPLE, 1).setName("&6Golden Head").getItem();

    private UHCPlayer host; // TODO: Set up for setting host, only thing it'll have is a prefix when they talk in chat

    private boolean chatEnabled = true;

    private int playersScattered = 0;
    private int initialScatterCount = 0;

    private int chunksLoaded = 0;

    private boolean practiceEnabled = true;

    private Date scheduleDate = null;

    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public UHCManager() {
        instance = this;

        currentGameState = GameState.WAITING;

        // Starter inventory
        startingInventory.put(0, new ItemStack(Material.COOKED_BEEF, 15));
        startingInventory.put(1, new ItemStack(Material.BOOK));

        // Hub inventory
        hubInventory.put(0, new ItemCreator(Material.DIAMOND_SWORD, 1).setName("&6Practice &7(Right Click)").addLore(Collections.singletonList("&7Right Click to go to Practice!")).getItem());
        hubInventory.put(1, new ItemCreator(Material.BOOK, 1).setName("&6Config &7(Right Click)").addLore(Collections.singletonList("&7Right Click to open the UHC Menu!")).getItem());

        hubInventory.put(8, new ItemCreator(Material.NAME_TAG, 1).setName("&6Leaderboards &7(Right Click)").addLore(Collections.singletonList("&7Right Click to view the Leaderboards!")).getItem());

        // Spectator Inventory (Non Staff)
        spectatorInventory.put(0, new ItemCreator(Material.COMPASS, 1).setName("&6Navigator &7(Right Click)").addLore(Collections.singletonList("&7Right Click to View all Players within 100x100")).getItem());
        spectatorInventory.put(1, new ItemCreator(Material.WATCH, 1).setName("&6Random Player &7(Right Click)").addLore(Collections.singletonList("&7Right Click to teleport to a random Player!")).getItem());

        // Spectator Inventory (Staff)
        modSpectatorInventory.put(0, new ItemCreator(Material.COMPASS, 1).setName("&6Navigator &7(Right Click)").addLore(Collections.singletonList("&7Right Click to View all Players")).getItem());
        modSpectatorInventory.put(1, new ItemCreator(Material.WATCH, 1).setName("&6Random Player &7(Right Click)").addLore(Collections.singletonList("&7Right Click to teleport to a random Player!")).getItem());

        modSpectatorInventory.put(4, new ItemCreator(Material.TRIPWIRE_HOOK, 1).setName("&6Toggle Alerts &7(Right Click)").addLore(Collections.singletonList("&7Right Click to toggle your alerts!")).getItem());

        modSpectatorInventory.put(7, new ItemCreator(Material.DIAMOND_ORE, 1).setName("&6Diamond Top &7(Right Click)").addLore(Collections.singletonList("&7Right Click to view Diamond Top!")).getItem());
        modSpectatorInventory.put(8, new ItemCreator(Material.GOLD_ORE, 1).setName("&6Gold Top &7(Right Click)").addLore(Collections.singletonList("&7Right Click to view Gold Top!")).getItem());

        // Practice Arena
        practiceInventory.put(0, new ItemCreator(Material.DIAMOND_SWORD, 1).setUnbreakable(true).addEnchant(Enchantment.DAMAGE_ALL, 2, true).getItem());
        practiceInventory.put(1, new ItemCreator(Material.FISHING_ROD, 1).setUnbreakable(true).getItem());

        practiceInventory.put(4, new ItemCreator(Material.GOLDEN_APPLE, 3).getItem());
        practiceInventory.put(5, goldenHead);

        practiceInventory.put(39, new ItemCreator(Material.DIAMOND_HELMET, 1).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).getItem());
        practiceInventory.put(38, new ItemCreator(Material.DIAMOND_CHESTPLATE, 1).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).getItem());
        practiceInventory.put(37, new ItemCreator(Material.DIAMOND_LEGGINGS, 1).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).getItem());
        practiceInventory.put(36, new ItemCreator(Material.DIAMOND_BOOTS, 1).setUnbreakable(true).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true).getItem());

        game = null;
    }

    public void startUHC() {
        setPracticeEnabled(false);
        clearPractice();

        List<UHCPlayer> gamePlayers = new ArrayList<>();
        UHCPlayer.getUhcPlayers().values().forEach(player -> { if (player.getCurrentState() == UHCPlayer.PlayerState.LOBBY) gamePlayers.add(player); });
        game = new UHCGame(gamePlayers);
    }

    // TODO: Make it so you can schedule games to start scatter
    public void scheduleGameScatterTime() {

    }

    public void clearPractice() {
        UHCPlayer.getUhcPlayers().values().forEach(player -> {
            if (player.isInPractice()) player.reset();
        });
    }
}

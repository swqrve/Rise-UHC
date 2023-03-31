package me.swerve.riseuhc.player;

import com.lunarclient.bukkitapi.LunarClientAPI;
import lombok.Getter;
import lombok.Setter;
import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.manager.TeamManager;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.logger.CombatLogger;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.team.Team;
import me.swerve.riseuhc.util.BorderUtil;
import me.swerve.riseuhc.util.ItemCreator;
import me.swerve.riseuhc.util.ScatterLocation;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter @Setter
public class UHCPlayer {
    public enum PlayerState { LOBBY, PLAYING, SPECTATING }

    @Getter private static final Map<UUID, UHCPlayer> uhcPlayers = new HashMap<>();

    private final UUID uuid;
    private PlayerState currentState;

    private boolean inPractice;
    private ScatterLocation loc;

    // Current rise Stats
    private int currentKills;

    private int currentDiamondsMined = 0;
    private int currentGoldMined = 0;

    private boolean diamondStaffAlert = false;
    private boolean goldStaffAlert = false;
    private boolean fightStaffAlert = false;
    private boolean netherEnterStaffAlert = false;

    private final List<Block> countedBlocks = new ArrayList<>();

    private Location respawnLocation;

    private ItemStack[] respawnItems;
    private ItemStack[] respawnArmor;
    private boolean invulnerable;

    private UUID lastSpokenUser;
    private String displayName;

    private UHCPlayer assignedPlayer = null;
    private Date cooldown = null;

    public UHCPlayer(Player p) {
        this.currentState = PlayerState.LOBBY;
        this.uuid = p.getUniqueId();
        this.displayName = p.getDisplayName();

        uhcPlayers.put(uuid, this);

        reset();
    }

    public void reset() {
        currentState = PlayerState.LOBBY;

        if (inPractice) {
            getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou've left practice."));
            inPractice = false;
        }

        resetPlayer();

        getPlayerObject().teleport(new Location(Bukkit.getWorld("Lobby"), -69.5, 114, -5.5, 0, -1.3f));
        getPlayerObject().playSound(getPlayerObject().getLocation(), Sound.valueOf("LEVEL_UP"), 1f, 1);

        UHCManager.getInstance().getHubInventory().keySet().forEach(keyInt -> {
            getPlayerObject().getInventory().setItem(keyInt, UHCManager.getInstance().getHubInventory().get(keyInt));
            getPlayerObject().getInventory().setItem(7, new ItemCreator(Material.SKULL_ITEM, 1).setOwner(getPlayerObject().getName()).setName("&6Stats &7(Right Click)").addLore(Collections.singletonList("&7Right Click to view your Stats!")).getItem());
        });

        for (Player player : Bukkit.getOnlinePlayers()) player.showPlayer(getPlayerObject());
    }

    public void startUHC() {
        currentState = PlayerState.PLAYING;

        resetPlayer();

        UHCManager.getInstance().getStartingInventory().keySet().forEach(keyInt -> getPlayerObject().getInventory().setItem(keyInt, UHCManager.getInstance().getStartingInventory().get(keyInt)));
        getPlayerObject().updateInventory();

        hideSpectators();

        getPlayerObject().teleport(new Location(Bukkit.getWorld("uhc_world"), loc.getX(), loc.getY(), loc.getZ()));
    }

    public void hideSpectators() {
        uhcPlayers.values().forEach(p -> {
            if (p.getCurrentState() == PlayerState.SPECTATING) hideSpectator(p.getPlayerObject());
        });
    }

    public void hideSpectator(Player player) {
        if (getPlayerObject() == null) return;
        if (player == null || !player.isOnline()) return;

        if (player.getDisplayName().equalsIgnoreCase(getPlayerObject().getDisplayName())) return;
        getPlayerObject().hidePlayer(player);
    }

    private void resetPlayer() {
        if (getPlayerObject() == null) return;

        LunarClientAPI.getInstance().disableAllStaffModules(getPlayerObject());

        getPlayerObject().setGameMode(GameMode.SURVIVAL);
        getPlayerObject().getInventory().clear();
        getPlayerObject().getInventory().setArmorContents(null);

        getPlayerObject().setHealth(20D);
        getPlayerObject().setFoodLevel(20);
        getPlayerObject().setExp(0);
        getPlayerObject().setLevel(0);
        getPlayerObject().setFireTicks(0);

        EntityPlayer player = ((CraftPlayer) getPlayerObject()).getHandle();
        player.setAbsorptionHearts(0);
        getPlayerObject().getActivePotionEffects().clear();
        getPlayerObject().updateInventory();

        for (Player p : Bukkit.getOnlinePlayers()) p.showPlayer(getPlayerObject());
    }

    public Player getPlayerObject() {
        if (Bukkit.getPlayer(uuid) == null) return null;
        return Bukkit.getPlayer(uuid);
    }

    public void setSpectator() {
        resetPlayer();

        uhcPlayers.values().forEach(uhcPlayer -> uhcPlayer.hideSpectator(getPlayerObject()));

        if (getPlayerObject().hasPermission("uhc.modspectate")) {
            UHCManager.getInstance().getModSpectatorInventory().keySet().forEach(keyInt -> getPlayerObject().getInventory().setItem(keyInt, UHCManager.getInstance().getModSpectatorInventory().get(keyInt)));
            LunarClientAPI.getInstance().giveAllStaffModules(getPlayerObject());
        } else UHCManager.getInstance().getSpectatorInventory().keySet().forEach(keyInt -> getPlayerObject().getInventory().setItem(keyInt, UHCManager.getInstance().getSpectatorInventory().get(keyInt)));

        getPlayerObject().setGameMode(GameMode.CREATIVE);
        getPlayerObject().teleport(new Location(Bukkit.getWorld("uhc_world"), 0, 100, 0));

        currentState = PlayerState.SPECTATING;
    }

    public void sendToPractice() {
        resetPlayer();

        if (!inPractice)  {
            getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSending you to practice.."));
            inPractice = true;
        }

        UHCManager.getInstance().getPracticeInventory().keySet().forEach(keyInt -> getPlayerObject().getInventory().setItem(keyInt, UHCManager.getInstance().getPracticeInventory().get(keyInt)));
        getPlayerObject().updateInventory();
        practiceScatter();

        currentState = PlayerState.LOBBY;
    }


    public void respawn() {
        if (getPlayerObject() == null) return;
        currentState = PlayerState.PLAYING;

        resetPlayer();

        getPlayerObject().getInventory().setContents(respawnItems);
        getPlayerObject().getInventory().setArmorContents(respawnArmor);

        getPlayerObject().updateInventory();

        UHCManager.getInstance().getGame().getGamePlayers().add(this);

        hideSpectators();

        getPlayerObject().teleport(respawnLocation);
        BorderUtil.updatePlayer(getPlayerObject());
    }

    private void practiceScatter() {
        int x = Scenario.getRandom().nextInt(40);
        int z = Scenario.getRandom().nextInt(95) + 10;

        getPlayerObject().teleport(new Location(Bukkit.getWorld("uhc_practice"), x, 105, z));
    }

    public void useCombatLogger() {
        CombatLogger logger = CombatLogger.getLoggers().get(getPlayerObject().getUniqueId());

        if (logger.isDead()) {
            setSpectator();
            HandlerList.unregisterAll(logger);
            CombatLogger.getLoggers().remove(getPlayerObject().getUniqueId());
            return;
        }

        currentState = PlayerState.PLAYING;
        resetPlayer();

        getPlayerObject().setHealth(logger.getLogger().getHealth());

        getPlayerObject().getInventory().setContents(logger.getContents());
        getPlayerObject().getInventory().setArmorContents(logger.getLogger().getEquipment().getArmorContents());

        getPlayerObject().teleport(logger.getLogger().getLocation());

        getPlayerObject().setExp(logger.getXpTilNextLevel());
        getPlayerObject().setLevel(logger.getXpLevels());

        logger.getLogger().remove();
        HandlerList.unregisterAll(logger);

        CombatLogger.getLoggers().remove(getPlayerObject().getUniqueId());
    }

    public void lateScatter() {
        loc = UHCManager.getInstance().getGame().getSafeLocation(Bukkit.getWorld("uhc_world"), UHCManager.getInstance().getGame().getCurrentBorder());
        getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You've been late scattered!"));

        if ((int) MatchAttribute.getAttributeFromName("Team Size").getCurrentSelection().getValue() != 1) {
            boolean hasTeam = false;
            for (Team team : TeamManager.getInstance().getTeamsList()) {
                if (team.isMember(getUuid())) {
                    hasTeam = true;
                    break;
                }
            }

            if (!hasTeam) {
                new Team(getUuid());
                getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cA team was created for you since you didn't have one!"));
            }
        }

        setCurrentState(PlayerState.PLAYING);
        UHCManager.getInstance().getGame().setInitialPlayerCount(UHCManager.getInstance().getGame().getInitialPlayerCount() + 1);

        invulnerable = true;
        Bukkit.getScheduler().scheduleSyncDelayedTask(RiseUHC.getInstance(), () -> invulnerable = false, 120);

        hideSpectators();
        startUHC();
    }

    public Team getCurrentTeam() {
        for (Team team : TeamManager.getInstance().getTeamsList()) {
            if (team.isMember(getPlayerObject().getUniqueId())) return team;
        }

        return null;
    }
}

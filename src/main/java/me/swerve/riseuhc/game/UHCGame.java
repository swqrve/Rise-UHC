package me.swerve.riseuhc.game;

import lombok.Getter;
import lombok.Setter;
import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.manager.TeamManager;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.runnable.GameRunnable;
import me.swerve.riseuhc.runnable.GameStartRunnable;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.team.Team;
import me.swerve.riseuhc.util.ScatterLocation;
import me.swerve.riseuhc.util.SitUtil;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UHCGame {
    @Getter @Setter private int gameTime;
    @Getter @Setter private GameRunnable runnable;

    @Getter @Setter private int initialPlayerCount;

    @Getter @Setter private int currentBorder;

    @Getter private UHCPlayer winner;
    @Getter private int teamKills;

    @Getter private int secondsTillRestart = 30;

    public UHCGame(List<UHCPlayer> players) {
        players.forEach(player -> { if (player.getCurrentState() != UHCPlayer.PlayerState.SPECTATING) player.setCurrentState(UHCPlayer.PlayerState.PLAYING); });
        gameTime = 0;

        initialPlayerCount = getGamePlayers().size();
        currentBorder = (int) MatchAttribute.getAttributeFromName("Border Size").getCurrentSelection().getValue();

        if ((int) MatchAttribute.getAttributeFromName("Team Size").getCurrentSelection().getValue() != 1) {
            for (UHCPlayer p : getGamePlayers()) {
                boolean hasTeam = false;
                for (Team team : TeamManager.getInstance().getTeamsList()) {
                    if (team.isMember(p.getUuid())) {
                        hasTeam = true;
                        break;
                    }
                }

                if (!hasTeam) {
                    new Team(p.getUuid());
                    p.getPlayerObject().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cA team was created for you since you didn't have one!"));
                }
            }
        }

        scatterPlayers();
    }

    public void scatterPlayers() {
        UHCManager.getInstance().setCurrentGameState(UHCManager.GameState.SCATTERING);
        UHCManager.getInstance().setInitialScatterCount(getGamePlayers().size());

        int x = 0;
        int z = 0;
        int borderSize = (int) MatchAttribute.getAttributeFromName("Border").getCurrentSelection().getValue() - 1;
        final World world = Bukkit.getWorld("uhc_world");

        List<Chunk> chunksToLoad = new ArrayList<>();
        if ((int) MatchAttribute.getAttributeFromName("Team Size").getCurrentSelection().getValue() != 1) {
            for (UHCPlayer player : getGamePlayers()) {
                Team currentTeam = null;
                for (Team team : TeamManager.getInstance().getTeamsList()) {

                    if (team.isMember(player.getUuid())) {
                        currentTeam = team;
                        break;
                    }
                }

                if (currentTeam == null) continue; // SHOULD NOT BE POSSIBLE

                ScatterLocation safeLoc;
                if (currentTeam.getTeamScatterLoc() != null) safeLoc = currentTeam.getTeamScatterLoc();
                else safeLoc = getSafeLocation(world, borderSize);

                if (currentTeam.getTeamScatterLoc() == null) {
                    chunksToLoad.add(world.getChunkAt(x, z));
                    UHCManager.getInstance().setChunksLoaded(UHCManager.getInstance().getChunksLoaded() + 1);

                    currentTeam.setTeamScatterLoc(safeLoc);
                }

                player.setLoc(safeLoc);
            }
        } else {
            for (UHCPlayer player : getGamePlayers()) {
                player.setLoc(getSafeLocation(world, borderSize));

                chunksToLoad.add(world.getChunkAt(x, z));
                UHCManager.getInstance().setChunksLoaded(UHCManager.getInstance().getChunksLoaded() + 1);
            }
        }

        chunksToLoad.forEach(chunk -> chunk.load(true));

        startTeleporting();
    }

    private void startTeleporting() {
        List<UHCPlayer> toScatter = getGamePlayers();

        new BukkitRunnable() {
            public void run() {
                if (toScatter.size() < 1) {
                    cancel();
                    createGame();
                    return;
                }

                if (toScatter.get(0).getPlayerObject() == null) {
                    toScatter.remove(0);
                    return;
                }

                toScatter.get(0).startUHC();
                UHCManager.getInstance().setPlayersScattered(UHCManager.getInstance().getPlayersScattered() + 1);
                SitUtil.sitPlayer(toScatter.get(0).getPlayerObject());
                toScatter.remove(0);
            }
        }.runTaskTimer(RiseUHC.getInstance(), 0, 1);
    }

    private void createGame() {
        new GameStartRunnable().runTaskTimer(RiseUHC.getInstance(), 0, 20);

        runnable = new GameRunnable(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(RiseUHC.getInstance(), runnable, 0, 20);
    }

    public void endGame(List<UHCPlayer> winners) {
        UHCManager.getInstance().setCurrentGameState(UHCManager.GameState.ENDING);

        if (winners.size() == 1) {
            UHCPlayer player = winners.get(0);
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fCongratulations to " + player.getPlayerObject().getDisplayName() + " &ffor winning the UHC with " + player.getCurrentKills() + " kills!"));
            winner = player;
        }

        if (winners.size() > 1) {
            StringBuilder playerWinBuilder = new StringBuilder();
            int totalKills = 0;
            for (int i = 0; i < winners.size(); i++) {
                if (i == winners.size() - 1) playerWinBuilder.append(", and ");
                else if (i != 0) playerWinBuilder.append(", ");

                playerWinBuilder.append(winners.get(i).getPlayerObject().getDisplayName());
                totalKills += winners.get(i).getCurrentKills();
            }

            teamKills = totalKills;
            winner = winners.get(0);

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &fCongratulations to " + playerWinBuilder + " &ffor winning the UHC with a total of " + totalKills + " kills!"));
        }

        for (UHCPlayer player : winners) scheduleFireWorks(player.getPlayerObject());

        new BukkitRunnable() {
            public void run() {
                secondsTillRestart--;

                if (secondsTillRestart <= 0) {
                    cancel();

                    UHCPlayer.getUhcPlayers().values().forEach(UHCPlayer::reset);

                    List<File> filesToDelete = new ArrayList<>();
                    filesToDelete.add(Bukkit.getWorld("uhc_world").getWorldFolder());
                    Bukkit.unloadWorld("uhc_world", false);

                    filesToDelete.add(Bukkit.getWorld("uhc_world_nether").getWorldFolder());
                    Bukkit.unloadWorld("uhc_world_nether", false);

                    filesToDelete.forEach(file -> {
                        try { FileUtils.deleteDirectory(file); }
                        catch(IOException ignored) {}
                    });

                    Bukkit.shutdown();
                }
            }
        }.runTaskTimer(RiseUHC.getInstance(), 0, 20);
    }

    private void scheduleFireWorks(Player p) {
        new BukkitRunnable() { public void run() { spawnFireWorks(p.getLocation().add(0, 1, 0)); }
        }.runTaskTimer(RiseUHC.getInstance(), 0, 40);
    }

    private void spawnFireWorks(Location loc) {
        Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fireWorkMeta = firework.getFireworkMeta();

        fireWorkMeta.setPower(2);
        fireWorkMeta.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

        firework.setFireworkMeta(fireWorkMeta);

        new BukkitRunnable() { public void run() { firework.detonate(); }
        }.runTaskLater(RiseUHC.getInstance(), 15);
    }

    public ScatterLocation getSafeLocation(World world, int borderSize) {
        int x = Scenario.getRandom().nextInt(borderSize * 2) - borderSize;
        int z = Scenario.getRandom().nextInt(borderSize * 2) - borderSize;

        for (int i = 5; i > 0; i--) if (world.getBlockAt(x, world.getHighestBlockYAt(x, z) - i, z).getType() == Material.LAVA || world.getBlockAt(x, world.getHighestBlockYAt(x, z) - i, z).getType() == Material.LAVA) return getSafeLocation(world, borderSize);

        return new ScatterLocation(x, world.getHighestBlockYAt(x, z) + 4, z);
    }

    public List<UHCPlayer> getGamePlayers() {
        List<UHCPlayer> uhcPlayers = new ArrayList<>();
        Bukkit.getOnlinePlayers().forEach(player -> {
            UHCPlayer p = UHCPlayer.getUhcPlayers().get(player.getUniqueId());
            if (p.getCurrentState() == UHCPlayer.PlayerState.PLAYING) uhcPlayers.add(p);
        });

        return uhcPlayers;
    }
}

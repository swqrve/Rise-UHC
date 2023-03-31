package me.swerve.riseuhc.scoreboard;

import assemble.AssembleAdapter;
import lombok.Getter;
import lombok.Setter;
import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.UHCPlayer;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.TimeUtil;
import me.swerve.riseuhc.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScoreBoardManager implements AssembleAdapter {
    @Getter @Setter private static int gameCountDown = 25;
    private final DecimalFormat df = new DecimalFormat("0.00");

    public String getTitle(Player player) { return ChatColor.translateAlternateColorCodes('&', "&6&lRise &7⎟ &fBeta Season");
    }

    public List<String> getLines(Player player) {
        List<String> lines = new ArrayList<>();
        lines.add("&7&m-------------------------------");
        if (UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.SCHEDULED) {
            int differenceInSeconds  = TimeUtil.differenceInSeconds(UHCManager.getInstance().getScheduleDate(), new Date());

            String result = String.format("%02d:%02d", (differenceInSeconds / 60) % 60, differenceInSeconds % 60);
            if (differenceInSeconds / 3600 != 0) result = String.format("%02d:%02d:%02d", differenceInSeconds / 3600, (differenceInSeconds / 60) % 60, differenceInSeconds % 60);


            lines.add("&6Opening In:");
            lines.add("&f" + result);
            lines.add("");
            lines.add("&7riseuhc.club");
        }


        if (UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.WAITING) {
            if (UHCManager.getInstance().isWhitelisted()) {
                lines.add("&6Online:");
                lines.add("&f" + Bukkit.getOnlinePlayers().size());
                lines.add("");
                lines.add("&6State:");
                lines.add("&fWhitelisted");
                lines.add("");
                lines.add("&7riseuhc.club");
            } else {
                lines.add("&6Online: &f" + Bukkit.getOnlinePlayers().size());
                lines.add("&6Type: &f" + MatchAttribute.getAttributeFromName("Team Size").getCurrentSelection().getName());
                lines.add("");
                lines.add("&6Border: &7[&f" + MatchAttribute.getAttributeFromName("Border").getCurrentSelection().getValue() + "x" + MatchAttribute.getAttributeFromName("Border").getCurrentSelection().getValue() + "&7]");
                lines.add("");
                lines.add("&6Scenarios:");
                int i = 0;
                for (Scenario scen : Scenario.getEnabledScenarios()) {
                    if (i >= 4) {
                        lines.add("&f....");
                        break;
                    }
                    lines.add(" &f- " + ChatColor.stripColor(scen.getTitle()));
                    i++;
                }
                lines.add(" ");
                lines.add("&7riseuhc.club");
            }
        }

        if (UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.GENERATING) {
            double percentage = 0.00;
            if (WorldManager.getInstance().getFillTask() != null) percentage = WorldManager.getInstance().getFillTask().getPercentageCompleted();

            lines.add("&6Currently Generating:");
            lines.add("&f" + WorldManager.getInstance().getCurrentlyGeneratingWorldType().toString().toLowerCase());
            lines.add("");
            lines.add("&6Percentage:");
            lines.add("&f" + df.format(percentage) + "%");
            lines.add("");
            lines.add("&7riseuhc.club");
        }

        if (UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.SCATTERING) {
            lines.add("&6Chunks: &7[&f" + UHCManager.getInstance().getChunksLoaded() + "/" + UHCManager.getInstance().getInitialScatterCount() + "&7]");
            lines.add("");
            lines.add("&6Scattering: &7[&f" + UHCManager.getInstance().getPlayersScattered() + "/" + UHCManager.getInstance().getInitialScatterCount() + "&7]");
            lines.add("");
            lines.add("&7riseuhc.club");
        }

        if (UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.STARTING) {
            lines.add("&6Starting In:");
            lines.add("&f" + gameCountDown);
            lines.add("");
            lines.add("&7riseuhc.club");
        }

        if (UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.PLAYING) {
            String result = String.format("%02d:%02d", (UHCManager.getInstance().getGame().getGameTime() / 60) % 60, UHCManager.getInstance().getGame().getGameTime() % 60);
            if (UHCManager.getInstance().getGame().getGameTime() / 3600 != 0) result = String.format("%02d:%02d:%02d", UHCManager.getInstance().getGame().getGameTime() / 3600, (UHCManager.getInstance().getGame().getGameTime() / 60) % 60, UHCManager.getInstance().getGame().getGameTime() % 60);
            lines.add("&6Duration: &f" + result);
            lines.add("&6Players: &7[&f" + UHCManager.getInstance().getGame().getGamePlayers().size() + "/" + UHCManager.getInstance().getGame().getInitialPlayerCount() + "&7]");
            lines.add("&6Kills: &f" + UHCPlayer.getUhcPlayers().get(player.getUniqueId()).getCurrentKills());
            if (UHCPlayer.getUhcPlayers().get(player.getUniqueId()).getCooldown() != null) lines.add("&6DND: &f" + TimeUtil.differenceInSeconds(UHCPlayer.getUhcPlayers().get(player.getUniqueId()).getCooldown(), new Date()) + "s");
            lines.add("&6Border: &7[&f" + UHCManager.getInstance().getGame().getCurrentBorder() + "x" + UHCManager.getInstance().getGame().getCurrentBorder() + "&7]");
            lines.add("");
            lines.add("&7riseuhc.club");
        }

        if (UHCManager.getInstance().getCurrentGameState() == UHCManager.GameState.ENDING) {
            if ((int) MatchAttribute.getAttributeFromName("Team Size").getCurrentSelection().getValue() == 1) {
                lines.add("&6Winner:");
                lines.add("&f" + UHCManager.getInstance().getGame().getWinner().getDisplayName());
                lines.add("");
                lines.add("&6Kills:");
                lines.add("&f" + UHCManager.getInstance().getGame().getWinner().getCurrentKills());
            } else {
                lines.add("&6Winners:");
                lines.add("&f Team #" + UHCManager.getInstance().getGame().getWinner().getCurrentTeam().getTeamNumber());
                lines.add("");
                lines.add("&6Team Kills:");
                lines.add("&f" + UHCManager.getInstance().getGame().getTeamKills());
            }

            lines.add("");
            lines.add("&6Closing in:");
            lines.add("&f" + UHCManager.getInstance().getGame().getSecondsTillRestart());
            lines.add("");
            lines.add("&7riseuhc.club");
        }

        lines.add("&7&m-------------------------------");

        List<String> toReturn = new ArrayList<>();
        lines.forEach(line -> toReturn.add(ChatColor.translateAlternateColorCodes('&', line)));
        return toReturn;
    }
}

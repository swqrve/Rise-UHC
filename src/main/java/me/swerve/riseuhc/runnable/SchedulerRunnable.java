package me.swerve.riseuhc.runnable;

import me.swerve.riseuhc.bot.DiscordBot;
import me.swerve.riseuhc.bot.TwitterBot;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import twitter4j.TwitterException;

import java.util.Date;

public class SchedulerRunnable extends BukkitRunnable {

    private boolean announced;

    public SchedulerRunnable() {
        UHCManager.getInstance().setCurrentGameState(UHCManager.GameState.SCHEDULED);
        UHCManager.getInstance().setWhitelisted(true);
        String time = UHCManager.getInstance().getFormatter().format(UHCManager.getInstance().getScheduleDate());
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4The game is now scheduled for: " + time.substring(0, time.length() - 3)));
    }

    @Override
    public void run() {
        if (UHCManager.getInstance().getCurrentGameState() != UHCManager.GameState.SCHEDULED) {
            cancel();
            return;
        }

        if (!announced && TimeUtil.differenceInMinutes(UHCManager.getInstance().getScheduleDate(), new Date()) + 1 <= 15) {
            announced = true;

            StringBuilder scenariosAsString = new StringBuilder();

            for (Scenario scenario : Scenario.getEnabledScenarios()) scenariosAsString.append(scenario.getTitle()).append(",");
            String scens = scenariosAsString.toString();

            if (scens.length() > 1) scens = scens.substring(0, scens.length() - 1);
            else scens = "None";

            String time = UHCManager.getInstance().getFormatter().format(UHCManager.getInstance().getScheduleDate());
            time = time.split(" ")[1].substring(0, time.split(" ")[1].length() - 3);

            if (DiscordBot.getInstance() != null) {
                DiscordBot.getInstance().announceGame(scens, time);
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cAnnounced in Discord"));
            }

            if (TwitterBot.getInstance() != null) {
                try { TwitterBot.getInstance().announceGame(scens, time);
                } catch (TwitterException ignored) {  }

                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cAnnounced on Twitter"));
            }
        }

        int differenceInSeconds = TimeUtil.differenceInSeconds(UHCManager.getInstance().getScheduleDate(), new Date());
        if (differenceInSeconds <= 0) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&4The game is now open!"));
            UHCManager.getInstance().setCurrentGameState(UHCManager.GameState.WAITING);
            UHCManager.getInstance().setScheduleDate(null);
            UHCManager.getInstance().setWhitelisted(false);
            cancel();
        }
    }
}

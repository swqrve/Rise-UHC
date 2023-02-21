package me.swerve.riseuhc.runnable.action;

import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.player.logger.CombatLogger;
import me.swerve.riseuhc.scenario.Scenario;
import me.swerve.riseuhc.util.BorderUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Action {
    final String message;
    final TimedAction.ActionType actionType;

    public Action(String message, TimedAction.ActionType actionType) {
        this.message = message;
        this.actionType = actionType;
    }

    public void executeAction() {
        Bukkit.broadcastMessage(message);
        if (actionType == null) return;
        switch (actionType) {
            case ENABLE_PVP:
                UHCManager.getInstance().setPvpEnabled(true);
                break;
            case FINAL_HEAL:
                for (Player player : Bukkit.getOnlinePlayers()) player.setHealth(20);
                break;
            case SHRINK_BORDER:
                if (UHCManager.getInstance().getGame().getCurrentBorder() == BorderUtil.nextBorder(UHCManager.getInstance().getGame().getCurrentBorder())) return;

                int newBorderSize = BorderUtil.nextBorder(UHCManager.getInstance().getGame().getCurrentBorder());
                BorderUtil.createBedrockWall(newBorderSize);
                UHCManager.getInstance().getGame().setCurrentBorder(newBorderSize);

                if (newBorderSize <= 100) if (!Scenario.getScenarioByTitle("DoNotDisturb").isEnabled()) Scenario.getScenarioByTitle("DoNotDisturb").setState(true);

                if ((boolean) MatchAttribute.getAttributeFromName("Border Resistance").getCurrentSelection().getValue()) {
                    UHCManager.getInstance().setBorderShrinkResistance(true);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(RiseUHC.getInstance(), () -> { UHCManager.getInstance().setBorderShrinkResistance(false); }, 60);
                }

                for (Player player : Bukkit.getOnlinePlayers()) BorderUtil.updatePlayer(player);
                for (CombatLogger logger : CombatLogger.getLoggers().values()) BorderUtil.updatePlayer(logger.getLogger(), true);

                int borderTime = UHCManager.getInstance().getGame().getGameTime() + (5 * 60);
                TimedAction borderShrink = new TimedAction(UHCManager.getInstance().getGame().getRunnable()).addAction(borderTime - 5 * 60, "&f[&6RiseUHC&f] &f5 Minutes until Border Shrinks to &7[&f" + BorderUtil.nextBorder(UHCManager.getInstance().getGame().getCurrentBorder()) + "x" + BorderUtil.nextBorder(UHCManager.getInstance().getGame().getCurrentBorder()) + "&7]")
                        .addAction(borderTime - 3 * 60, "&f[&6RiseUHC&f] &f3 Minutes until Border Shrinks to &7[&f" + BorderUtil.nextBorder(UHCManager.getInstance().getGame().getCurrentBorder()) + "x" + BorderUtil.nextBorder(UHCManager.getInstance().getGame().getCurrentBorder()) + "&7]")
                        .addAction(borderTime - 60,"&f[&6RiseUHC&f] &f1 Minute until Border Shrinks to &7[&f" + BorderUtil.nextBorder(UHCManager.getInstance().getGame().getCurrentBorder()) + "x" + BorderUtil.nextBorder(UHCManager.getInstance().getGame().getCurrentBorder()) + "&7]")
                        .addAction(borderTime, "&f[&6RiseUHC&f] Border has shrunk to &7[&f" + BorderUtil.nextBorder(UHCManager.getInstance().getGame().getCurrentBorder()) + "x" + BorderUtil.nextBorder(UHCManager.getInstance().getGame().getCurrentBorder()) + "&7]", TimedAction.ActionType.SHRINK_BORDER);

                for (int i = 10; i > 0; i--) {
                    if (i > 5 && i != 10) continue;

                    String withS = " Seconds";
                    if (i == 1) withS = " Second";

                    borderShrink.addAction(borderTime - i, "&f[&6RiseUHC&f] &f" + i + withS + " until Border Shrinks to &7[&f" + BorderUtil.nextBorder(UHCManager.getInstance().getGame().getCurrentBorder()) + "x" + BorderUtil.nextBorder(UHCManager.getInstance().getGame().getCurrentBorder()) + "&7]");
                }

                break;
        }
    }
}

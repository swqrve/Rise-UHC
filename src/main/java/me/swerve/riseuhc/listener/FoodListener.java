package me.swerve.riseuhc.listener;

import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.scenario.Scenario;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodListener implements Listener {

    @EventHandler
    public void onFoodLoseEvent(FoodLevelChangeEvent e) {
        if (UHCManager.getInstance().getCurrentGameState() != UHCManager.GameState.PLAYING) {
            e.setFoodLevel(20);
            return;
        }

        Player player = (Player) e.getEntity();
        int oldFoodLevel = player.getFoodLevel();
        int newFoodLevel = e.getFoodLevel();

        if (newFoodLevel < oldFoodLevel) if (Scenario.getRandom().nextInt(100) + 1 > 40) {
            e.setCancelled(true);
        }
    }
}

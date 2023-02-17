package me.swerve.riseuhc.runnable;

import lombok.Getter;
import lombok.Setter;
import me.swerve.riseuhc.attribute.MatchAttribute;
import me.swerve.riseuhc.game.UHCGame;
import me.swerve.riseuhc.manager.UHCManager;
import me.swerve.riseuhc.runnable.action.TimedAction;
import me.swerve.riseuhc.util.BorderUtil;
import me.swerve.riseuhc.util.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameRunnable extends BukkitRunnable {
    @Getter @Setter private int taskId;
    @Getter private final List<TimedAction> timedActions = new ArrayList<>();

    public GameRunnable(UHCGame game) {
        UHCManager.getInstance().setPvpEnabled(false);

        if ((boolean) MatchAttribute.getAttributeFromName("Combine Books").getCurrentSelection().getValue()) {
            ShapelessRecipe bookCraft = new ShapelessRecipe(new ItemStack(Material.BOOK));
            bookCraft.addIngredient(2, Material.ENCHANTED_BOOK);

            Bukkit.getServer().addRecipe(bookCraft);
        }

        if ((boolean) MatchAttribute.getAttributeFromName("Golden Heads").getCurrentSelection().getValue()) {
            ShapedRecipe goldenHead = new ShapedRecipe(UHCManager.getInstance().getGoldenHead());
            goldenHead.shape("!!!","!@!","!!!");
            goldenHead.setIngredient('!', Material.GOLD_INGOT);
            goldenHead.setIngredient('@', new ItemCreator(Material.SKULL_ITEM, 1).setData(3).getItem().getData());

            Bukkit.getServer().addRecipe(goldenHead);
        }

        if ((boolean) MatchAttribute.getAttributeFromName("String Craft").getCurrentSelection().getValue()) {
            ShapelessRecipe stringCraft = new ShapelessRecipe(new ItemStack(Material.STRING));
            stringCraft.addIngredient(4, Material.WOOL);

            Bukkit.getServer().addRecipe(stringCraft);
        }

        int finalTime = (int) MatchAttribute.getAttributeFromName("Final Heal Time").getCurrentSelection().getValue() * 60;
        TimedAction finalHeal = new TimedAction(this).addAction(finalTime - 5 * 60,"&f[&6RiseUHC&f] &f5 Minutes until Final Heal")
                .addAction(finalTime - 3 * 60,"&f[&6RiseUHC&f] &f3 Minutes until Final Heal")
                .addAction(finalTime - 60,"&f[&6RiseUHC&f] &f1 Minute until Final Heal")
                .addAction(finalTime, "&f[&6RiseUHC&f] Final heal has been given. Please don't ask again.", TimedAction.ActionType.FINAL_HEAL);

        int pvpTime = (int) MatchAttribute.getAttributeFromName("Pvp Time").getCurrentSelection().getValue() * 60;
        TimedAction pvpEnable = new TimedAction(this).addAction(pvpTime - 5 * 60, "&f[&6RiseUHC&f] &f5 Minutes until PvP enables.")
                .addAction(pvpTime - 3 * 60, "&f[&6RiseUHC&f] &f3 Minutes until PvP enables.")
                .addAction(pvpTime - 60,"&f[&6RiseUHC&f] &f1 Minute until PvP enables.")
                .addAction(pvpTime, "&f[&6RiseUHC&f] PvP has been Enabled.", TimedAction.ActionType.ENABLE_PVP);

        int borderTime = (int) MatchAttribute.getAttributeFromName("Border Time").getCurrentSelection().getValue() * 60;
        TimedAction borderShrink = new TimedAction(this).addAction(borderTime - 5 * 60, "&f[&6RiseUHC&f] &f5 Minutes until Border Shrinks to &7[&f" + BorderUtil.nextBorder(game.getCurrentBorder()) + "x" + BorderUtil.nextBorder(game.getCurrentBorder()) + "&7]")
                .addAction(borderTime - 3 * 60, "&f[&6RiseUHC&f] &f3 Minutes until Border Shrinks to &7[&f" + BorderUtil.nextBorder(game.getCurrentBorder()) + "x" + BorderUtil.nextBorder(game.getCurrentBorder()) + "&7]")
                .addAction(borderTime - 60,"&f[&6RiseUHC&f] &f1 Minute until Border Shrinks to &7[&f" + BorderUtil.nextBorder(game.getCurrentBorder()) + "x" + BorderUtil.nextBorder(game.getCurrentBorder()) + "&7]")
                .addAction(borderTime, "&f[&6RiseUHC&f] Border has shrunk to &7[&f" + BorderUtil.nextBorder(game.getCurrentBorder()) + "x" + BorderUtil.nextBorder(game.getCurrentBorder()) + "&7]", TimedAction.ActionType.SHRINK_BORDER);

        for (int i = 10; i > 0; i--) {
            String withS = " Seconds";
            if (i == 1) withS = " Second";

            finalHeal.addAction(finalTime - i,"&f[&6RiseUHC&f] &f" + i + withS + " until Final Heal.");
            pvpEnable.addAction(pvpTime - i, "&f[&6RiseUHC&f] &f" + i  + withS + " until PvP Time.");
            borderShrink.addAction(borderTime - i, "&f[&6RiseUHC&f] &f" + i + withS + " until Border Shrinks to &7[&f" + BorderUtil.nextBorder(game.getCurrentBorder()) + "x" + BorderUtil.nextBorder(game.getCurrentBorder()) + "&7]");
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world clear");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "wb uhc_world_nether clear");
    }

    public void run() {
        if (UHCManager.getInstance().getCurrentGameState() != UHCManager.GameState.PLAYING) return;

        setGameTime(getGameTime() + 1);

        for (TimedAction action : new ArrayList<>(timedActions)) action.update(getGameTime());
        if (UHCManager.getInstance().getCurrentGameState() != UHCManager.GameState.ENDING) if (UHCManager.getInstance().getGame().getGamePlayers().size() < 2) {
            UHCManager.getInstance().getGame().endGame(Collections.singletonList(UHCManager.getInstance().getGame().getGamePlayers().get(0)));
            cancel();
        }
    }

    private void setGameTime(int i) {
        UHCManager.getInstance().getGame().setGameTime(i);
    }

    private int getGameTime() {
        return UHCManager.getInstance().getGame().getGameTime();
    }
}

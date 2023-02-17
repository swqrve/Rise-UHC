package me.swerve.riseuhc.scenario;

import lombok.Getter;
import me.swerve.riseuhc.RiseUHC;
import me.swerve.riseuhc.scenario.scen.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Scenario implements Listener {
    @Getter private static final List<Scenario> scenarios = new ArrayList<>();
    @Getter private static final Random random = new Random();

    @Getter private final String title;
    @Getter private final Material symbol;
    @Getter private final List<String> description;

    @Getter private boolean enabled;

    public Scenario(String title, Material symbol, List<String> description) {
        this.title = title;
        this.symbol = symbol;

        List<String> coloredDescription = new ArrayList<>();
        description.forEach(desc -> coloredDescription.add(ChatColor.translateAlternateColorCodes('&', desc)));
        this.description = coloredDescription;

        this.enabled = false;

        scenarios.add(this);
    }

    public static List<Scenario> getEnabledScenarios() {
        List<Scenario> toReturn = new ArrayList<>();
        scenarios.forEach(scenario -> { if (scenario.isEnabled()) toReturn.add(scenario); });

        return toReturn;
    }

    public void setState(boolean state) {
        this.enabled = state;

        if (enabled) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &f" + title + " has been enabled."));
            Bukkit.getPluginManager().registerEvents(this, RiseUHC.getInstance());
        } else {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&f[&6RiseUHC&f] &f" + title + " has been disabled."));
            HandlerList.unregisterAll(this);
        }
    }

    public static Scenario getScenarioByTitle(String title) {
        for (Scenario scen : scenarios) if (ChatColor.stripColor(scen.getTitle()).equalsIgnoreCase(ChatColor.stripColor(title))) return scen;
        return null;
    }


    public static void instantiateScenarios() {
        new BedBomb();
        new BloodDiamonds();
        new BloodEnchants();
        new BloodGold();
        new Bowless();
        new CutClean();
        new EnchantedDeath();
        new Fireless();
        new Goldless();
        new HasteyBoys();
        new NoAnvils();
        new NoClean();
        new NoDiamondArmor();
        new NoFall();
        new RedArrow();
        new Timber();
        new TimeBomb();
        new DoNotDisturb();
    }
}

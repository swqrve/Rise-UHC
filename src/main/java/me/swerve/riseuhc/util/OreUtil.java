package me.swerve.riseuhc.util;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OreUtil {
    @Getter private static final List<OreUtil> miners = new ArrayList<>();

    @Getter @Setter private String name;
    @Getter @Setter private int minedDiamonds;
    @Getter @Setter private int minedGold;

    public OreUtil(String name, int minedDiamonds, int minedGold) {
        this.name = name;

        this.minedDiamonds = minedDiamonds;
        this.minedGold = minedGold;

        miners.add(this);
    }
}

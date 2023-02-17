package me.swerve.riseuhc.attribute.attribute.mining;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class RedstoneXP extends MatchAttribute {
    public RedstoneXP() {
        super(Section.MINING, "Redstone XP");

        getOptions().add(new AttributeValue("Normal", 1));
        getOptions().add(new AttributeValue("Half", .5));
        getOptions().add(new AttributeValue("Off", 0));

        setCurrentSelection(getOptions().get(0));
    }
}


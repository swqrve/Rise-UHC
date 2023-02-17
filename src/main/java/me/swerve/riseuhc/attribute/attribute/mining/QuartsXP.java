package me.swerve.riseuhc.attribute.attribute.mining;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class QuartsXP extends MatchAttribute {
    public QuartsXP() {
        super(Section.MINING, "Quartz XP");

        getOptions().add(new AttributeValue("Normal", 1));
        getOptions().add(new AttributeValue("Half", .5));
        getOptions().add(new AttributeValue("Off", 0));

        setCurrentSelection(getOptions().get(0));
    }
}


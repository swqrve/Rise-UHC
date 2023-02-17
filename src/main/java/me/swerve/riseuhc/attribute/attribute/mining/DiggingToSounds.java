package me.swerve.riseuhc.attribute.attribute.mining;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class DiggingToSounds extends MatchAttribute {
    public DiggingToSounds() {
        super(Section.MINING, "Digging to Sounds");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(0));
    }
}


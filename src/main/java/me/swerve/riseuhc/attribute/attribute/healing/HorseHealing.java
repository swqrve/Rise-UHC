package me.swerve.riseuhc.attribute.attribute.healing;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class HorseHealing extends MatchAttribute {
    public HorseHealing() {
        super(Section.HEALING, "Horse Healing");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(0));
    }
}

package me.swerve.riseuhc.attribute.attribute.misc;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class Shears extends MatchAttribute {
    public Shears() {
        super(Section.GENERAL, "Shears");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(0));
    }
}

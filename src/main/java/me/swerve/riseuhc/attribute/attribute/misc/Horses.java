package me.swerve.riseuhc.attribute.attribute.misc;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class Horses extends MatchAttribute{
    public Horses() {
        super(Section.GENERAL, "Horses");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(1));
    }
}

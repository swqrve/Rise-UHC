package me.swerve.riseuhc.attribute.attribute.misc;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class Camping extends MatchAttribute{
    public Camping() {
        super(Section.GENERAL, "Camping");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(1));
    }
}

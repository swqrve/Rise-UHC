package me.swerve.riseuhc.attribute.attribute.nether;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class Nether extends MatchAttribute {
    public Nether() {
        super(Section.NETHER, "Nether");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(1));
    }
}

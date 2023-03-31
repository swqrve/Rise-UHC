package me.swerve.riseuhc.attribute.attribute.nether;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class Speed extends MatchAttribute {
    public Speed() {
        super(Section.NETHER, "Speed");

        getOptions().add(new AttributeValue("Speed 2", 2));
        getOptions().add(new AttributeValue("Speed 1", 1));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(2));
    }
}

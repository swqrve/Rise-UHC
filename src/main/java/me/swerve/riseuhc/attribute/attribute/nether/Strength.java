package me.swerve.riseuhc.attribute.attribute.nether;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class Strength extends MatchAttribute {
    public Strength() {
        super(Section.NETHER, "Strength");

        getOptions().add(new AttributeValue("Strength 2", 2));
        getOptions().add(new AttributeValue("Strength 1", 1));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(0));
    }
}

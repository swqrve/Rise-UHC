package me.swerve.riseuhc.attribute.attribute.generator;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class DiamondRate extends MatchAttribute {
    public DiamondRate() {
        super(Section.GENERATION, "Diamond Rate");

        getOptions().add(new AttributeValue("6.0", 6));
        getOptions().add(new AttributeValue("5.5", 5));
        getOptions().add(new AttributeValue("5.0", 4));

        setCurrentSelection(getOptions().get(0));
    }
}

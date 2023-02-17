package me.swerve.riseuhc.attribute.attribute.mining;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class Rollercoasting extends MatchAttribute {
    public Rollercoasting() {
        super(Section.MINING, "Rollercoasting");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(0));
    }
}

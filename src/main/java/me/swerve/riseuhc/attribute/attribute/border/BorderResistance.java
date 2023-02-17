package me.swerve.riseuhc.attribute.attribute.border;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class BorderResistance extends MatchAttribute{
    public BorderResistance() {
        super(Section.BORDER, "Border Resistance");

        getOptions().add(new AttributeValue("On - 3 Seconds", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(0));
    }
}

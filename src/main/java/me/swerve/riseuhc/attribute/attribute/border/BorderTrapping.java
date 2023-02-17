package me.swerve.riseuhc.attribute.attribute.border;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class BorderTrapping extends MatchAttribute{
    public BorderTrapping() {
        super(Section.BORDER, "Border Trapping");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(0));
    }
}

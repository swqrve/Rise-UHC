package me.swerve.riseuhc.attribute.attribute.misc;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class CombineBooks extends MatchAttribute{
    public CombineBooks() {
        super(Section.GENERAL, "Combine Books");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(0));
    }
}

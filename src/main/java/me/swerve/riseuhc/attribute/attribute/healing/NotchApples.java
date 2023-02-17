package me.swerve.riseuhc.attribute.attribute.healing;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class NotchApples extends MatchAttribute {
    public NotchApples() {
        super(Section.HEALING, "Notch Apples");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(0));
    }
}

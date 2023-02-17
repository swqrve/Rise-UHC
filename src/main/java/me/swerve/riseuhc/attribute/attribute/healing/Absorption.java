package me.swerve.riseuhc.attribute.attribute.healing;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class Absorption extends MatchAttribute {
    public Absorption() {
        super(Section.HEALING, "Absorption");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(0));
    }
}

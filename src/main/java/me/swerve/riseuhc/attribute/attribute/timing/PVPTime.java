package me.swerve.riseuhc.attribute.attribute.timing;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class PVPTime extends MatchAttribute {
    public PVPTime() {
        super(Section.TIMING, "PVP Time");

        for (int i = 8; i > 2; i--) getOptions().add(new AttributeValue("" + 5 * i, 5 * i));
        getOptions().add(new AttributeValue("2", 2));

        setCurrentSelection(getOptions().get(3));
    }
}

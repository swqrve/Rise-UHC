package me.swerve.riseuhc.attribute.attribute.timing;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class BorderTime extends MatchAttribute {
    public BorderTime() {
        super(Section.TIMING, "Border Time");

        for (int i = 10; i > 4; i--) getOptions().add(new AttributeValue("" + 5 * i, 5 * i));
        getOptions().add(new AttributeValue("1", 1));

        setCurrentSelection(getOptions().get(3));
    }
}

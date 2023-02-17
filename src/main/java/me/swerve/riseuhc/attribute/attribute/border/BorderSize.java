package me.swerve.riseuhc.attribute.attribute.border;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class BorderSize extends MatchAttribute {
    public BorderSize() {
        super(Section.BORDER, "Border Size");

        for (int i = 6; i > 0; i--) getOptions().add(new AttributeValue("" + 500 * i, 500 * i));

        setCurrentSelection(getOptions().get(3));
    }
}

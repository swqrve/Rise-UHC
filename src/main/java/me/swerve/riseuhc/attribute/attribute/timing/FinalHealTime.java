package me.swerve.riseuhc.attribute.attribute.timing;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class FinalHealTime extends MatchAttribute {
    public FinalHealTime() {
        super(Section.TIMING, "Final Heal Time");

        for (int i = 6; i > 0; i--) getOptions().add(new AttributeValue("" + 5 * i, 5 * i));

        setCurrentSelection(getOptions().get(3));
    }
}

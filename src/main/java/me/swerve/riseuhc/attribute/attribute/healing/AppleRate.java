package me.swerve.riseuhc.attribute.attribute.healing;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class AppleRate extends MatchAttribute {
    public AppleRate() {
        super(Section.HEALING, "Apple Rate");

        for (int i = 4; i > 0; i--) getOptions().add(new AttributeValue(5 * i + "%", i * 3));

        setCurrentSelection(getOptions().get(2));
    }
}

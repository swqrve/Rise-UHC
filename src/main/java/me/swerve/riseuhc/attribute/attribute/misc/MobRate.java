package me.swerve.riseuhc.attribute.attribute.misc;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class MobRate extends MatchAttribute {
    public MobRate() {
        super(Section.GENERAL, "Mob Rate");

        getOptions().add(new AttributeValue("Normal", 1.0D));
        getOptions().add(new AttributeValue("Decreased", .5D));
        getOptions().add(new AttributeValue("Off", 0.0D));

        setCurrentSelection(getOptions().get(0));
    }
}

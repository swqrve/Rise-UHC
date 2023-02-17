package me.swerve.riseuhc.attribute.attribute.timing;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class MeetupTime extends MatchAttribute {
    public MeetupTime() {
        super(Section.TIMING, "Meetup Time");

        for (int i = 12; i > 6; i--) getOptions().add(new AttributeValue("" + 5 * i, 5 * i));

        setCurrentSelection(getOptions().get(3));
    }
}

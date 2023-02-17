package me.swerve.riseuhc.attribute.attribute.teams;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class CrossTeaming extends MatchAttribute{
    public CrossTeaming() {
        super(Section.TEAM_INFORMATION, "Cross Teaming");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(0));
    }
}

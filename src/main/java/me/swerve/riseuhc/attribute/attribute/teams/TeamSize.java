package me.swerve.riseuhc.attribute.attribute.teams;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class TeamSize extends MatchAttribute{
    public TeamSize() {
        super(Section.TEAM_INFORMATION, "Team Size");

        getOptions().add(new AttributeValue("FFA", 1));
        for (int i = 2; i < 10; i++) getOptions().add(new AttributeValue("To" +  i, i));

        setCurrentSelection(getOptions().get(0));
    }
}

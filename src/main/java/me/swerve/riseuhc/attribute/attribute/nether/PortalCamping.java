package me.swerve.riseuhc.attribute.attribute.nether;

import me.swerve.riseuhc.attribute.AttributeValue;
import me.swerve.riseuhc.attribute.MatchAttribute;

public class PortalCamping extends MatchAttribute {
    public PortalCamping() {
        super(Section.NETHER, "Portal Camping");

        getOptions().add(new AttributeValue("On", true));
        getOptions().add(new AttributeValue("Off", false));

        setCurrentSelection(getOptions().get(0));
    }
}

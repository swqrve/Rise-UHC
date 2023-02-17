package me.swerve.riseuhc.attribute;

import lombok.Getter;
import lombok.Setter;
import me.swerve.riseuhc.attribute.attribute.border.BorderResistance;
import me.swerve.riseuhc.attribute.attribute.border.BorderSize;
import me.swerve.riseuhc.attribute.attribute.border.BorderTrapping;
import me.swerve.riseuhc.attribute.attribute.generator.DiamondRate;
import me.swerve.riseuhc.attribute.attribute.generator.GoldRate;
import me.swerve.riseuhc.attribute.attribute.healing.*;
import me.swerve.riseuhc.attribute.attribute.mining.*;
import me.swerve.riseuhc.attribute.attribute.misc.*;
import me.swerve.riseuhc.attribute.attribute.nether.Nether;
import me.swerve.riseuhc.attribute.attribute.nether.PortalCamping;
import me.swerve.riseuhc.attribute.attribute.nether.Speed;
import me.swerve.riseuhc.attribute.attribute.nether.Strength;
import me.swerve.riseuhc.attribute.attribute.teams.CrossTeaming;
import me.swerve.riseuhc.attribute.attribute.timing.BorderTime;
import me.swerve.riseuhc.attribute.attribute.timing.FinalHealTime;
import me.swerve.riseuhc.attribute.attribute.timing.MeetupTime;
import me.swerve.riseuhc.attribute.attribute.timing.PVPTime;

import java.util.ArrayList;
import java.util.List;

public abstract class MatchAttribute {
    @Getter private static final List<MatchAttribute> attributes = new ArrayList<>();

    public enum Section {
        BORDER("Border"), TEAM_INFORMATION("Team Information"), HEALING("Healing"), MINING("Mining"), TIMING("Timing"), GENERATION("Generate Settings"), GENERAL("Misc"), NETHER("Nether");
        @Getter private final String pageIdentifier;
        Section(String pageIdentifier) {
            this.pageIdentifier = pageIdentifier;
        }
    };

    @Getter private final List<AttributeValue> options = new ArrayList<>();
    @Getter private final Section section;

    @Getter @Setter private AttributeValue currentSelection;
    @Getter private final String name;

    public MatchAttribute(Section matchSection, String name) {
        this.section = matchSection;
        this.name = name;

        attributes.add(this);
    }

    public static MatchAttribute getAttributeFromName(String string) {
        for (MatchAttribute attribute : attributes) if (attribute.getName().equalsIgnoreCase(string)) return attribute;
        return attributes.get(0);
    }

    public static void instantiateAttributes() {
        new BorderSize();
        new BorderTrapping();
        new Camping();
        new CrossTeaming();
        new DiggingToSounds();
        new HorseArmor();
        new Horses();
        new MobRate();
        new Nether();
        new Pokeholing();
        new PortalCamping();
        new Rollercoasting();
        new Shears();
        new Speed();
        new Strength();
        new BorderResistance();
        new CombineBooks();
        new StringCraft();
        new Absorption();
        new AppleRate();
        new DiamondRate();
        new GoldRate();
        new BorderTime();
        new MeetupTime();
        new PVPTime();
        new QuartsXP();
        new RedstoneXP();
        new HorseHealing();
        new NotchApples();
        new GoldenHeads();
        new FinalHealTime();
    }
}

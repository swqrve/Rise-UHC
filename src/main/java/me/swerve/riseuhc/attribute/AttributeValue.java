package me.swerve.riseuhc.attribute;

import lombok.Getter;

public class AttributeValue {
    @Getter private final String name;
    @Getter private final Object value;

    public AttributeValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}

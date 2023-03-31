package me.swerve.riseuhc.util;
public enum CrashType {
    EXPLOSION, POSITION;

    public static CrashType fromString(String s) {
        for (CrashType type : values()) if (type.name().toLowerCase().contains(s.toLowerCase())) return type;
        return null;
    }
}
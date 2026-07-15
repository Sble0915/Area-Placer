package com.sble.areaplacer.client;

public class TargetOverrideState {
    private static PlacementAttempt currentAttempt;
    private static boolean overrideEnabled = false;

    public static void setAttempt(PlacementAttempt attempt) {
        currentAttempt = attempt;
    }

    public static PlacementAttempt getAttempt() {
        return currentAttempt;
    }

    public static void clearAttempt() {
        currentAttempt = null;
    }

    public static boolean hasAttempt() {
        return currentAttempt != null;
    }

    public static boolean isOverrideEnabled() {
        return overrideEnabled;
    }

    public static void setOverrideEnabled(boolean enabled) {
        overrideEnabled = enabled;
    }

    public static boolean shouldOverride() {
        return overrideEnabled && currentAttempt != null;
    }
}

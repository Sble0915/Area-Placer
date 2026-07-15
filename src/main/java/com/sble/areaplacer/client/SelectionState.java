package com.sble.areaplacer.client;

import net.minecraft.core.BlockPos;

public class SelectionState {
    private static BlockPos pos1;
    private static BlockPos pos2;
    private static boolean enabled = false;

    public static void setPos1(BlockPos pos) {
        pos1 = pos.immutable();
    }

    public static void setPos2(BlockPos pos) {
        pos2 = pos.immutable();
    }

    public static BlockPos getPos1() {
        return pos1;
    }

    public static BlockPos getPos2() {
        return pos2;
    }

    public static boolean hasSelection() {
        return pos1 != null && pos2 != null;
    }

    public static void clear() {
        pos1 = null;
        pos2 = null;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void toggleEnabled() {
        enabled = !enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static int minX() {
        return Math.min(pos1.getX(), pos2.getX());
    }

    public static int minY() {
        return Math.min(pos1.getY(), pos2.getY());
    }

    public static int minZ() {
        return Math.min(pos1.getZ(), pos2.getZ());
    }

    public static int maxX() {
        return Math.max(pos1.getX(), pos2.getX());
    }

    public static int maxY() {
        return Math.max(pos1.getY(), pos2.getY());
    }

    public static int maxZ() {
        return Math.max(pos1.getZ(), pos2.getZ());
    }
}

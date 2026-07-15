package com.sble.areaplacer.client;

import net.minecraft.core.BlockPos;

public class TargetState {
    private static BlockPos currentTarget;

    public static void setCurrentTarget(BlockPos pos) {
        currentTarget = pos;
    }

    public static BlockPos getCurrentTarget() {
        return currentTarget;
    }

    public static void clear() {
        currentTarget = null;
    }
}

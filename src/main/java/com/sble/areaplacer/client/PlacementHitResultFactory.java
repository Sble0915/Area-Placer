package com.sble.areaplacer.client;

import net.minecraft.world.phys.BlockHitResult;

public class PlacementHitResultFactory {
    public static BlockHitResult fromAttempt(PlacementAttempt attempt) {
        if (attempt == null) return null;

        return new BlockHitResult(
                attempt.getHitVec(),
                attempt.getClickedFace(),
                attempt.getClickedBlockPos(),
                false
        );
    }
}

package com.example.areaplacer.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class PlacementAttempt {
    private final BlockPos targetPos;
    private final BlockPos clickedBlockPos;
    private final Direction clickedFace;
    private final Vec3 hitVec;

    public PlacementAttempt(BlockPos targetPos, BlockPos clickedBlockPos, Direction clickedFace, Vec3 hitVec) {
        this.targetPos = targetPos;
        this.clickedBlockPos = clickedBlockPos;
        this.clickedFace = clickedFace;
        this.hitVec = hitVec;
    }

    public BlockPos getTargetPos() {
        return targetPos;
    }

    public BlockPos getClickedBlockPos() {
        return clickedBlockPos;
    }

    public Direction getClickedFace() {
        return clickedFace;
    }

    public Vec3 getHitVec() {
        return hitVec;
    }
}

package com.example.areaplacer.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PlacementTargetFinder {
    private static final Direction[] SEARCH_ORDER = new Direction[] {
            Direction.DOWN,
            Direction.NORTH,
            Direction.SOUTH,
            Direction.WEST,
            Direction.EAST,
            Direction.UP
    };

    public static PlacementAttempt findNextAttempt() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return null;
        if (!SelectionState.hasSelection()) return null;

        ItemStack held = mc.player.getMainHandItem();

        // 1. 씨앗류/작물류는 farmland 위 planting 후보를 최우선으로 찾음
        if (isFarmlandPlantItem(held)) {
            PlacementAttempt farmingAttempt = findFarmlandPlantAttempt();
            if (farmingAttempt != null) {
                return farmingAttempt;
            }
        }

        // 2. 일반 설치 로직
        List<BlockPos> all = AreaIterator.getAllPositions();

        for (BlockPos targetPos : all) {
            if (!ReachUtil.isWithinReach(mc.player, targetPos, 6.0)) {
                continue;
            }

            BlockState targetState = mc.level.getBlockState(targetPos);

            if (!targetState.canBeReplaced()) {
                continue;
            }

            PlacementAttempt attempt = findAttemptForTarget(targetPos);
            if (attempt != null) {
                return attempt;
            }
        }

        return null;
    }

    private static PlacementAttempt findFarmlandPlantAttempt() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return null;

        List<BlockPos> all = AreaIterator.getAllPositions();

        for (BlockPos targetPos : all) {
            if (!ReachUtil.isWithinReach(mc.player, targetPos, 6.0)) {
                continue;
            }

            BlockState targetState = mc.level.getBlockState(targetPos);
            if (!targetState.canBeReplaced()) {
                continue;
            }

            BlockPos below = targetPos.below();
            BlockState belowState = mc.level.getBlockState(below);

            // 아래가 farmland여야 함
            if (!(belowState.getBlock() instanceof FarmBlock)) {
                continue;
            }

            // farmland의 윗면을 클릭해서 심기
            Direction clickedFace = Direction.UP;
            Vec3 hitVec = Vec3.atCenterOf(below).add(0.0, 0.5, 0.0);

            return new PlacementAttempt(targetPos, below, clickedFace, hitVec);
        }

        return null;
    }

    private static PlacementAttempt findAttemptForTarget(BlockPos targetPos) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return null;

        for (Direction face : SEARCH_ORDER) {
            BlockPos clickedBlockPos = targetPos.relative(face);
            BlockState clickedState = mc.level.getBlockState(clickedBlockPos);

            if (clickedState.isAir()) {
                continue;
            }

            Direction clickedFace = face.getOpposite();

            Vec3 hitVec = Vec3.atCenterOf(clickedBlockPos)
                    .add(
                            clickedFace.getStepX() * 0.5,
                            clickedFace.getStepY() * 0.5,
                            clickedFace.getStepZ() * 0.5
                    );

            return new PlacementAttempt(targetPos, clickedBlockPos, clickedFace, hitVec);
        }

        return null;
    }

    private static boolean isFarmlandPlantItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (!(stack.getItem() instanceof BlockItem blockItem)) return false;

        Block placedBlock = blockItem.getBlock();

        // 밀/당근/감자/비트/네더와트 계열 등 crop 류
        if (placedBlock instanceof CropBlock) {
            return true;
        }

        // 호박/수박 줄기
        if (placedBlock instanceof StemBlock) {
            return true;
        }

        // ItemNameBlockItem 계열은 씨앗류가 많아서 같이 허용
        return stack.getItem() instanceof ItemNameBlockItem;
    }
}

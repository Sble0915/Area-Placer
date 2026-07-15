package com.sble.areaplacer.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

        // 1. 괭이 우선: 흙/잔디를 farmland로 만들기
        if (isHoeItem(held)) {
            PlacementAttempt hoeAttempt = findHoeTillAttempt();
            if (hoeAttempt != null) {
                return hoeAttempt;
            }
        }

        // 2. 씨앗류/작물류 우선: farmland 위 planting
        if (isFarmlandPlantItem(held)) {
            PlacementAttempt farmingAttempt = findFarmlandPlantAttempt();
            if (farmingAttempt != null) {
                return farmingAttempt;
            }
        }

        // 3. 일반 설치 로직
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

    private static PlacementAttempt findHoeTillAttempt() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return null;

        List<BlockPos> all = AreaIterator.getAllPositions();

        for (BlockPos pos : all) {
            if (!ReachUtil.isWithinReach(mc.player, pos, 6.0)) {
                continue;
            }

            BlockState state = mc.level.getBlockState(pos);
            Block block = state.getBlock();

            // 우선 흙 / 잔디만 처리
            if (!(block == Blocks.DIRT || block == Blocks.GRASS_BLOCK)) {
                continue;
            }

            // 위가 비어 있어야 괭이질 가능
            BlockPos above = pos.above();
            BlockState aboveState = mc.level.getBlockState(above);
            if (!aboveState.canBeReplaced()) {
                continue;
            }

            // 블록 윗면을 클릭해서 경작
            Direction clickedFace = Direction.UP;
            Vec3 hitVec = Vec3.atCenterOf(pos).add(0.0, 0.5, 0.0);

            return new PlacementAttempt(pos, pos, clickedFace, hitVec);
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

            if (!(belowState.getBlock() instanceof FarmBlock)) {
                continue;
            }

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

    private static boolean isHoeItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItem() instanceof HoeItem;
    }

    private static boolean isFarmlandPlantItem(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (!(stack.getItem() instanceof BlockItem blockItem)) return false;

        Block placedBlock = blockItem.getBlock();

        if (placedBlock instanceof CropBlock) {
            return true;
        }

        if (placedBlock instanceof StemBlock) {
            return true;
        }

        return stack.getItem() instanceof ItemNameBlockItem;
    }
}

package com.example.areaplacer.client;

import com.example.areaplacer.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ClientTicker {
    public static void clientTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }

        while (Keybinds.SET_POS1.consumeClick()) {
            BlockPos looked = getLookedBlockPos(mc);
            if (looked != null) {
                SelectionState.setPos1(looked);
                ChatUtil.sendClientMessage("pos1 set: " + looked.toShortString());
            } else {
                ChatUtil.sendClientMessage("No block targeted for pos1.");
            }
        }

        while (Keybinds.SET_POS2.consumeClick()) {
            BlockPos looked = getLookedBlockPos(mc);
            if (looked != null) {
                SelectionState.setPos2(looked);
                ChatUtil.sendClientMessage("pos2 set: " + looked.toShortString());
            } else {
                ChatUtil.sendClientMessage("No block targeted for pos2.");
            }
        }

        while (Keybinds.TOGGLE_ENABLED.consumeClick()) {
            SelectionState.toggleEnabled();
            TargetOverrideState.setOverrideEnabled(SelectionState.isEnabled());
            ChatUtil.sendClientMessage("Enabled: " + SelectionState.isEnabled());
        }

        while (Keybinds.CLEAR_SELECTION.consumeClick()) {
            SelectionState.clear();
            TargetState.clear();
            TargetOverrideState.clearAttempt();
            ChatUtil.sendClientMessage("Selection cleared.");
        }

        if (SelectionState.isEnabled() && SelectionState.hasSelection()) {
            PlacementAttempt attempt = PlacementTargetFinder.findNextAttempt();
            if (attempt != null) {
                TargetState.setCurrentTarget(attempt.getTargetPos());
                TargetOverrideState.setAttempt(attempt);
                TargetOverrideState.setOverrideEnabled(true);
            } else {
                TargetState.clear();
                TargetOverrideState.clearAttempt();
            }
        } else {
            TargetState.clear();
            TargetOverrideState.clearAttempt();
            TargetOverrideState.setOverrideEnabled(false);
        }
    }

    private static BlockPos getLookedBlockPos(Minecraft mc) {
        HitResult hit = mc.hitResult;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) {
            return null;
        }

        BlockHitResult bhr = (BlockHitResult) hit;
        return bhr.getBlockPos();
    }
}

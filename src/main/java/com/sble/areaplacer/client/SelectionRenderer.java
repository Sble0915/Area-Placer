package com.example.areaplacer.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public class SelectionRenderer {
    public static void render(PoseStack poseStack, double camX, double camY, double camZ) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer lineBuffer = bufferSource.getBuffer(RenderType.lines());

        if (SelectionState.hasSelection()) {
            AABB box = new AABB(
                    SelectionState.minX(),
                    SelectionState.minY(),
                    SelectionState.minZ(),
                    SelectionState.maxX() + 1,
                    SelectionState.maxY() + 1,
                    SelectionState.maxZ() + 1
            ).move(-camX, -camY, -camZ);

            LevelRenderer.renderLineBox(
                    poseStack,
                    lineBuffer,
                    box,
                    0.2f, 0.8f, 1.0f, 1.0f
            );
        }

        BlockPos target = TargetState.getCurrentTarget();
        if (target != null) {
            AABB targetBox = new AABB(target).inflate(0.01).move(-camX, -camY, -camZ);

            LevelRenderer.renderLineBox(
                    poseStack,
                    lineBuffer,
                    targetBox,
                    1.0f, 0.2f, 0.2f, 1.0f
            );
        }

        bufferSource.endBatch(RenderType.lines());
    }
}

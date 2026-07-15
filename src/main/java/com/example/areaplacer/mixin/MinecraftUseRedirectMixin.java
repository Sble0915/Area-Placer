package com.example.areaplacer.mixin;

import com.example.areaplacer.client.PlacementAttempt;
import com.example.areaplacer.client.PlacementHitResultFactory;
import com.example.areaplacer.client.TargetOverrideState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftUseRedirectMixin {
    @Shadow
    public HitResult hitResult;

    @Redirect(
            method = "startUseItem",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;hitResult:Lnet/minecraft/world/phys/HitResult;"
            )
    )
    private HitResult areaplacer$redirectHitResult(Minecraft instance) {
        if (TargetOverrideState.shouldOverride()) {
            PlacementAttempt attempt = TargetOverrideState.getAttempt();
            BlockHitResult forced = PlacementHitResultFactory.fromAttempt(attempt);
            if (forced != null) {
                return forced;
            }
        }

        return this.hitResult;
    }
}

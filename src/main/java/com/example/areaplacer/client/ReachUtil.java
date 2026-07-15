package com.example.areaplacer.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class ReachUtil {
    public static boolean isWithinReach(Player player, BlockPos pos, double maxDistance) {
        double dx = player.getX() - (pos.getX() + 0.5);
        double dy = player.getEyeY() - (pos.getY() + 0.5);
        double dz = player.getZ() - (pos.getZ() + 0.5);
        return dx * dx + dy * dy + dz * dz <= maxDistance * maxDistance;
    }
}

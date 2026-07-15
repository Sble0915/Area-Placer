package com.sble.areaplacer.client;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class AreaIterator {
    public static List<BlockPos> getAllPositions() {
        List<BlockPos> result = new ArrayList<>();

        if (!SelectionState.hasSelection()) {
            return result;
        }

        for (int y = SelectionState.minY(); y <= SelectionState.maxY(); y++) {
            for (int z = SelectionState.minZ(); z <= SelectionState.maxZ(); z++) {
                for (int x = SelectionState.minX(); x <= SelectionState.maxX(); x++) {
                    result.add(new BlockPos(x, y, z));
                }
            }
        }

        return result;
    }
}

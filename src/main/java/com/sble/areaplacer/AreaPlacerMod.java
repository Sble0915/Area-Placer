package com.sble.areaplacer;

import com.sble.areaplacer.client.ClientInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(AreaPlacerMod.MOD_ID)
public class AreaPlacerMod {
    public static final String MOD_ID = "areaplacer";

    public AreaPlacerMod(IEventBus modEventBus) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientInit.init(modEventBus);
        }
    }
}

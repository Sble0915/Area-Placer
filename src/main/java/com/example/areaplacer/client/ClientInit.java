package com.example.areaplacer.client;

import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ClientInit {
    public static void init(IEventBus modBus) {
        modBus.addListener(ClientInit::registerKeys);

        NeoForge.EVENT_BUS.addListener(ClientInit::onClientTick);
        NeoForge.EVENT_BUS.addListener(ClientInit::onRenderLevelStage);
    }

    private static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(Keybinds.SET_POS1);
        event.register(Keybinds.SET_POS2);
        event.register(Keybinds.TOGGLE_ENABLED);
        event.register(Keybinds.CLEAR_SELECTION);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        if (Minecraft.getInstance().isPaused()) return;
        ClientTicker.clientTick();
    }

    private static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        double camX = mc.gameRenderer.getMainCamera().getPosition().x;
        double camY = mc.gameRenderer.getMainCamera().getPosition().y;
        double camZ = mc.gameRenderer.getMainCamera().getPosition().z;

        SelectionRenderer.render(
                event.getPoseStack(),
                camX, camY, camZ
        );
    }
}

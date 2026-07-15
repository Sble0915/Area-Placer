package com.example.areaplacer.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static final String CATEGORY = "key.categories.areaplacer";

    public static final KeyMapping SET_POS1 = new KeyMapping(
            "key.areaplacer.set_pos1",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_BRACKET,
            CATEGORY
    );

    public static final KeyMapping SET_POS2 = new KeyMapping(
            "key.areaplacer.set_pos2",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_BRACKET,
            CATEGORY
    );

    public static final KeyMapping TOGGLE_ENABLED = new KeyMapping(
            "key.areaplacer.toggle_enabled",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_BACKSLASH,
            CATEGORY
    );

    public static final KeyMapping CLEAR_SELECTION = new KeyMapping(
            "key.areaplacer.clear_selection",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            CATEGORY
    );
}

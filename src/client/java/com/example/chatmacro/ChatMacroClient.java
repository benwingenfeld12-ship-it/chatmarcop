package com.example.chatmacro;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class ChatMacroClient implements ClientModInitializer {
    // Ab 1.21.9 braucht ein KeyBinding eine Category (kein String mehr)
    private static final KeyBinding.Category CATEGORY =
        KeyBinding.Category.create(Identifier.of("chatmacro", "general"));

    private static KeyBinding keyHello;
    private static KeyBinding keyGg;

    @Override
    public void onInitializeClient() {
        keyHello = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.chatmacro.hello", InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_H, CATEGORY));

        keyGg = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.chatmacro.gg", InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G, CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyHello.wasPressed()) {
                while (keyHello.wasPressed()) { } // restliche Klicks verwerfen
                send(client, "Hallo zusammen!");
            }
            if (keyGg.wasPressed()) {
                while (keyGg.wasPressed()) { }
                send(client, "gg");
            }
        });
    }

    private void send(MinecraftClient client, String msg) {
        if (client.player == null) return;
        if (msg.startsWith("/")) {
            client.player.networkHandler.sendChatCommand(msg.substring(1));
        } else {
            client.player.networkHandler.sendChatMessage(msg);
        }
    }
}

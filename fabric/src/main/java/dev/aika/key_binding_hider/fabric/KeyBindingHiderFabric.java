package dev.aika.key_binding_hider.fabric;

import dev.aika.key_binding_hider.KeyBindingHider;
import net.fabricmc.api.ModInitializer;

public final class KeyBindingHiderFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        KeyBindingHider.init();
    }
}

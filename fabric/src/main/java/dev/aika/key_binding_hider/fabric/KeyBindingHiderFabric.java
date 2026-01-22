package dev.aika.key_binding_hider.fabric;

import dev.aika.key_binding_hider.KeyBindingHider;
import net.fabricmc.api.ModInitializer;

public final class KeyBindingHiderFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        KeyBindingHider.init();
    }
}

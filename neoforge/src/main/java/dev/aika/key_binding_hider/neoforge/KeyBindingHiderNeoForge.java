package dev.aika.key_binding_hider.neoforge;

import dev.aika.key_binding_hider.KeyBindingHider;
import net.neoforged.fml.common.Mod;

@Mod(KeyBindingHider.MOD_ID)
public final class KeyBindingHiderNeoForge {
    public KeyBindingHiderNeoForge() {
        // Run our common setup.
        KeyBindingHider.init();
    }
}

package dev.aika.key_binding_hider.forge;

import dev.aika.key_binding_hider.KeyBindingHider;
import net.minecraftforge.fml.common.Mod;

@Mod(KeyBindingHider.MOD_ID)
public final class KeyBindingHiderForge {
    public KeyBindingHiderForge() {
        // Run our common setup.
        KeyBindingHider.init();
    }
}

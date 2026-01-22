package dev.aika.key_binding_hider;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;

public class KeyBindingHiderPlatform {
    @SuppressWarnings("unused")
    @ExpectPlatform
    public static void setKeyBindingToUnknown(KeyMapping k) {
        throw new AssertionError();
    }
}

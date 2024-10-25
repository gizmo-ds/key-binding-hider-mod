package dev.aika.key_binding_hider;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.KeyMapping;

import java.nio.file.Path;

public class KeyBindingHiderPlatform {
    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void SetKeyBindingToUnknown(KeyMapping binding) {
        throw new AssertionError();
    }
}

package dev.aika.key_binding_hider;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class KeyBindingHiderPlatform {
    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }
}

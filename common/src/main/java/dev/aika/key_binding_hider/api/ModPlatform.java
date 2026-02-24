package dev.aika.key_binding_hider.api;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

@SuppressWarnings("unused")
public class ModPlatform {
    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }
}

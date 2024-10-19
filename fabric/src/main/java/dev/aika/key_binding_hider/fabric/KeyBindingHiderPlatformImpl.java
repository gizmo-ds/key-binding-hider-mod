package dev.aika.key_binding_hider.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

@SuppressWarnings("unused")
public class KeyBindingHiderPlatformImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}

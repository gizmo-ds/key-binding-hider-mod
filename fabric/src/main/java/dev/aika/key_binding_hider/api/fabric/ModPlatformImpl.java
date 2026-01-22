package dev.aika.key_binding_hider.api.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

@SuppressWarnings("unused")
public class ModPlatformImpl {
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}

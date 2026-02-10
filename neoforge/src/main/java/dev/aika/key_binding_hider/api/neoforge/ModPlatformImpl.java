package dev.aika.key_binding_hider.api.neoforge;

import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

@SuppressWarnings("unused")
public class ModPlatformImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}

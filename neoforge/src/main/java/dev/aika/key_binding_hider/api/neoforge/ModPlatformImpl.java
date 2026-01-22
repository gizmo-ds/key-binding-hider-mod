package dev.aika.key_binding_hider.api.neoforge;

import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;

import java.nio.file.Path;

@SuppressWarnings("unused")
public class ModPlatformImpl {
    public static boolean isModLoaded(String modId) {
        return FMLLoader.getLoadingModList().getModFileById(modId) != null;
    }

    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}

package dev.aika.key_binding_hider.forge;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

@SuppressWarnings("unused")
public class KeyBindingHiderPlatformImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}

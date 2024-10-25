package dev.aika.key_binding_hider.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;

import java.nio.file.Path;

@SuppressWarnings("unused")
public class KeyBindingHiderPlatformImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }

    public static void SetKeyBindingToUnknown(KeyMapping binding) {
        binding.setKey(InputConstants.UNKNOWN);
    }
}

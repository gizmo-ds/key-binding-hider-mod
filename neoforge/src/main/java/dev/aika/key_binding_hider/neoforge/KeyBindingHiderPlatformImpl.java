package dev.aika.key_binding_hider.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.nio.file.Path;

@SuppressWarnings("unused")
public class KeyBindingHiderPlatformImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static void SetKeyBindingToUnknown(KeyMapping binding) {
        binding.setKeyModifierAndCode(KeyModifier.NONE, InputConstants.UNKNOWN);
    }
}

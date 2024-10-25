package dev.aika.key_binding_hider.forge;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraft.client.KeyMapping;

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

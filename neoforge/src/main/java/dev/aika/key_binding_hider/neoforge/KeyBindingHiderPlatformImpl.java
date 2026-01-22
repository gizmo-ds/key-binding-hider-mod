package dev.aika.key_binding_hider.neoforge;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyModifier;

@SuppressWarnings("unused")
public class KeyBindingHiderPlatformImpl {
    public static void setKeyBindingToUnknown(KeyMapping k) {
        k.setKeyModifierAndCode(KeyModifier.NONE, InputConstants.UNKNOWN);
    }
}

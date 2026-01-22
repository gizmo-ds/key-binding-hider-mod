package dev.aika.key_binding_hider.fabric;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

@SuppressWarnings("unused")
public class KeyBindingHiderPlatformImpl {
    public static void setKeyBindingToUnknown(KeyMapping k) {
        k.setKey(InputConstants.UNKNOWN);
    }
}

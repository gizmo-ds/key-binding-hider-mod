package dev.aika.key_binding_hider.mixin.client;

import dev.aika.key_binding_hider.KeyBindingHider;
import dev.aika.key_binding_hider.KeyBindingHiderPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
@Mixin(KeyBindsList.class)
public abstract class KeyBindsListMixin extends ContainerObjectSelectionList<KeyBindsList.Entry> {
    public KeyBindsListMixin(Minecraft minecraft, int width, int height, int y, int itemHeight) {
        super(minecraft, width, height, y, itemHeight);
    }

    @Redirect(method = "<init>", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;keyMappings:[Lnet/minecraft/client/KeyMapping;"))
    private KeyMapping[] hidingSpecificKeyBindings(final Options options) {
        return ArrayUtils.removeElements(options.keyMappings, Arrays.stream(options.keyMappings).filter(k -> {
            for (String key : KeyBindingHider.CONFIG.KeyBindings) {
                if (k.getName().startsWith(key)) {
                    if (KeyBindingHider.CONFIG.SetKeyBindingToUnknown)
                        KeyBindingHiderPlatform.SetKeyBindingToUnknown(k);
                    return true;
                }
            }
            return false;
        }).toArray(KeyMapping[]::new));
    }
}

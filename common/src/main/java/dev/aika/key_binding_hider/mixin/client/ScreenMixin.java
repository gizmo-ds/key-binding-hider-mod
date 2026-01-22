package dev.aika.key_binding_hider.mixin.client;

import dev.aika.key_binding_hider.KeyBindingHider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Unique
    private static boolean key_binding_hider$firstTitleScreen = false;

    @SuppressWarnings({"ConstantValue", "EqualsBetweenInconvertibleTypes"})
    @Inject(method = "rebuildWidgets", at = @At("HEAD"))
    private void rebuildWidgets(CallbackInfo ci) {
        if (!key_binding_hider$firstTitleScreen && this.getClass().equals(TitleScreen.class)) {
            key_binding_hider$firstTitleScreen = true;
            KeyBindingHider.applyKeyBinding();
        }
    }
}

package dev.aika.key_binding_hider.mixin.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.aika.key_binding_hider.KeyBindingHider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin {
    @Shadow
    @Final
    private String name;

    @Inject(method = "isDefault", at = @At("HEAD"), cancellable = true)
    private void isDefault(CallbackInfoReturnable<Boolean> cir) {
        if (KeyBindingHider.hiddenKeyMappings.contains(this.name)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getDefaultKey", at = @At("HEAD"), cancellable = true)
    private void getDefaultKey(CallbackInfoReturnable<InputConstants.Key> cir) {
        if (KeyBindingHider.hiddenKeyMappings.contains(this.name)) {
            cir.setReturnValue(InputConstants.UNKNOWN);
        }
    }
}

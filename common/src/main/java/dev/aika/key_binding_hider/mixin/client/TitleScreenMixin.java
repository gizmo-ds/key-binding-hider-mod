package dev.aika.key_binding_hider.mixin.client;

import dev.aika.key_binding_hider.KeyBindingHider;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    @Unique
    private static boolean key_binding_hider$firstTitleScreen = false;

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Override
    protected void rebuildWidgets() {
        super.rebuildWidgets();

        if (!key_binding_hider$firstTitleScreen) {
            key_binding_hider$firstTitleScreen = true;
            KeyBindingHider.applyKeyBinding();
        }
    }
}

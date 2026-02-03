package dev.aika.key_binding_hider.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.aika.key_binding_hider.KeyBindingHider;
import dev.aika.key_binding_hider.api.ModPlatform;
import dev.aika.key_binding_hider.client.gui.screen.MissingClothConfigScreen;
import dev.aika.key_binding_hider.compat.cloth.ClothConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuCompat implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent ->
                ModPlatform.isModLoaded("cloth-config2") ?
                        ClothConfigScreen.builder().setParent(parent).setConfig(KeyBindingHider.CONFIG).build()
                        : new MissingClothConfigScreen(parent);
    }
}

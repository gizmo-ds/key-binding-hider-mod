package dev.aika.key_binding_hider.neoforge;

import dev.aika.key_binding_hider.KeyBindingHider;
import dev.aika.key_binding_hider.api.ModPlatform;
import dev.aika.key_binding_hider.client.gui.screen.MissingClothConfigScreen;
import dev.aika.key_binding_hider.compat.ClothConfigScreen;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = KeyBindingHider.MOD_ID, dist = Dist.CLIENT)
public class KeyBindingHiderClientNeoForge {
    public KeyBindingHiderClientNeoForge(IEventBus ignoredEventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (ModContainer ignoredModContainer, Screen parent) ->
                        ModPlatform.isModLoaded("cloth_config") ?
                                ClothConfigScreen.builder()
                                        .setParent(parent)
                                        .setConfig(KeyBindingHider.CONFIG)
                                        .build()
                                : new MissingClothConfigScreen(parent)
        );
    }
}

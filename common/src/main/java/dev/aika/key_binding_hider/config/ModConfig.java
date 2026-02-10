package dev.aika.key_binding_hider.config;

import dev.aika.key_binding_hider.KeyBindingHider;
import dev.aika.key_binding_hider.api.ModPlatform;
import org.slf4j.Logger;

import java.io.File;

public abstract class ModConfig {
    protected static final Logger log = KeyBindingHider.LOGGER;

    protected static final File CONFIG_FILE = new File(ModPlatform.getConfigDirectory().toFile(), KeyBindingHider.MOD_ID + ".json");

    public abstract void save();
}

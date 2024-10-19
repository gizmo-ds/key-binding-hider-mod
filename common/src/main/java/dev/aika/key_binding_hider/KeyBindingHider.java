package dev.aika.key_binding_hider;

import dev.aika.key_binding_hider.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class KeyBindingHider {
    public static final String MOD_ID = "key_binding_hider";
    public static final String MOD_NAME = "Key Binding Hider";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static ModConfig CONFIG;

    public static void init() {
        try {
            CONFIG = ModConfig.load();
        } catch (IOException e) {
            LOGGER.error("Failed to load config", e);
        }
    }
}

package dev.aika.key_binding_hider.config;

import com.moandjiezana.toml.Toml;
import dev.aika.key_binding_hider.KeyBindingHider;
import dev.aika.key_binding_hider.api.ModPlatform;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public abstract class ModConfig {
    protected static final Logger log = KeyBindingHider.LOGGER;
    private static final Marker marker = MarkerFactory.getMarker("ModConfig");

    protected static final File CONFIG_FILE = new File(ModPlatform.getConfigDirectory().toFile(), KeyBindingHider.MOD_ID + ".toml");

    @SuppressWarnings("SameParameterValue")
    protected static <T> T load(Class<T> clazz) {
        try (final var reader = new FileReader(CONFIG_FILE)) {
            return new Toml().read(reader).to(clazz);
        } catch (IOException e) {
            log.error(marker, "Failed to parse config file: {}", CONFIG_FILE, e);
            return null;
        }
    }

    public abstract void save();
}

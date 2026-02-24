package dev.aika.key_binding_hider.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class KeyBindingHiderConfig extends ModConfig {
    private static final Marker marker = MarkerFactory.getMarker("KeyBindingHiderConfig");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public boolean HiddenKeyBindingsUseUnknown = false;
    public List<String> HiddenKeyPatterns = new ArrayList<>();
    public List<String> HiddenCategoryPatterns = new ArrayList<>();

    @SneakyThrows
    public static KeyBindingHiderConfig load() {
        if (!CONFIG_FILE.exists()) {
            final var defaultConfig = new KeyBindingHiderConfig();
            defaultConfig.save();
            return defaultConfig;
        }
        try (final var reader = new FileReader(CONFIG_FILE)) {
            return GSON.fromJson(reader, KeyBindingHiderConfig.class);
        } catch (IOException e) {
            log.error(marker, "Failed to parse config file: {}", CONFIG_FILE, e);
            return null;
        }
    }

    @Override
    public void save() {
        HiddenKeyPatterns.removeIf(s -> s == null || s.trim().isEmpty());
        HiddenCategoryPatterns.removeIf(s -> s == null || s.trim().isEmpty());

        try (final var writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            log.error(marker, "Failed to write config file {}", CONFIG_FILE, e);
        }
    }
}

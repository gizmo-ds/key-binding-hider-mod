package dev.aika.key_binding_hider.config;

import com.moandjiezana.toml.TomlWriter;
import dev.aika.key_binding_hider.KeyBindingHider;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class KeyBindingHiderConfig extends ModConfig {
    private static final Marker marker = MarkerFactory.getMarker("KeyBindingHiderConfig");
    private static final String CONFIG_COMMAND = """
            # Visit https://github.com/gizmo-ds/key-binding-hider-mod for detailed configuration options.
            """;
    private static final TomlWriter tomlWriter = new TomlWriter.Builder().build();

    public boolean HiddenKeyBindingsUseUnknown = false;
    public List<String> HiddenKeyPatterns = new ArrayList<>();
    public List<String> HiddenCategoryPatterns = new ArrayList<>();

    @SneakyThrows
    public static KeyBindingHiderConfig load() {
        if (CONFIG_FILE.exists())
            return KeyBindingHiderConfig.load(KeyBindingHiderConfig.class);
        final var defaultConfig = new KeyBindingHiderConfig();
        defaultConfig.save();
        return defaultConfig;
    }

    @Override
    public void save() {
        pass();

        try (final var writer = new FileWriter(CONFIG_FILE)) {
            writer.write(CONFIG_COMMAND);
            writer.write("\n");
            tomlWriter.write(this, writer);
        } catch (IOException e) {
            log.error(marker, "Failed to write config file {}", CONFIG_FILE, e);
        }
    }

    private void pass() {
        HiddenKeyPatterns.removeIf(s -> s == null || s.trim().isEmpty());
        HiddenCategoryPatterns.removeIf(s -> s == null || s.trim().isEmpty());

        KeyBindingHider.applyKeyBinding();
    }
}

package dev.aika.key_binding_hider.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import dev.aika.key_binding_hider.KeyBindingHider;
import dev.aika.key_binding_hider.KeyBindingHiderPlatform;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ModConfig {
    private static final File CONFIG_FILE = new File(KeyBindingHiderPlatform.getConfigDirectory().toFile(), KeyBindingHider.MOD_ID + ".toml");

    public List<String> KeyBindings;

    private static final String DEFAULT_CONFIG = """
            # Visit https://github.com/gizmo-ds/key-binding-hider-mod for detailed configuration options.
            
            # List of key bindings to hide
            KeyBindings = []
            """;

    public static ModConfig load() throws IOException {
        if (!CONFIG_FILE.exists()) {
            var config = new ModConfig();
            config.KeyBindings = List.of();
            if (!CONFIG_FILE.createNewFile()) throw new IOException("Failed to create config file");
            var writer = new FileWriter(CONFIG_FILE);
            writer.write(DEFAULT_CONFIG);
            writer.close();
            return ModConfig.save(config);
        }
        return new Toml().read(CONFIG_FILE).to(ModConfig.class);
    }

    public static ModConfig save(ModConfig conf) throws IOException {
        var writer = new TomlWriter();
        writer.write(conf, CONFIG_FILE);
        return conf;
    }
}

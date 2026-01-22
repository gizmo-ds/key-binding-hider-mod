package dev.aika.key_binding_hider;

import dev.aika.key_binding_hider.config.KeyBindingHiderConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class KeyBindingHider {
    public static final String MOD_ID = "key_binding_hider";
    public static final String MOD_NAME = "KeyBindingHider";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static KeyBindingHiderConfig CONFIG = new KeyBindingHiderConfig();

    public static List<String> hiddenKeyMappings = new ArrayList<>();

    public static void init() {
        CONFIG = KeyBindingHiderConfig.load();

        if (!CONFIG.getKeyBindings().isEmpty()) {
            LOGGER.warn("KeyBindings config is deprecated and will be removed in a future version.");
        }
    }

    public static void applyKeyBinding() {
        final KeyMapping[] keyMappings = Minecraft.getInstance().options.keyMappings;
//        final List<String> categories = Arrays.stream(keyMappings).map(KeyMapping::getCategory).distinct().toList();

        final List<KeyMapping> _hiddenKeyMappings = new ArrayList<>();

        final var oldConfigMatches = CONFIG.getKeyBindings();
        final var hiddenKeyPatterns = CONFIG.getHiddenKeyPatterns();
        final var hiddenKeyRegexPatterns = CONFIG.getHiddenKeyPatterns().stream().filter(c -> c.startsWith("#"))
                .map(c -> c.substring(1)).toList();
        final var hiddenCategoryPatterns = CONFIG.getHiddenCategoryPatterns();
        final var hiddenCategoryRegexPatterns = CONFIG.getHiddenCategoryPatterns().stream().filter(c -> c.startsWith("#"))
                .map(c -> c.substring(1)).toList();

        Arrays.stream(keyMappings).forEach(k -> {
            if (hiddenKeyPatterns.contains(k.getName())) _hiddenKeyMappings.add(k);
            hiddenKeyRegexPatterns.forEach(r -> {
                if (Pattern.matches(r, k.getName())) _hiddenKeyMappings.add(k);
            });
            oldConfigMatches.forEach(m -> {
                if (k.getName().startsWith(m)) _hiddenKeyMappings.add(k);
            });

            if (hiddenCategoryPatterns.contains(k.getCategory())) _hiddenKeyMappings.add(k);
            hiddenCategoryRegexPatterns.forEach(r -> {
                if (Pattern.matches(r, k.getCategory())) _hiddenKeyMappings.add(k);
            });
        });

        if (CONFIG.isSetKeyBindingToUnknown()) {
            _hiddenKeyMappings.forEach(KeyBindingHiderPlatform::setKeyBindingToUnknown);
        }

        hiddenKeyMappings = _hiddenKeyMappings.stream().map(KeyMapping::getName).toList();
    }
}

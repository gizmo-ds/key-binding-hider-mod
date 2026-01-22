package dev.aika.key_binding_hider.compat;

import dev.aika.key_binding_hider.KeyBindingHider;
import dev.aika.key_binding_hider.client.gui.ComponentBuilder;
import dev.aika.key_binding_hider.client.gui.components.ListBuilder;
import dev.aika.key_binding_hider.config.ModConfig;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class ClothConfigScreen {
    private static final Logger log = KeyBindingHider.LOGGER;

    public static ClothConfigScreenBuilder builder() {
        return new ClothConfigScreenBuilder();
    }

    @Accessors(chain = true)
    public static class ClothConfigScreenBuilder {
        private static final Marker marker = MarkerFactory.getMarker("ClothConfigScreenBuilder");
        private static final Language language = Language.getInstance();

        @Setter
        private String modId = KeyBindingHider.MOD_ID;
        @Setter
        private Screen parent;
        @Setter
        private ModConfig config;
        @Setter
        private Class<?> defaultConfigClass;

        private final List<Runnable> saveRunnables = new ArrayList<>();
        private final ConfigBuilder builder = ConfigBuilder.create();
        private ComponentBuilder componentBuilder;

        private ClothConfigScreenBuilder() {
        }

        @SneakyThrows
        public Screen build() {
            builder.setParentScreen(parent)
                    .setSavingRunnable(() -> saveRunnables.forEach(Runnable::run))
                    .setTitle(Component.translatable("config." + modId + ".@title"));

            saveRunnables.add(config::save);

            componentBuilder = new ComponentBuilder(builder.entryBuilder(), config).setModId(modId)
                    .setDefaultConfigObject(
                            Objects.requireNonNullElseGet(defaultConfigClass, () -> config.getClass())
                                    .getDeclaredConstructor().newInstance()
                    );

            for (final Field field : config.getClass().getDeclaredFields()) {
                addEntry(field);
            }

            getConfigCategory("general").addEntry(
                    builder.entryBuilder().startTextDescription(
                            Component.translatable("config.key_binding_hider.@footer")
                    ).build()
            );

            builder.transparentBackground();
            return builder.build();
        }

        private void addEntry(Field field) {
            if (Modifier.isFinal(field.getModifiers())) return;

            final String categoryKey = "general";
            final ConfigCategory configCategory = getConfigCategory(categoryKey);

            final Class<?> fieldType = field.getType();
            if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                configCategory.addEntry(componentBuilder.switchBuilder(field, categoryKey).build());
                return;
            } else if (fieldType.equals(List.class) && field.getGenericType() instanceof ParameterizedType pt) {
                final Class<?> listClass = (Class<?>) pt.getActualTypeArguments()[0];
                if (ListBuilder.isSupported(listClass)) configCategory.addEntry(
                        componentBuilder.listBuilder(
                                        field, categoryKey, (Class<?>) pt.getActualTypeArguments()[0])
                                .build());
                return;
            }
            configCategory.addEntry(componentBuilder.unsupportedBuilder(field).build());
            log.warn(marker, "Unsupported field type: {}", fieldType);
        }

        @SuppressWarnings("SameParameterValue")
        private ConfigCategory getConfigCategory(String categoryKey) {
            final ConfigCategory category = builder.getOrCreateCategory(
                    Component.translatable("config." + modId + "." + categoryKey)
            );
            final String descriptionKey = String.format("config.%s.%s.@description", modId, categoryKey);
            if (language.has(descriptionKey)) {
                category.setDescription(new FormattedText[]{Component.translatable(descriptionKey)});
            }
            return category;
        }
    }
}

package dev.aika.key_binding_hider.compat.cloth;

import dev.aika.key_binding_hider.KeyBindingHider;
import dev.aika.key_binding_hider.client.gui.ComponentBuilder;
import dev.aika.key_binding_hider.client.gui.components.ListBuilder;
import dev.aika.key_binding_hider.client.gui.screen.HiddenKeyBindingScreen;
import dev.aika.key_binding_hider.compat.cloth.widget.ClothButtonEntry;
import dev.aika.key_binding_hider.config.ModConfig;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
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
import java.util.stream.Collectors;

import static dev.aika.key_binding_hider.KeyBindingHider.CONFIG;

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

            final ConfigCategory general = getConfigCategory("general");
            final Minecraft mc = Minecraft.getInstance();
            final List<KeyMapping> keyMappings = List.of(mc.options.keyMappings);
            final List<KeyMapping> distinctByCategory = keyMappings.stream()
                    .collect(Collectors.toMap(
                            KeyMapping::getCategory,
                            km -> km,
                            (a, b) -> a,
                            LinkedHashMap::new
                    ))
                    .values()
                    .stream()
                    .toList();

            general.addEntry(new ClothButtonEntry(
                    Component.translatable("config.key_binding_hider.general.hiddenKeyIdList"),
                    Component.translatable("config.key_binding_hider.@open"),
                    button -> mc.setScreen(
                            new HiddenKeyBindingScreen(mc.screen,
                                    Component.translatable("gui.key_binding_hider.general.hiddenKeyIdList.@title"),
                                    keyMappings
                            ) {
                                private final List<String> hiddenKeys = new ArrayList<>(CONFIG.getHiddenKeyPatterns());

                                @Override
                                protected String getLabel(KeyMapping k) {
                                    return k.getName();
                                }

                                @Override
                                protected boolean getValue(KeyMapping k) {
                                    return hiddenKeys.contains(k.getName());
                                }

                                @Override
                                protected void setValue(KeyMapping k, boolean value) {
                                    if (value) hiddenKeys.add(k.getName());
                                    else hiddenKeys.remove(k.getName());
                                }

                                @Override
                                protected void apply() {
                                    CONFIG.HiddenKeyPatterns.clear();
                                    CONFIG.HiddenKeyPatterns.addAll(hiddenKeys);
                                    config.save();
                                    KeyBindingHider.applyKeyBinding();
                                    if (this.minecraft != null) {
                                        this.minecraft.setScreen(parent);
                                    }
                                }
                            }
                    )
            ));
            general.addEntry(new ClothButtonEntry(
                    Component.translatable("config.key_binding_hider.general.hiddenCategoryList"),
                    Component.translatable("config.key_binding_hider.@open"),
                    button -> mc.setScreen(
                            new HiddenKeyBindingScreen(mc.screen,
                                    Component.translatable("gui.key_binding_hider.general.hiddenCategoryList.@title"),
                                    distinctByCategory
                            ) {
                                private final List<String> hiddenCategories = new ArrayList<>(CONFIG.getHiddenCategoryPatterns());

                                @Override
                                protected String getLabel(KeyMapping k) {
                                    return k.getCategory();
                                }

                                @Override
                                protected boolean getValue(KeyMapping k) {
                                    return hiddenCategories.contains(k.getCategory());
                                }

                                @Override
                                protected void setValue(KeyMapping k, boolean value) {
                                    if (value) hiddenCategories.add(k.getCategory());
                                    else hiddenCategories.remove(k.getCategory());
                                }

                                @Override
                                protected void apply() {
                                    CONFIG.HiddenCategoryPatterns.clear();
                                    CONFIG.HiddenCategoryPatterns.addAll(hiddenCategories);
                                    config.save();
                                    KeyBindingHider.applyKeyBinding();
                                    if (this.minecraft != null) {
                                        this.minecraft.setScreen(parent);
                                    }
                                }
                            }
                    )
            ));
            for (final Field field : config.getClass().getDeclaredFields()) {
                addEntry(field);
            }
            general.addEntry(
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

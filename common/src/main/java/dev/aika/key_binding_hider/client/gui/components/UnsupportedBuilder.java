package dev.aika.key_binding_hider.client.gui.components;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;

public class UnsupportedBuilder<T> extends AbstractComponentBuilder<T> {
    public UnsupportedBuilder(ConfigEntryBuilder entryBuilder, Object object, Field field) {
        super(entryBuilder, object, field);
    }

    @Override
    public AbstractConfigListEntry<?> build() {
        final Class<?> clazz = field.getType();
        return entryBuilder.startTextDescription(
                        Component.translatable(String.format("config.%s.unsupported.description", modId),
                                clazz.getSimpleName())
                )
                .setTooltip(Component.literal(clazz.getName()))
                .build();
    }
}

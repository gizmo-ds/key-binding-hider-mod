package dev.aika.key_binding_hider.client.gui.components;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.AbstractTextFieldListListEntry;
import me.shedaniel.clothconfig2.impl.builders.AbstractListBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.lang.reflect.Field;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@Environment(EnvType.CLIENT)
public final class ListBuilder<T, A extends AbstractTextFieldListListEntry<T, ?, A>, SELF extends AbstractListBuilder<T, A, SELF>> extends AbstractComponentBuilder<List<T>> {
    private final Class<T> listClass;

    public ListBuilder(ConfigEntryBuilder entryBuilder, Object configObject,
                       Class<T> listClass, Field field) {
        super(entryBuilder, configObject, field);
        this.listClass = listClass;
    }

    public static boolean isSupported(Class<?> clazz) {
        return clazz.equals(String.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Double.class);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public AbstractConfigListEntry<List<T>> build() {
        final AbstractListBuilder<T, A, SELF> builder;
        if (listClass.equals(String.class)) {
            builder = (AbstractListBuilder<T, A, SELF>) entryBuilder.startStrList(fieldNameKey(), (List<String>) this.getValue());
        } else {
            throw new RuntimeException("Unsupported list class " + listClass.getName());
        }

        builder.setSaveConsumer(this::setValue);
        builder.setExpanded(true);

        fieldBuilderInit(builder);
        return builder.build();
    }
}

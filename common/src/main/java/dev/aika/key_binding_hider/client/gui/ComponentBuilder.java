package dev.aika.key_binding_hider.client.gui;

import dev.aika.key_binding_hider.client.gui.components.AbstractComponentBuilder;
import dev.aika.key_binding_hider.client.gui.components.ListBuilder;
import dev.aika.key_binding_hider.client.gui.components.SwitchBuilder;
import dev.aika.key_binding_hider.client.gui.components.UnsupportedBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.lang.reflect.Field;

@Accessors(chain = true)
@Environment(EnvType.CLIENT)
public class ComponentBuilder {
    @Getter
    protected final ConfigEntryBuilder entryBuilder;
    @Getter
    protected final Object configObject;
    @Setter
    protected Object defaultConfigObject;
    @Getter
    @Setter
    protected String modId;

    public ComponentBuilder(ConfigEntryBuilder entryBuilder, Object configObject) {
        this.entryBuilder = entryBuilder;
        this.configObject = configObject;
    }

    private <T extends AbstractComponentBuilder<?>> T createBuilder(String category, T builder) {
        builder.setModId(modId).setCategory(category);
        if (defaultConfigObject != null) builder.setDefaultConfigObject(defaultConfigObject);
        return builder;
    }

    public SwitchBuilder switchBuilder(Field field, String category) {
        return createBuilder(category, new SwitchBuilder(entryBuilder, configObject, field));
    }

    public <T> ListBuilder<T, ?, ?> listBuilder(Field field, String category, Class<T> listClass) {
        return createBuilder(category, new ListBuilder<>(entryBuilder, configObject, listClass, field));
    }

    public <T> UnsupportedBuilder<T> unsupportedBuilder(Field field) {
        return createBuilder("", new UnsupportedBuilder<>(entryBuilder, configObject, field));
    }
}

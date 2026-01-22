package dev.aika.key_binding_hider.client.gui.components;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;

public final class SwitchBuilder extends AbstractComponentBuilder<Boolean> {
    public static final String DefaultCheckedText = "true";
    public static final String DefaultUncheckedText = "false";

    public SwitchBuilder(ConfigEntryBuilder entryBuilder, Object object, Field field) {
        super(entryBuilder, object, field);
    }

    @Override
    public AbstractConfigListEntry<Boolean> build() {
        final BooleanToggleBuilder builder = entryBuilder.startBooleanToggle(fieldNameKey(), getValue())
                .setSaveConsumer(this::setValue)
                .setYesNoTextSupplier(this::yesNoTextSupplier);
        fieldBuilderInit(builder);
        return builder.build();
    }

    private Component yesNoTextSupplier(Boolean value) {
        return Component.translatable(
                String.format("config.%s.@switch.%s", modId, (value ? DefaultCheckedText : DefaultUncheckedText))
        );
    }
}

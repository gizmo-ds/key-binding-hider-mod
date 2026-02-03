package dev.aika.key_binding_hider.compat.cloth.widget;

import com.google.common.collect.ImmutableList;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class ClothButtonEntry extends AbstractConfigListEntry<Boolean> {
    private final Button button;

    public ClothButtonEntry(Component fieldName, Component buttonName, Button.OnPress onPress) {
        super(fieldName, false);

        this.button = Button.builder(buttonName, onPress).build();
    }

    public ClothButtonEntry(Component fieldName, Button.OnPress onPress) {
        this(Component.empty(), fieldName, onPress);
    }

    @Override
    public Boolean getValue() {
        return true;
    }

    @Override
    public Optional<Boolean> getDefaultValue() {
        return Optional.of(true);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return ImmutableList.of(
                button
        );
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return ImmutableList.of(
                button
        );
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        button.setX(x + entryWidth - 150);
        button.setY(y);
        button.render(graphics, mouseX, mouseY, delta);
        Component displayedFieldName = this.getDisplayedFieldName();
        if (!displayedFieldName.getString().isEmpty()) {
            graphics.drawString(
                    Minecraft.getInstance().font, displayedFieldName.getVisualOrderText(),
                    x, y + 6, this.getPreferredTextColor()
            );
        }
        super.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
    }
}

package dev.aika.key_binding_hider.client.gui.screen;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class HiddenKeyBindingScreen extends Screen {
    private final Screen lastScreen;

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private KeyMappingList keyMappingList;
    private final List<KeyMapping> keyMappings;

    public HiddenKeyBindingScreen(Screen lastScreen, Component title, final List<KeyMapping> keyMappings) {
        super(title);

        this.lastScreen = lastScreen;
        this.keyMappings = keyMappings;
    }

    protected abstract String getLabel(final KeyMapping k);

    protected abstract boolean getValue(KeyMapping k);

    protected abstract void setValue(KeyMapping k, boolean value);

    protected abstract void apply();

    @Override
    protected void init() {
        super.init();

        this.layout.addTitleHeader(this.title, this.font);
        this.addContents();
        this.addFooter();

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.lastScreen);
        }
    }

    public void onApply() {
        this.apply();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        if (this.keyMappingList != null) {
            this.keyMappingList.updateSize(this.width, this.layout);
        }
    }

    private void addContents() {
        this.keyMappingList = new HiddenKeyBindingScreen.KeyMappingList();
        this.layout.addToContents(this.keyMappingList);
    }

    private void addFooter() {
        final LinearLayout footerLayout = LinearLayout.horizontal().spacing(8);
        footerLayout.addChild(Button.builder(CommonComponents.GUI_BACK, b -> this.onClose()).width(120).build());
        footerLayout.addChild(Button.builder(
                        Component.translatable("config.key_binding_hider.@apply"),
                        b -> this.onApply())
                .width(120).build());
        this.layout.addToFooter(footerLayout);
    }

    class KeyMappingList extends ContainerObjectSelectionList<KeyMappingEntry> {
        public KeyMappingList() {
            super(
                    Minecraft.getInstance(),
                    HiddenKeyBindingScreen.this.width,
                    HiddenKeyBindingScreen.this.layout.getContentHeight(),
                    HiddenKeyBindingScreen.this.layout.getHeaderHeight(),
                    24
            );
            for (final KeyMapping k : HiddenKeyBindingScreen.this.keyMappings) {
                this.addEntry(new BooleanKeyMappingEntry(k, HiddenKeyBindingScreen.this.getValue(k)));
            }
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

            final var hoveredEntry = this.getHovered();
            if (hoveredEntry != null) {
                HiddenKeyBindingScreen.this.setTooltipForNextRenderPass(
                        Component.literal(
                                HiddenKeyBindingScreen.this.getLabel(hoveredEntry.keyMapping)
                        )
                );
            }
        }
    }

    class BooleanKeyMappingEntry extends KeyMappingEntry {
        private final CycleButton<Boolean> checkbox;

        public BooleanKeyMappingEntry(final KeyMapping keyMapping, final boolean initialValue) {
            super(keyMapping);
            this.checkbox = CycleButton.booleanBuilder(
                            Component.translatable("config.key_binding_hider.@switch.hide"),
                            Component.translatable("config.key_binding_hider.@switch.show"))
                    .withInitialValue(initialValue)
                    .displayOnlyValue()
                    .create(10, 5, 44, 20,
                            Component.literal(HiddenKeyBindingScreen.this.getLabel(keyMapping)),
                            (b, value) -> HiddenKeyBindingScreen.this.setValue(keyMapping, value));
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            this.renderLabel(guiGraphics, top, left);
            this.checkbox.setX(left + width - 45);
            this.checkbox.setY(top);
            this.checkbox.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(checkbox);
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return ImmutableList.of(checkbox);
        }

        public void renderLabel(GuiGraphics guiGraphics, int top, int left) {
            if (HiddenKeyBindingScreen.this.minecraft != null) {
                guiGraphics.drawString(
                        HiddenKeyBindingScreen.this.minecraft.font,
                        Component.translatable(HiddenKeyBindingScreen.this.getLabel(this.keyMapping)),
                        left, top + 5, -1, false
                );
            }
        }
    }

    abstract static class KeyMappingEntry extends ContainerObjectSelectionList.Entry<KeyMappingEntry> {
        final @NotNull KeyMapping keyMapping;

        public KeyMappingEntry(final @NotNull KeyMapping keyMapping) {
            this.keyMapping = keyMapping;
        }
    }
}

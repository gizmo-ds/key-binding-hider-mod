package dev.aika.key_binding_hider.client.gui.screen;

import com.google.common.collect.ImmutableList;
import dev.aika.key_binding_hider.KeyBindingHider;
import lombok.NonNull;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

import static dev.aika.key_binding_hider.KeyBindingHider.CONFIG;

public class KeyBindingHiderConfigScreen extends Screen {
    private final Screen lastScreen;

    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final List<KeyMapping> keyMappings;
    private final List<String> hiddenKeyPatterns;
    private final List<String> hiddenCategoryPatterns;
    private Boolean hiddenKeyBindingsUseUnknown;

    public KeyBindingHiderConfigScreen(final Screen lastScreen) {
        super(Component.translatable("config.key_binding_hider.@title"));

        this.lastScreen = lastScreen;
        this.keyMappings = Arrays.stream(ArrayUtils.clone(Minecraft.getInstance().options.keyMappings)).sorted().toList();
        this.hiddenKeyPatterns = new ArrayList<>(KeyBindingHider.CONFIG.HiddenKeyPatterns);
        this.hiddenCategoryPatterns = new ArrayList<>(KeyBindingHider.CONFIG.HiddenCategoryPatterns);
        this.hiddenKeyBindingsUseUnknown = CONFIG.isHiddenKeyBindingsUseUnknown();
    }

    @Override
    protected void init() {
        super.init();

        this.addHeader();
        this.layout.addToContents(new KeyMappingList());
        this.addFooter();

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) this.minecraft.setScreen(this.lastScreen);
    }

    public void apply() {
        CONFIG.HiddenKeyPatterns.clear();
        CONFIG.HiddenCategoryPatterns.clear();
        CONFIG.HiddenKeyPatterns.addAll(this.hiddenKeyPatterns);
        CONFIG.HiddenCategoryPatterns.addAll(this.hiddenCategoryPatterns);
        CONFIG.HiddenKeyBindingsUseUnknown = this.hiddenKeyBindingsUseUnknown;
        CONFIG.save();
        KeyBindingHider.applyKeyBinding();
        this.onClose();
    }

    public void apply(Button btn) {
        this.apply();
    }

    private void addHeader() {
        final LinearLayout headerLayout = this.layout.addToHeader(LinearLayout.vertical().spacing(8));
        headerLayout.defaultCellSetting().alignHorizontallyCenter();
        headerLayout.addChild(new StringWidget(this.title, this.font));

        final LinearLayout optionsLayout = headerLayout.addChild(LinearLayout.horizontal().spacing(6));
        optionsLayout.addChild(CycleButton.onOffBuilder(this.hiddenKeyBindingsUseUnknown)
                .withTooltip(v -> Tooltip.create(Component.translatable("config.key_binding_hider.general.HiddenKeyBindingsUseUnknown.@tooltip")))
                .create(0, 0, 200, 20,
                        Component.translatable("config.key_binding_hider.general.HiddenKeyBindingsUseUnknown"),
                        (ignored, value) -> this.hiddenKeyBindingsUseUnknown = value
                ));

        this.layout.setHeaderHeight(33 + 22);
    }

    private void addFooter() {
        final LinearLayout footerLayout = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footerLayout.addChild(Button.builder(CommonComponents.GUI_BACK, b -> this.onClose())
                .width(120).build());
        footerLayout.addChild(Button.builder(Component.translatable("config.key_binding_hider.@apply"), this::apply)
                .width(120).build());
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    class KeyMappingList extends ContainerObjectSelectionList<KeyMappingEntry> {
        public KeyMappingList() {
            super(
                    Minecraft.getInstance(),
                    KeyBindingHiderConfigScreen.this.width,
                    KeyBindingHiderConfigScreen.this.layout.getContentHeight(),
                    KeyBindingHiderConfigScreen.this.layout.getHeaderHeight(),
                    20
            );

            final Map<String, List<KeyMappingEntry>> map = new LinkedHashMap<>();
            for (final KeyMapping k : KeyBindingHiderConfigScreen.this.keyMappings) {
                if (!map.containsKey(k.getCategory())) map.put(k.getCategory(), new ArrayList<>());
                map.get(k.getCategory()).add(new KeyBindingEntry(k));
            }
            for (final String id : map.keySet()) {
                this.addEntry(new CategoryEntry(id));
                map.get(id).forEach(this::addEntry);
            }
        }

        @Override
        public int getRowWidth() {
            return 340;
        }

        @Override
        public void renderWidget(@NonNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

            if (this.getHovered() instanceof KeyMappingEntry entry) {
                KeyBindingHiderConfigScreen.this.setTooltipForNextRenderPass(entry.getTooltip());
            }
        }
    }

    class CategoryEntry extends KeyMappingEntry {
        private final String id;
        private final StringWidget label;
        private final CycleButton<Boolean> button;

        public CategoryEntry(final String id) {
            this.id = id;
            this.label = new StringWidget(Component.translatable(id).withColor(0xFFD600), font);
            this.button = CycleButton.booleanBuilder(
                            Component.translatable("config.key_binding_hider.@switch.hide"),
                            Component.translatable("config.key_binding_hider.@switch.show")
                    )
                    .withInitialValue(KeyBindingHiderConfigScreen.this.hiddenCategoryPatterns.contains(id))
                    .displayOnlyValue()
                    .create(10, 5, 44, 20, Component.literal(id), this::onValueChange);
        }

        private void onValueChange(CycleButton<Boolean> ignored, boolean value) {
            if (value) KeyBindingHiderConfigScreen.this.hiddenCategoryPatterns.add(this.id);
            else KeyBindingHiderConfigScreen.this.hiddenCategoryPatterns.remove(this.id);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            this.button.setPosition(left + width - 45, top);
            this.button.render(guiGraphics, mouseX, mouseY, partialTick);
            this.label.setPosition(KeyBindingHiderConfigScreen.this.width / 2 - this.label.getWidth() / 2, top + 5);
            this.label.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public @NonNull List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.label, this.button);
        }

        @Override
        public @NonNull List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(this.label, this.button);
        }

        @Override
        Component getTooltip() {
            return Component.translatable("config.key_binding_hider.category.@tooltip", this.id);
        }
    }

    class KeyBindingEntry extends KeyMappingEntry {
        private final KeyMapping keyMapping;
        private final StringWidget label;
        private final CycleButton<Boolean> button;

        public KeyBindingEntry(final KeyMapping k) {
            this.keyMapping = k;
            this.label = new StringWidget(Component.translatable(k.getName()), font);
            this.button = CycleButton.booleanBuilder(
                            Component.translatable("config.key_binding_hider.@switch.hide"),
                            Component.translatable("config.key_binding_hider.@switch.show")
                    )
                    .withInitialValue(KeyBindingHiderConfigScreen.this.hiddenKeyPatterns.contains(k.getName()))
                    .displayOnlyValue()
                    .create(0, 5, 44, 20,
                            Component.literal(k.getName()), this::onValueChange);
        }

        private void onValueChange(CycleButton<Boolean> ignored, boolean value) {
            if (value) KeyBindingHiderConfigScreen.this.hiddenKeyPatterns.add(this.keyMapping.getName());
            else KeyBindingHiderConfigScreen.this.hiddenKeyPatterns.remove(this.keyMapping.getName());
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            this.button.setPosition(left + width - 45, top);
            this.button.render(guiGraphics, mouseX, mouseY, partialTick);
            this.label.setPosition(left, top + 5);
            this.label.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        @Override
        public @NonNull List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.label, this.button);
        }

        @Override
        public @NonNull List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(this.label, this.button);
        }

        @Override
        Component getTooltip() {
            return Component.translatable("config.key_binding_hider.key_binding.@tooltip", this.keyMapping.getName());
        }
    }

    abstract static class KeyMappingEntry extends ContainerObjectSelectionList.Entry<KeyMappingEntry> {
        abstract Component getTooltip();
    }
}

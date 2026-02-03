package dev.aika.key_binding_hider.client.gui.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class WarningScreen extends Screen {
    protected final Screen lastScreen;
    protected final Component message;
    protected LinearLayout layout = LinearLayout.vertical().spacing(6);

    public WarningScreen(Screen lastScreen, Component title, Component message) {
        super(title);

        this.lastScreen = lastScreen;
        this.message = message;
    }

    @SuppressWarnings("unused")
    public WarningScreen(Screen lastScreen, Component message) {
        super(Component.empty());

        this.lastScreen = lastScreen;
        this.message = message;
    }

    @Override
    protected void init() {
        super.init();

        this.layout.defaultCellSetting().alignHorizontallyCenter();

        if (!this.title.getString().isEmpty()) {
            final Component title = this.title.copy().withStyle(ChatFormatting.BOLD).withColor(0xFFAA00);
            this.layout.addChild(new StringWidget(title, this.font));
        }
        this.layout.addChild(
                new MultiLineTextWidget(this.message, this.font)
                        .setMaxWidth(this.width - 50)
                        .setMaxRows(15)
                        .setCentered(true)
        );

        this.addButtons();

        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    protected void addButtons() {
        this.layout.addChild(Button.builder(CommonComponents.GUI_PROCEED,
                        b -> Minecraft.getInstance().setScreen(this.lastScreen))
                .build());
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        FrameLayout.centerInRectangle(this.layout, this.getRectangle());
    }
}

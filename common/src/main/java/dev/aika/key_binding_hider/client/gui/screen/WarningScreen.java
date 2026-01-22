package dev.aika.key_binding_hider.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public abstract class WarningScreen extends Screen {
    protected final Screen parent;
    protected final Component message;
    protected LinearLayout layout = LinearLayout.vertical().spacing(4);

    protected WarningScreen(Screen parent, Component title, Component message) {
        super(title);

        this.parent = parent;
        this.message = message;
    }

    @Override
    protected void init() {
        super.init();

        this.layout.defaultCellSetting().alignHorizontallyCenter();
        this.layout.addChild(new StringWidget(this.title, this.font));
        this.layout.addChild(new MultiLineTextWidget(this.message, this.font).setMaxWidth(this.width - 50).setMaxRows(15).setCentered(true));
        LinearLayout linearLayout = this.layout.addChild(LinearLayout.horizontal().spacing(4));
        linearLayout.defaultCellSetting().paddingTop(16);

        this.addButtons();
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    protected void addButtons() {
        this.layout.addChild(Button.builder(CommonComponents.GUI_PROCEED,
                        b -> Minecraft.getInstance().setScreen(this.parent))
                .build());
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        FrameLayout.centerInRectangle(this.layout, this.getRectangle());
    }
}

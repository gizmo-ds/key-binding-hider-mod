package dev.aika.key_binding_hider.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;

public final class MissingClothConfigScreen extends WarningScreen {
    private final static String CLOTH_CONFIG_MODRINTH = "https://modrinth.com/mod/9s6osm5g";
    private final static String CLOTH_CONFIG_CURSEFORGE = "https://www.curseforge.com/minecraft/mc-mods/cloth-config";

    public MissingClothConfigScreen(Screen parent) {
        super(
                parent,
                Component.translatable("gui.key_binding_hider.missing_cloth_config.title"),
                Component.translatable("gui.key_binding_hider.missing_cloth_config.message")
        );
    }

    @Override
    protected void addButtons() {
        final LinearLayout buttonLayout = LinearLayout.horizontal().spacing(4);
        buttonLayout.addChild(Button.builder(
                        Component.translatable("gui.key_binding_hider.missing_cloth_config.curseforge_download"),
                        openLink(CLOTH_CONFIG_CURSEFORGE))
                .width(148).build());
        buttonLayout.addChild(Button.builder(
                        Component.translatable("gui.key_binding_hider.missing_cloth_config.modrinth_download"),
                        openLink(CLOTH_CONFIG_MODRINTH))
                .width(148).build());
        this.layout.addChild(buttonLayout);
        this.layout.addChild(Button.builder(
                        CommonComponents.GUI_BACK, b -> Minecraft.getInstance().setScreen(this.parent))
                .width(300).build());
    }

    private Button.OnPress openLink(String link) {
        return b -> minecraft.setScreen(new ConfirmLinkScreen((v) -> {
            if (v) Util.getPlatform().openUri(link);
            minecraft.setScreen(this);
        }, link, true));
    }
}

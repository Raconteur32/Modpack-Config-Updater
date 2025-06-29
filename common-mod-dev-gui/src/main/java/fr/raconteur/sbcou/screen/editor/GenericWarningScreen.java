package fr.raconteur.sbcou.screen.editor;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

import java.lang.Runnable;

public class GenericWarningScreen extends WarningScreen {
    private final Screen previousScreen;
    private final Runnable onProceed;

    public GenericWarningScreen(Screen previousScreen, Component title, Component warningMessage, Runnable onProceed) {
        super(
            title,
            warningMessage,
            null,
            Component.literal("Warning: " + warningMessage)
        );
        this.previousScreen = previousScreen;
        this.onProceed = onProceed;
    }

    @Override
    protected @NotNull LinearLayout addFooterButtons() {
        LinearLayout footerLayout = LinearLayout.horizontal().spacing(8);
        footerLayout.defaultCellSetting().alignHorizontallyCenter();

        // Proceed button
        footerLayout.addChild(Button.builder(Component.literal("Proceed"), (button) -> {
            onProceed.run();
            this.minecraft.setScreen(previousScreen); // Return to the previous screen
        }).width(100).build());

        // Cancel button
        footerLayout.addChild(Button.builder(Component.literal("Cancel"), (button) -> {
            this.minecraft.setScreen(previousScreen); // Return to the previous screen
        }).width(100).build());

        return footerLayout;
    }
}

package fr.raconteur.sbcou.screen.newversion;

import fr.raconteur.sbcou.OptionsActionsContext;
import fr.raconteur.sbcou.OptionsActionsUtils;
import fr.raconteur.sbcou.exceptions.OptionsActionsContextException;
import fr.raconteur.sbcou.exceptions.SbcouException;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import net.minecraft.client.gui.components.Tooltip;

public class NewVersionScreen extends Screen {
  private EditBox versionIdInput;
  @NotNull private final Screen parentScreen;
  private String errorMessage = null;

  public NewVersionScreen(@NotNull Screen parentScreen) {
    super(Component.literal("New Version"));
    this.parentScreen = parentScreen;
  }

  @Override
  protected void init() {
    int centerX = this.width / 2;
    int centerY = this.height / 2;

    this.versionIdInput = new EditBox(this.font, centerX - 100, centerY - 20, 200, 20, Component.literal("Version ID"));
    this.versionIdInput.setMaxLength(50);
    this.addRenderableWidget(this.versionIdInput);

    Button createVersionButton =
            this.addRenderableWidget(Button.builder(Component.literal("Create Version"), button -> {
              try {
                OptionsActionsContext.createVersion(this.versionIdInput.getValue());
                assert this.minecraft != null;
                this.minecraft.setScreen(parentScreen);
              } catch (SbcouException e) {
                errorMessage = e.getMessage();
              }
            }).bounds(centerX - 75, centerY + 10, 150, 20).build());

    createVersionButton.setTooltip(Tooltip.create(Component.literal("Créer une nouvelle version avec le contexte actuel")));

    setInitialFocus(this.versionIdInput);
  }

  @Override
  public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
    super.render(guiGraphics, mouseX, mouseY, partialTick);

    guiGraphics.drawString(this.font, "Version ID:", this.versionIdInput.getX() - 70, this.versionIdInput.getY() + 5, 0xFFFFFF);

    if (errorMessage != null) {
      guiGraphics.drawString(this.font, errorMessage, this.versionIdInput.getX(), this.versionIdInput.getY() + 25, 0xFF0000);
    }
  }
}

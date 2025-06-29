package fr.raconteur.sbcou.mixin;

import fr.raconteur.sbcou.screen.editor.SbcouScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
  protected TitleScreenMixin(Component pTitle) {
    super(pTitle);
  }

  @Inject(
          method = "init",
          at = @At(
                  value = "TAIL"
          ),
          locals = LocalCapture.CAPTURE_FAILHARD
  )
  public void init(CallbackInfo ci) {
    Button button1;
    button1 = Button.builder(Component.literal("Tst"),
                    ignored -> Minecraft.getInstance().setScreen(new SbcouScreen(this)))
            .bounds(0, 0, 16, 16)
            .build();
    this.addRenderableWidget(button1);
  }
}

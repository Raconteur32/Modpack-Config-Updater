package fr.raconteur.sbcou;

import fr.raconteur.sbcou.platform.Services;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ExampleMod {

    public ExampleMod() {

        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.

        // Use Forge to bootstrap the Common mod.
        Services.PLATFORM.logInfo("Hello Forge world!");
        CommonClass.init();

    }
}
package fr.raconteur.sbcou;

import fr.raconteur.sbcou.platform.Services;
import net.fabricmc.api.ModInitializer;

public class Sbcou implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Services.PLATFORM.logInfo("Hello Fabric world!");
        CommonClass.init();
    }
}

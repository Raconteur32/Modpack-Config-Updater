package fr.raconteur.sbcou;

import fr.raconteur.sbcou.exceptions.SbcouException;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class SbcouPreLaunch implements PreLaunchEntrypoint {
  @Override
  public void onPreLaunch() {
    System.out.println("Hello from SbcouPreLaunch!");
    try {
      CommonClass.preInit();
    } catch (SbcouException e) {
      throw new RuntimeException(e);
    }
  }
}

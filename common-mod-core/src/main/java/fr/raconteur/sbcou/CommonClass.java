package fr.raconteur.sbcou;

import fr.raconteur.sbcou.exceptions.SbcouException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonClass {
  private static final Logger LOGGER = LoggerFactory.getLogger(Constants.MOD_NAME);
  public static void init() {
  }

  public static void preInit() throws SbcouException {
    OptionsActionsContext.verifySbcouConfigDir();
    Version.verifyVersionsSpecsFiles();
    OptionsActionsContext.applyUpdates();
  }

  public static Logger getLogger() {
    return LOGGER;
  }
}

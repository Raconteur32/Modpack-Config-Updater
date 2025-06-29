package fr.raconteur.sbcou.platform;

import fr.raconteur.sbcou.CommonClass;
import fr.raconteur.sbcou.platform.services.IPlatformHelper;

import java.nio.file.Path;

public abstract class CommonPlatformHelper implements IPlatformHelper {
  @Override
  public void logInfo(String message) {
    CommonClass.getLogger().info(message);
  }

  @Override
  public void logWarning(String message) {
    CommonClass.getLogger().warn(message);
  }

  @Override
  public void logWarning(String message, Throwable throwable) {
    CommonClass.getLogger().warn(message, throwable);
  }

  @Override
  public void logError(String message) {
    CommonClass.getLogger().error(message);
  }

  @Override
  public void logError(String message, Throwable throwable) {
    CommonClass.getLogger().error(message, throwable);
  }

  @Override
  public void logDebug(String message) {
    CommonClass.getLogger().debug(message);
  }
}

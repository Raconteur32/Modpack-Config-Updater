package fr.raconteur.sbcou.platform;

import fr.raconteur.sbcou.platform.services.IPlatformHelper;

import java.nio.file.Path;

public class CmdPlatformHelper implements IPlatformHelper {

  private Path devInstancePath;

  @Override
  public String getPlatformName() {
    return "cli";
  }

  @Override
  public boolean isModLoaded(String modId) {
    return false;
  }

  @Override
  public boolean isDevelopmentEnvironment() {
    return true; // Always true for the command-line tool
  }

  @Override
  public Path getMinecraftDirectory() {
    if (devInstancePath != null) {
      return devInstancePath;
    }
    String envPath = System.getenv("DEV_INSTANCE_DIR");
    if (envPath != null && !envPath.isEmpty()) {
      return Path.of(envPath);
    }

    return Path.of(System.getProperty("user.dir"));
  }

  @Override
  public void logInfo(String message) {
    System.out.println(message);
  }

  @Override
  public void logWarning(String message) {
    System.err.println(message);
  }

  @SuppressWarnings("all")
  @Override
  public void logWarning(String message, Throwable throwable) {
    System.err.println(message);
    throwable.printStackTrace();
  }

  @Override
  public void logError(String message) {
    System.err.println(message);
  }

  @SuppressWarnings("all")
  @Override
  public void logError(String message, Throwable throwable) {
    System.err.println(message);
    throwable.printStackTrace();
  }

  @Override
  public void logDebug(String message) {
    System.out.println(message);
  }

  public void setDevInstancePath(Path devInstancePath) {
    this.devInstancePath = devInstancePath;
  }

  public Path getDevInstancePath() {
    return devInstancePath;
  }
}

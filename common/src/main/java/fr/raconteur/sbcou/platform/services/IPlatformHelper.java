package fr.raconteur.sbcou.platform.services;

import java.nio.file.Path;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Gets the path to the .minecraft directory.
     *
     * @return The path to the .minecraft directory.
     */
    Path getMinecraftDirectory();

    /**
     * Log an info log message to the platform's logging system.
     */
    void logInfo(String message);

    /**
     * Log a warning log message to the platform's logging system.
     */
    void logWarning(String message);

    /**
     * Log a warning log message to the platform's logging system.
     */
    void logWarning(String message, Throwable throwable);

    /**
     * Log an error log message to the platform's logging system.
     */
    void logError(String message);

    /**
     * Log an error log message to the platform's logging system.
     */
    void logError(String message, Throwable throwable);

    /**
     * Log a debug log message to the platform's logging system.
     */
    void logDebug(String message);
}
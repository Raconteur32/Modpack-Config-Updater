package fr.raconteur.sbcou.platform;

import fr.raconteur.sbcou.platform.services.IPlatformHelper;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Test implementation of IPlatformHelper for unit testing
 */
public class TestPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Test Platform";
    }

    @Override
    public boolean isModLoaded(String modId) {
        // For testing purposes, assume no mods are loaded except our own
        return "sbcou".equals(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return true;
    }

    @Override
    public String getMinecraftInstanceDirectory() {
        // Return a unique temporary directory within the repository for testing
        Path projectRoot = Paths.get(System.getProperty("user.dir"));
        Path tempDir = projectRoot.resolve("src/test/tmp/sbcou-test-" + System.currentTimeMillis());
        tempDir.toFile().mkdirs();
        return tempDir.toString();
    }
}
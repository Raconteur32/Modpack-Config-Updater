package fr.raconteur.sbcou;

import fr.raconteur.sbcou.db.versions.SbcouVersionsDataBase;
import fr.raconteur.sbcou.platform.Services;
import org.junit.jupiter.api.BeforeAll;

import java.sql.SQLException;

/**
 * Base test class that provides common setup for all SBCOU tests.
 * All test classes should extend this class to ensure proper database initialization.
 */
public abstract class BaseTest {

    @BeforeAll
    static void setUp() {
        // Initialize logging system for tests
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "WARN");
        
        // Show test directory locations
        String minecraftDir = Services.PLATFORM.getMinecraftInstanceDirectory();
        System.out.println("=== TEST SETUP ===");
        System.out.println("Test database at: " + minecraftDir + "/config/sbcou.db");
        System.out.println("==================");
        
        // Initialize the database before running tests
        try {
            new SbcouVersionsDataBase();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database for tests", e);
        }
    }
}
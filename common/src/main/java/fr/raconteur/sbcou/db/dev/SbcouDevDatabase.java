package fr.raconteur.sbcou.db.dev;

import fr.raconteur.sbcou.db.versions.DbDataValues;
import fr.raconteur.sbcou.db.versions.DbExtensionMimic;
import fr.raconteur.sbcou.db.versions.DbForcedEncoding;
import fr.raconteur.sbcou.db.versions.DbForcedExtension;
import fr.raconteur.sbcou.platform.Services;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static fr.raconteur.sbcou.Constants.MINECRAFT_DIR_SBCOU_DB_RELATIVE_PATH;

public class SbcouDevDatabase {
    private Connection connection;
    private static final Path DB_PATH = Path.of(Services.PLATFORM.getMinecraftInstanceDirectory(), MINECRAFT_DIR_SBCOU_DB_RELATIVE_PATH);
    private static SbcouDevDatabase latestInstance;

    /**
     * Create a database object and initialize it
     */
    public SbcouDevDatabase() throws SQLException {
        connect();
        init();
        latestInstance = this;
    }

    /**
     * Set up the connection to the database
     *
     * @throws SQLException SQL Exception
     */
    private void connect() throws SQLException {
        String url = "jdbc:sqlite:" + DB_PATH.normalize();
        Boolean ignored = (new File(DB_PATH.toString())).getParentFile().mkdirs();
        this.connection = DriverManager.getConnection(url);
    }

    /**
     * Init database if needed
     *
     * @throws SQLException SQL Exception
     */
    private void init() throws SQLException {
        DbDataValues.init(getConnection());
        DbExtensionMimic.init(getConnection());
        DbForcedEncoding.init(getConnection());
        DbForcedExtension.init(getConnection());
    }

    /**
     * Get the database connection
     *
     * @return The database connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Get the latest instance of SbcouDataBase
     *
     * @return The latest instance, or null if none exists
     */
    public static SbcouDevDatabase getLatestInstance() {
        return latestInstance;
    }
}

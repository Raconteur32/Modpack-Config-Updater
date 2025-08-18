package fr.raconteur.sbcou.db.versions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class DbForcedExtension {
    private final String filePath;
    private final String forcedExtension;
    private boolean validity = true;

    /**
     * Main constructor to create a DbForcedExtension instance
     *
     * @param filePath         The file path
     * @param forcedExtension  The forced extension
     */
    public DbForcedExtension(String filePath, String forcedExtension) {
        this.filePath = filePath;
        this.forcedExtension = forcedExtension;
    }

    /**
     * Getter for file path
     *
     * @return The file path
     */
    public String getFilePath() {
        verifyValidity();
        return filePath;
    }

    /**
     * Getter for forced extension
     *
     * @return The forced extension
     */
    public String getForcedExtension() {
        verifyValidity();
        return forcedExtension;
    }

    /**
     * Verifies the validity of the instance
     * Throws a RuntimeException if the instance has been deleted from the database
     */
    private void verifyValidity() {
        if (!validity) {
            throw new RuntimeException("This DbForcedExtension instance has been deleted from the database");
        }
    }

    /**
     * Retrieves a data row by its file path
     *
     * @param filePath The file path to retrieve
     * @return An Optional containing the row if found, otherwise Optional.empty()
     */
    public static Optional<DbForcedExtension> getFromDb(String filePath) {
        SbcouVersionsDataBase db = SbcouVersionsDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT file_path, forced_extension FROM forced_extension WHERE file_path = ?");
            
            stmt.setString(1, filePath);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(new DbForcedExtension(
                    rs.getString("file_path"),
                    rs.getString("forced_extension")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    /**
     * Creates a new row in the database
     *
     * @param filePath        The file path
     * @param forcedExtension The forced extension
     * @return An Optional containing the created row if successful, otherwise Optional.empty()
     */
    public static Optional<DbForcedExtension> create(String filePath, String forcedExtension) {
        SbcouVersionsDataBase db = SbcouVersionsDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement(
                "INSERT INTO forced_extension (file_path, forced_extension) VALUES (?, ?)");
            
            stmt.setString(1, filePath);
            stmt.setString(2, forcedExtension);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return Optional.of(new DbForcedExtension(filePath, forcedExtension));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    /**
     * Deletes this instance from the database
     *
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteInDb() {
        verifyValidity();
        
        SbcouVersionsDataBase db = SbcouVersionsDataBase.getLatestInstance();
        if (db == null) {
            return false;
        }
        
        PreparedStatement stmt = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("DELETE FROM forced_extension WHERE file_path = ?");
            
            stmt.setString(1, filePath);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                validity = false;
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Initializes the forced_extension table and its indexes
     *
     * @throws SQLException SQL Exception
     */
    public static void init(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            
            statement.execute(
                "CREATE TABLE IF NOT EXISTS forced_extension("
                    + "file_path TEXT PRIMARY KEY,"
                    + "forced_extension TEXT NOT NULL"
                    + ");");
            
            statement.execute("CREATE INDEX IF NOT EXISTS idx_forced_extension_file_path ON forced_extension(file_path);");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_forced_extension_forced_extension ON forced_extension(forced_extension);");
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Get the forced extension for a given file path
     *
     * @param filePath The file path to look up
     * @return An Optional containing the forced extension if found, otherwise Optional.empty()
     */
    public static Optional<String> getForcedExtension(String filePath) {
        SbcouVersionsDataBase db = SbcouVersionsDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT forced_extension FROM forced_extension WHERE file_path = ?");
            
            stmt.setString(1, filePath);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(rs.getString("forced_extension"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
} 
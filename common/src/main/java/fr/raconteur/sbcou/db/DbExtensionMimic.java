package fr.raconteur.sbcou.db;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class DbExtensionMimic {
    private final String fileExtension;
    private final String targetExtension;
    private boolean validity = true;

    /**
     * Main constructor to create a DbExtensionMimic instance
     *
     * @param fileExtension   The file extension (must start with a dot)
     * @param targetExtension The target extension (must start with a dot)
     */
    public DbExtensionMimic(String fileExtension, String targetExtension) {
        this.fileExtension = fileExtension;
        this.targetExtension = targetExtension;
    }

    /**
     * Getter for file extension
     *
     * @return The file extension
     */
    public String getFileExtension() {
        verifyValidity();
        return fileExtension;
    }

    /**
     * Getter for target extension
     *
     * @return The target extension
     */
    public String getTargetExtension() {
        verifyValidity();
        return targetExtension;
    }

    /**
     * Verifies the validity of the instance
     * Throws a RuntimeException if the instance has been deleted from the database
     */
    private void verifyValidity() {
        if (!validity) {
            throw new RuntimeException("This DbExtensionMimic instance has been deleted from the database");
        }
    }

    /**
     * Retrieves a data row by its file extension
     *
     * @param fileExtension The file extension to retrieve
     * @return An Optional containing the row if found, otherwise Optional.empty()
     */
    public static Optional<DbExtensionMimic> getFromDb(String fileExtension) {
        SbcouDataBase db = SbcouDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT file_extension, target_extension FROM extension_mimic WHERE file_extension = ?");
            
            stmt.setString(1, fileExtension);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(new DbExtensionMimic(
                    rs.getString("file_extension"),
                    rs.getString("target_extension")
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
     * @param fileExtension   The file extension
     * @param targetExtension The target extension
     * @return An Optional containing the created row if successful, otherwise Optional.empty()
     */
    public static Optional<DbExtensionMimic> create(String fileExtension, String targetExtension) {
        // Verify that extensions start with a dot
        if (!fileExtension.startsWith(".")) {
            throw new IllegalArgumentException("File extension must start with a dot: " + fileExtension);
        }
        if (!targetExtension.startsWith(".")) {
            throw new IllegalArgumentException("Target extension must start with a dot: " + targetExtension);
        }

        SbcouDataBase db = SbcouDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement(
                "INSERT INTO extension_mimic (file_extension, target_extension) VALUES (?, ?)");
            
            stmt.setString(1, fileExtension);
            stmt.setString(2, targetExtension);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return Optional.of(new DbExtensionMimic(fileExtension, targetExtension));
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
        
        SbcouDataBase db = SbcouDataBase.getLatestInstance();
        if (db == null) {
            return false;
        }
        
        PreparedStatement stmt = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("DELETE FROM extension_mimic WHERE file_extension = ?");
            
            stmt.setString(1, fileExtension);
            
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
     * Initializes the extension_mimic table and its indexes
     *
     * @throws SQLException SQL Exception
     */
    public static void init(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            
            statement.execute(
                "CREATE TABLE IF NOT EXISTS extension_mimic("
                    + "file_extension TEXT PRIMARY KEY CHECK (file_extension LIKE '.%'),"
                    + "target_extension TEXT NOT NULL CHECK (target_extension LIKE '.%')"
                    + ");");
            
            statement.execute("CREATE INDEX IF NOT EXISTS idx_extension_mimic_file_extension ON extension_mimic(file_extension);");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_extension_mimic_target_extension ON extension_mimic(target_extension);");
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Get the target extension of a presumed mimic
     *
     * @param fileExtension The file extension to look up
     * @return An Optional containing the target extension if found, otherwise Optional.empty()
     */
    public static Optional<String> getMimicTarget(String fileExtension) {
        SbcouDataBase db = SbcouDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT target_extension FROM extension_mimic WHERE file_extension = ?");
            
            stmt.setString(1, fileExtension);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(rs.getString("target_extension"));
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
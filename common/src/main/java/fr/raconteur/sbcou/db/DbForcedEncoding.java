package fr.raconteur.sbcou.db;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class DbForcedEncoding {
    private final String filePath;
    private final String forcedEncoding;
    private boolean validity = true;

    /**
     * Main constructor to create a DbForcedEncoding instance
     *
     * @param filePath       The file path
     * @param forcedEncoding The forced encoding
     */
    public DbForcedEncoding(String filePath, String forcedEncoding) {
        this.filePath = filePath;
        this.forcedEncoding = forcedEncoding;
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
     * Getter for forced encoding
     *
     * @return The forced encoding
     */
    public String getForcedEncoding() {
        verifyValidity();
        return forcedEncoding;
    }

    /**
     * Verifies the validity of the instance
     * Throws a RuntimeException if the instance has been deleted from the database
     */
    private void verifyValidity() {
        if (!validity) {
            throw new RuntimeException("This DbForcedEncoding instance has been deleted from the database");
        }
    }

    /**
     * Retrieves a data row by its file path
     *
     * @param filePath The file path to retrieve
     * @return An Optional containing the row if found, otherwise Optional.empty()
     */
    public static Optional<DbForcedEncoding> getFromDb(String filePath) {
        SbcouDataBase db = SbcouDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT file_path, forced_encoding FROM forced_encoding WHERE file_path = ?");
            
            stmt.setString(1, filePath);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(new DbForcedEncoding(
                    rs.getString("file_path"),
                    rs.getString("forced_encoding")
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
     * @param filePath      The file path
     * @param forcedEncoding The forced encoding
     * @return An Optional containing the created row if successful, otherwise Optional.empty()
     */
    public static Optional<DbForcedEncoding> create(String filePath, String forcedEncoding) {
        SbcouDataBase db = SbcouDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement(
                "INSERT INTO forced_encoding (file_path, forced_encoding) VALUES (?, ?)");
            
            stmt.setString(1, filePath);
            stmt.setString(2, forcedEncoding);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return Optional.of(new DbForcedEncoding(filePath, forcedEncoding));
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
            stmt = connection.prepareStatement("DELETE FROM forced_encoding WHERE file_path = ?");
            
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
     * Initializes the forced_encoding table and its indexes
     *
     * @throws SQLException SQL Exception
     */
    public static void init(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            
            statement.execute(
                "CREATE TABLE IF NOT EXISTS forced_encoding("
                    + "file_path TEXT PRIMARY KEY,"
                    + "forced_encoding TEXT NOT NULL"
                    + ");");
            
            statement.execute("CREATE INDEX IF NOT EXISTS idx_forced_encoding_file_path ON forced_encoding(file_path);");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_forced_encoding_forced_encoding ON forced_encoding(forced_encoding);");
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Get the forced encoding for a given file path
     *
     * @param filePath The file path to look up
     * @return An Optional containing the forced encoding if found, otherwise Optional.empty()
     */
    public static Optional<String> getForcedEncoding(String filePath) {
        SbcouDataBase db = SbcouDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT forced_encoding FROM forced_encoding WHERE file_path = ?");
            
            stmt.setString(1, filePath);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(rs.getString("forced_encoding"));
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
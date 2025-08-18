package fr.raconteur.sbcou.db.versions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbDataModification {
    public enum ModificationActionChoice {
        INCLUDE_AS_DEFAULT,
        OVERRIDE
    }

    private final int id;
    private final String flatKey;
    private final int dataValueId;
    private final ModificationActionChoice action;
    private boolean validity = true;

    /**
     * Main constructor to create a DbDataModification instance
     *
     * @param id          The unique identifier of the row
     * @param flatKey     The flat key as text
     * @param dataValueId The reference to data_values table
     * @param action      The modification action
     */
    public DbDataModification(int id, String flatKey, int dataValueId, ModificationActionChoice action) {
        this.id = id;
        this.flatKey = flatKey;
        this.dataValueId = dataValueId;
        this.action = action;
    }


    /**
     * Getter for the identifier
     *
     * @return The row identifier
     */
    public int getId() {
        verifyValidity();
        return id;
    }

    /**
     * Getter for the flat key
     *
     * @return The flat key
     */
    public String getFlatKey() {
        verifyValidity();
        return flatKey;
    }

    /**
     * Getter for the data value ID
     *
     * @return The data value ID
     */
    public int getDataValueId() {
        verifyValidity();
        return dataValueId;
    }

    /**
     * Getter for the action
     *
     * @return The modification action
     */
    public ModificationActionChoice getAction() {
        verifyValidity();
        return action;
    }

    /**
     * Verifies the validity of the instance
     * Throws a RuntimeException if the instance has been deleted from the database
     */
    private void verifyValidity() {
        if (!validity) {
            throw new RuntimeException("This DbDataModification instance has been deleted from the database");
        }
    }

    public static Optional<DbDataModification> getFromDb(int id) {
        SbcouVersionsDataBase db = SbcouVersionsDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT id, flat_key, data_value_id, action FROM data_modification WHERE id = ?");
            
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(new DbDataModification(
                    rs.getInt("id"),
                    rs.getString("flat_key"),
                    rs.getInt("data_value_id"),
                    ModificationActionChoice.valueOf(rs.getString("action"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
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

    public static Optional<DbDataModification> getFromDb(String flatKey, int dataValueId, ModificationActionChoice action) {
        SbcouVersionsDataBase db = SbcouVersionsDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT id, flat_key, data_value_id, action FROM data_modification WHERE flat_key = ? AND data_value_id = ? AND action = ?");
            
            stmt.setString(1, flatKey);
            stmt.setInt(2, dataValueId);
            stmt.setString(3, action.name());
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(new DbDataModification(
                    rs.getInt("id"),
                    rs.getString("flat_key"),
                    rs.getInt("data_value_id"),
                    ModificationActionChoice.valueOf(rs.getString("action"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
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

    public static List<DbDataModification> getAllFromDb() {
        SbcouVersionsDataBase db = SbcouVersionsDataBase.getLatestInstance();
        if (db == null) {
            return new ArrayList<>();
        }
        
        List<DbDataModification> modifications = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT id, flat_key, data_value_id, action FROM data_modification ORDER BY id");
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                modifications.add(new DbDataModification(
                    rs.getInt("id"),
                    rs.getString("flat_key"),
                    rs.getInt("data_value_id"),
                    ModificationActionChoice.valueOf(rs.getString("action"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return modifications;
    }

    public static DbDataModification get(String flatKey, int dataValueId, ModificationActionChoice action) {
        Optional<DbDataModification> existing = getFromDb(flatKey, dataValueId, action);
        
        if (existing.isPresent()) {
            return existing.get();
        }
        
        Optional<DbDataModification> created = create(flatKey, dataValueId, action);
        if (created.isPresent()) {
            return created.get();
        }
        
        throw new RuntimeException("Failed to get or create DbDataModification");
    }

    public static Optional<DbDataModification> create(String flatKey, int dataValueId, ModificationActionChoice action) {
        SbcouVersionsDataBase db = SbcouVersionsDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement(
                "INSERT INTO data_modification (flat_key, data_value_id, action) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, flatKey);
            stmt.setInt(2, dataValueId);
            stmt.setString(3, action.name());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return Optional.of(new DbDataModification(id, flatKey, dataValueId, action));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public boolean deleteInDb() {
        verifyValidity();
        
        SbcouVersionsDataBase db = SbcouVersionsDataBase.getLatestInstance();
        if (db == null) {
            return false;
        }
        
        PreparedStatement stmt = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("DELETE FROM data_modification WHERE id = ?");
            
            stmt.setInt(1, id);
            
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

    public static void init(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            // Temporarily drop the table for rewrites
            statement.execute("DROP TABLE IF EXISTS data_modification;");
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
}
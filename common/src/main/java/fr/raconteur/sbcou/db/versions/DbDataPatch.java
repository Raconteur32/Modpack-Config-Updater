package fr.raconteur.sbcou.db.versions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DbDataPatch {
    private final int id;
    private final String name;
    private final String modificationList;
    private boolean validity = true;

    /**
     * Main constructor to create a DbDataPatch instance
     *
     * @param id               The unique identifier of the row
     * @param name             The name of the patch
     * @param modificationList The comma-separated list of modification IDs
     */
    public DbDataPatch(int id, String name, String modificationList) {
        this.id = id;
        this.name = name;
        this.modificationList = modificationList;
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
     * Getter for the name
     *
     * @return The patch name
     */
    public String getName() {
        verifyValidity();
        return name;
    }

    /**
     * Getter for the modification list
     *
     * @return The comma-separated modification list
     */
    public String getModificationList() {
        verifyValidity();
        return modificationList;
    }

    /**
     * Converts the modification list string to a list of modification IDs
     *
     * @return List of modification IDs
     */
    public List<Integer> getModificationIds() {
        verifyValidity();
        if (modificationList == null || modificationList.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(modificationList.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * Gets the actual DbDataModification instances for this patch
     *
     * @return List of DbDataModification instances
     */
    public List<DbDataModification> getModifications() {
        verifyValidity();
        List<DbDataModification> modifications = new ArrayList<>();
        for (Integer id : getModificationIds()) {
            Optional<DbDataModification> modification = DbDataModification.getFromDb(id);
            modification.ifPresent(modifications::add);
        }
        return modifications;
    }

    /**
     * Verifies the validity of the instance
     * Throws a RuntimeException if the instance has been deleted from the database
     */
    private void verifyValidity() {
        if (!validity) {
            throw new RuntimeException("This DbDataPatch instance has been deleted from the database");
        }
    }

    /**
     * Retrieves a data patch by its identifier
     *
     * @param id The identifier of the row to retrieve
     * @return An Optional containing the row if found, otherwise Optional.empty()
     */
    public static Optional<DbDataPatch> getFromDb(int id) {
        SbcouVersionsDataBase db = SbcouVersionsDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT id, name, modification_list FROM data_patch WHERE id = ?");
            
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(new DbDataPatch(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("modification_list")
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

    /**
     * Gets or creates a data patch entry by ID
     * If a patch with the given ID exists, returns it.
     * Otherwise, returns empty Optional.
     *
     * @param id The ID of the patch to retrieve
     * @return The DbDataPatch instance if found, otherwise empty Optional
     */
    public static Optional<DbDataPatch> get(int id) {
        return getFromDb(id);
    }

    /**
     * Retrieves all data patches
     *
     * @return A list of all data patches
     */
    public static List<DbDataPatch> getAllFromDb() {
        SbcouVersionsDataBase db = SbcouVersionsDataBase.getLatestInstance();
        if (db == null) {
            return new ArrayList<>();
        }
        
        List<DbDataPatch> patches = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT id, name, modification_list FROM data_patch ORDER BY id");
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                patches.add(new DbDataPatch(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("modification_list")
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
        return patches;
    }

    /**
     * Creates a new row in the database
     *
     * @param name             The name of the patch
     * @param modificationList The comma-separated list of modification IDs
     * @return An Optional containing the created row if successful, otherwise Optional.empty()
     */
    public static Optional<DbDataPatch> create(String name, String modificationList) {
        SbcouVersionsDataBase db = SbcouVersionsDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement(
                "INSERT INTO data_patch (name, modification_list) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, name);
            stmt.setString(2, modificationList);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return Optional.of(new DbDataPatch(id, name, modificationList));
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

    /**
     * Creates a data patch from a list of DbDataModification instances
     *
     * @param name          The name of the patch
     * @param modifications The list of modifications
     * @return An Optional containing the created row if successful, otherwise Optional.empty()
     */
    public static Optional<DbDataPatch> create(String name, List<DbDataModification> modifications) {
        String modificationList = modifications.stream()
                .map(mod -> String.valueOf(mod.getId()))
                .collect(Collectors.joining(","));
        
        return create(name, modificationList);
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
            stmt = connection.prepareStatement("DELETE FROM data_patch WHERE id = ?");
            
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


    /**
     * Initializes the data_patch table and its indexes
     *
     * @throws SQLException SQL Exception
     */
    public static void init(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            
            statement.execute(
                "CREATE TABLE IF NOT EXISTS data_patch("
                    + "id INTEGER NOT NULL PRIMARY KEY,"
                    + "name TEXT NOT NULL,"
                    + "modification_list TEXT NOT NULL"
                    + ");");
            
            statement.execute("CREATE INDEX IF NOT EXISTS idx_data_patch_id ON data_patch(id);");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_data_patch_name ON data_patch(name);");
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
}
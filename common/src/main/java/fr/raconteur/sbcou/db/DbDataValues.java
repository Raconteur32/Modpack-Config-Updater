package fr.raconteur.sbcou.db;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Optional;
import java.util.stream.Collectors;

import static fr.raconteur.sbcou.Constants.SBCOU_DATA_TYPES;

public class DbDataValues {
    public static final java.nio.charset.Charset stringValuesCharset = java.nio.charset.StandardCharsets.UTF_16;
    
    private final int id;
    private final String dataType;
    private final byte[] value;

    /**
     * Main constructor to create a DbDataValues instance
     *
     * @param id        The unique identifier of the row
     * @param dataType  The data type
     * @param value     The data value as bytes
     */
    public DbDataValues(int id, String dataType, byte[] value) {
        this.id = id;
        this.dataType = dataType;
        this.value = value;
    }

    /**
     * Getter for the identifier
     *
     * @return The row identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the data type
     *
     * @return The data type
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Getter for the value as bytes
     *
     * @return The data value as bytes
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * Getter for the value as string using UTF-16 encoding
     *
     * @return The data value as string
     */
    public String getValueAsString() {
        return new String(value, stringValuesCharset);
    }

    /**
     * Converts a string value to bytes using the standard charset
     *
     * @param value The string value to convert
     * @return The bytes representation
     */
    public static byte[] getStringValueAsBytes(String value) {
        return value.getBytes(stringValuesCharset);
    }

    /**
     * Retrieves a data row by its identifier
     *
     * @param id The identifier of the row to retrieve
     * @return An Optional containing the row if found, otherwise Optional.empty()
     */
    public static Optional<DbDataValues> getFromDb(int id) {
        SbcouDataBase db = SbcouDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT id, data_type, value FROM data_values WHERE id = ?");
            
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                byte[] valueBytes = rs.getBytes("value");
                
                return Optional.of(new DbDataValues(
                    rs.getInt("id"),
                    rs.getString("data_type"),
                    valueBytes
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
     * Retrieves a data row by its type and value
     *
     * @param dataType The data type
     * @param value    The data value as bytes
     * @return An Optional containing the row if found, otherwise Optional.empty()
     */
    private static Optional<DbDataValues> getFromDb(String dataType, byte[] value) {
        SbcouDataBase db = SbcouDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement("SELECT id, data_type, value FROM data_values WHERE data_type = ? AND value = ?");
            
            stmt.setString(1, dataType);
            stmt.setBytes(2, value);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                byte[] valueBytes = rs.getBytes("value");
                
                return Optional.of(new DbDataValues(
                    rs.getInt("id"),
                    rs.getString("data_type"),
                    valueBytes
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
     * Retrieves a data row by its type and value, or creates it if it doesn't exist
     *
     * @param dataType The data type
     * @param value    The data value as bytes
     * @return The row (existing or newly created)
     * @throws RuntimeException if creation fails
     */
    public static DbDataValues get(String dataType, byte[] value) {
        // First, try to retrieve an existing value
        Optional<DbDataValues> existing = getFromDb(dataType, value);
        
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // If it doesn't exist, create it
        Optional<DbDataValues> created = createInDb(dataType, value);
        if (created.isPresent()) {
            return created.get();
        }
        
        // If creation fails, throw an exception
        throw new RuntimeException("Failed to create DbDataValues for dataType: " + dataType);
    }

    /**
     * Initializes the data_values table and its indexes
     *
     * @throws SQLException SQL Exception
     */
    public static void init(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            
            String dataTypesList = SBCOU_DATA_TYPES.stream()
                .map(s -> String.format("'%s'", s))
                .collect(Collectors.joining(", "));
            
            statement.execute(
                String.format(
                    "CREATE TABLE IF NOT EXISTS data_values("
                        + "id INTEGER NOT NULL PRIMARY KEY,"
                        + "data_type TEXT CHECK (data_type IN %s) NOT NULL,"
                        + "value BLOB NOT NULL"
                        + ");"
                    , "(" + dataTypesList + ")"));
            
            statement.execute("CREATE INDEX IF NOT EXISTS idx_data_values_id ON data_values(id);");
            statement.execute("CREATE INDEX IF NOT EXISTS idx_data_values_type_value ON data_values(data_type, value);");
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Creates a new row in the database
     *
     * @param dataType The data type
     * @param value    The data value as bytes
     * @return An Optional containing the created row if successful, otherwise Optional.empty()
     */
    private static Optional<DbDataValues> createInDb(String dataType, byte[] value) {
        SbcouDataBase db = SbcouDataBase.getLatestInstance();
        if (db == null) {
            return Optional.empty();
        }
        
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        try {
            Connection connection = db.getConnection();
            stmt = connection.prepareStatement(
                "INSERT INTO data_values (data_type, value) VALUES (?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, dataType);
            stmt.setBytes(2, value);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return Optional.of(new DbDataValues(id, dataType, value));
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
} 
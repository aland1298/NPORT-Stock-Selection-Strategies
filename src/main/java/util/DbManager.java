package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Establishes functionality to connect to database.
 */
public class DbManager {
    private static final HikariDataSource dataSource;
    private static final Logger logger = Logger.getLogger(DbManager.class.getName());

    static {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("db");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(resourceBundle.getString("db.URI"));
        config.setUsername(resourceBundle.getString("db.USERNAME"));
        config.setPassword(resourceBundle.getString("db.PASSWORD"));
        dataSource = new HikariDataSource(config);

        try {
            FileHandler fileHandler = new FileHandler(DbManager.class.getName());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DbManager() {
    }

    public static void insertFilingWithAccessionNumbers(Map<Integer, List<String>> cikAccessionMap) throws SQLException {
        String updateFiling = "insert into filing(accession_number, form_type_id, cik) values (?, ?, ?)";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(updateFiling)
        ) {
            connection.setAutoCommit(false);

            for (Map.Entry<Integer, List<String>> entry : cikAccessionMap.entrySet()) {
                int cik = entry.getKey();
                List<String> accessionNumbers = entry.getValue();
                for (String accessionNumber : accessionNumbers) {
                    try {
                        statement.setString(1, accessionNumber);
                        statement.setInt(2, EnumTypeUtils.FormType.NPORT_P.getTypeId());
                        statement.setInt(3, cik);
                        statement.addBatch();
                    } catch (SQLException e) {
                        // Log the failed CIK and accession number
                        throw new SQLException("Failed to set parameters for CIK: " + cik + ", Accession: " + accessionNumber);
                    }
                }
            }

            int[] updateCounts = statement.executeBatch();

            for (int updateCount : updateCounts) {
                if (updateCount != 1) {
                    // Failed to process batch
                    connection.rollback();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Retrieves a list of distinct CIK (Central Index Key) numbers from the "investment_company" table in the database
     * with optional limits and offsets for pagination.
     *
     * @param limit  The maximum number of records to retrieve.
     * @param offset The number of records to skip before retrieving the data (useful for pagination).
     * @return A list of distinct CIK numbers based on the specified limit and offset, or null if fails.
     */
    public static List<Integer> getCikNumbers(int limit, int offset) {

        String sql = "select distinct cik from investment_company limit ? offset ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, limit);
            statement.setInt(2, offset);

            List<Integer> cikArr = new LinkedList<>();

            ResultSet cikResultSet = statement.executeQuery();
            while (cikResultSet.next()) {
                cikArr.add(cikResultSet.getInt(1));
            }

            return cikArr;
        } catch (SQLException e) {
            return null;
        }
    }

    public static List<Integer> getCikNumbersWithoutFiling(int limit, int offset, EnumTypeUtils.FormType formType) {

        String sql = "select distinct cik from investment_company limit ? offset ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, limit);
            statement.setInt(2, offset);

            List<Integer> cikArr = new LinkedList<>();

            ResultSet cikResultSet = statement.executeQuery();
            while (cikResultSet.next()) {
                cikArr.add(cikResultSet.getInt(1));
            }

            return cikArr;
        } catch (SQLException e) {
            return null;
        }
    }
}


package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import entities.filings.NPORT_P;
import entities.filings.nport_p.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;
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
            FileHandler fileHandler = new FileHandler("src\\main\\java\\logs\\" + DbManager.class.getName());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DbManager() {}

    public static void insertNPORTFilings(Map<Integer, List<String>> cikAccessionMap) throws SQLException {
        String insertNPORT_P = "insert into nport_p(accession_number) values (?);";

        String insertGenInfo = "insert into gen_info(accession_number, series_name, series_id, fiscal_pd_end, reported, is_final_filing) VALUES (?, ?, ?, ?, ?, ?);";

        String insertFundInfo = "insert into fund_info(accession_number, total_assets, net_assets) values (?, ?, ?);";

        String insertPortfolioInfo = "insert into portfolio_info(accession_number, cusip, fair_value_level, issuer_name, issue_name, value_usd, percent_of_nav, trade_type, asset_category, issuer_category, lei, investorCountry) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement prepareInsertNPORT_PStatement = connection.prepareStatement(insertNPORT_P);
                PreparedStatement prepareInsertGenInfo_Statement = connection.prepareStatement(insertGenInfo);
                PreparedStatement prepareInsertFundInfoStatement = connection.prepareStatement(insertFundInfo);
                PreparedStatement prepareInsertPortfolioInfoStatement = connection.prepareStatement(insertPortfolioInfo)
                ) {
            connection.setAutoCommit(false);

            for (Map.Entry<Integer, List<String>> entry: cikAccessionMap.entrySet()) {
                int cik = entry.getKey();
                List<String> accessionNumbers = entry.getValue();

                for (String accessionNumber: accessionNumbers) {
                    String formattedAccessionNumber = accessionNumber.replace("-", "");

                    // Query Edgar Manager to get NPORT_P filing data
                    NPORT_P nportP;
                    if ((nportP = EdgarManager.getNPORT_P(cik, formattedAccessionNumber)) != null) {
                        try {
                            // nport_p
                            prepareInsertNPORT_PStatement.setString(1, accessionNumber);
                            prepareInsertNPORT_PStatement.executeUpdate();

                            // gen_info(accession_number, series_name, series_id, fiscal_pd_end, reported, is_final_filing)
                            prepareInsertGenInfo_Statement.setString(1, accessionNumber);
                            prepareInsertGenInfo_Statement.setString(2, nportP.getFormData().getGenInfo().getSeriesName());
                            prepareInsertGenInfo_Statement.setString(3, nportP.getFormData().getGenInfo().getSeriesId());
                            prepareInsertGenInfo_Statement.setDate(4, nportP.getFormData().getGenInfo().getFiscalPdEnd());
                            prepareInsertGenInfo_Statement.setDate(5, nportP.getFormData().getGenInfo().getReported());
                            prepareInsertGenInfo_Statement.setString(6, nportP.getFormData().getGenInfo().getIsFinalFiling());
                            prepareInsertGenInfo_Statement.executeUpdate();

                            // fund_info(accession_number, total_assets, net_assets)
                            prepareInsertFundInfoStatement.setString(1, accessionNumber);
                            prepareInsertFundInfoStatement.setDouble(2, nportP.getFormData().getFundInfo().getTotAssets());
                            prepareInsertFundInfoStatement.setDouble(3, nportP.getFormData().getFundInfo().getNetAssets());
                            prepareInsertFundInfoStatement.executeUpdate();

                            for(Security security: nportP.getFormData().getInvstPortfolio()) {
                                // portfolio_info(accession_number, cusip, fair_value_level, issuer_name, issue_name, value_usd, percent_of_nav, trade_type, asset_category, issuer_category, lei)
                                prepareInsertPortfolioInfoStatement.setString(1, accessionNumber);
                                prepareInsertPortfolioInfoStatement.setString(2, security.getCusip());
                                prepareInsertPortfolioInfoStatement.setString(3, security.getFairValLevel());
                                prepareInsertPortfolioInfoStatement.setString(4, security.getIsserName());
                                prepareInsertPortfolioInfoStatement.setString(5, security.getIssueName());
                                prepareInsertPortfolioInfoStatement.setDouble(6, security.getValUSD());
                                prepareInsertPortfolioInfoStatement.setDouble(7, security.getPercentOfNAV());
                                prepareInsertPortfolioInfoStatement.setString(8, security.getTradeType());
                                prepareInsertPortfolioInfoStatement.setString(9, security.getAssetCat());
                                prepareInsertPortfolioInfoStatement.setString(10, security.getIssuerCat());
                                prepareInsertPortfolioInfoStatement.setString(11, security.getLei());
                                prepareInsertPortfolioInfoStatement.setString(12, security.getInvestorCountry());
                                prepareInsertPortfolioInfoStatement.addBatch();
                            }
                            prepareInsertPortfolioInfoStatement.executeBatch();

                            insert3MonthInfo(connection, nportP.getFormData().getFundInfo(), accessionNumber);
                            insert3MonthClassInfo(connection, accessionNumber, nportP.getFormData().getFundInfo().getReturnInfo().getMonthlyTotReturns());
                            insert3MonthCategoryInfo(connection, accessionNumber, nportP.getFormData().getFundInfo().getReturnInfo().getMonthlyReturnCats());
                            insert3MonthDerivativeInfo(connection, accessionNumber, nportP.getFormData().getFundInfo().getReturnInfo().getMonthlyReturnCats());

                        } catch (SQLException e) {
                            // Log the failed CIK and accession number, and rollback any updates
                            connection.rollback();
                            throw new SQLException("Failed to set parameters for CIK: " + cik + ", Accession: " + accessionNumber);
                        }
                    }
                }
            }

            //connection.commit();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    /**
     * Inserts filing records into the database with corresponding accession numbers,
     * form type IDs, and CIK numbers. This method prepares and executes batched SQL
     * insert statements for multiple records.
     *
     * @param cikAccessionMap A map containing CIK numbers as keys and lists of
     *                       accession numbers as values.
     * @throws SQLException If an SQL exception occurs during database operations,
     *                      such as connection, statement preparation, or batch execution.
     */
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

    /**
     * Retrieves a list of CIK (Central Index Key) numbers of investment companies that do not have
     * associated filings of a specified Form Type. The CIK numbers are selected from the 'investment_company'
     * table where there is no corresponding entry in the 'filing' table with the given Form Type ID.
     *
     * @param limit     The maximum number of CIK numbers to retrieve.
     * @param offset    The starting position for retrieving CIK numbers (used for pagination).
     * @param formType  The Form Type to check for missing filings.
     * @return A list of CIK numbers that do not have associated filings of the specified Form Type.
     *         If there is an SQL exception during the query, null is returned.
     */
    public static List<Integer> getCikNumbersWithoutFiling(int limit, int offset, EnumTypeUtils.FormType formType) {

        String sql = "SELECT DISTINCT ic.cik\n" +
                "FROM investment_company ic\n" +
                "LEFT JOIN filing f ON ic.cik = f.cik AND f.form_type_id = " + formType.getTypeId() + "\n" +
                "WHERE f.cik IS NULL\n" +
                "ORDER BY ic.cik\n" +
                "LIMIT ? OFFSET ?";

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

    /**
     * Inserts 3-month derivative information for various category types based on the provided MonthlyReturnCats.
     *
     * @param connection        The database connection.
     * @param accessionNumber   The accession number for the report.
     * @param monthlyReturnCats The MonthlyReturnCats data containing derivative information for different category types.
     * @throws SQLException if a database access error occurs or a SQL exception is thrown.
     */
    private static void insert3MonthDerivativeInfo(Connection connection, String accessionNumber, MonthlyReturnCats monthlyReturnCats) throws SQLException {
        // For each category type, insert 3-month derivative information
        insert3MonthDerivativeInfoHelper(
                connection,
                accessionNumber,
                EnumTypeUtils.CategoryType.COMMODITY,
                new TreeMap<>() {{{
                    put(EnumTypeUtils.DerivativeType.FORWARD, monthlyReturnCats.getCommodityContracts().getForward());
                    put(EnumTypeUtils.DerivativeType.FUTURE, monthlyReturnCats.getCommodityContracts().getFuture());
                    put(EnumTypeUtils.DerivativeType.OPTION, monthlyReturnCats.getCommodityContracts().getOption());
                    put(EnumTypeUtils.DerivativeType.OTHER, monthlyReturnCats.getCommodityContracts().getOther());
                    put(EnumTypeUtils.DerivativeType.SWAP, monthlyReturnCats.getCommodityContracts().getSwap());
                    put(EnumTypeUtils.DerivativeType.SWAPTION, monthlyReturnCats.getCommodityContracts().getSwaption());
                    put(EnumTypeUtils.DerivativeType.WARRANT, monthlyReturnCats.getCommodityContracts().getWarrent());
                }}}
        );
        insert3MonthDerivativeInfoHelper(
                connection,
                accessionNumber,
                EnumTypeUtils.CategoryType.CREDIT,
                new TreeMap<>() {{{
                    put(EnumTypeUtils.DerivativeType.FORWARD, monthlyReturnCats.getCreditContracts().getForward());
                    put(EnumTypeUtils.DerivativeType.FUTURE, monthlyReturnCats.getCreditContracts().getFuture());
                    put(EnumTypeUtils.DerivativeType.OPTION, monthlyReturnCats.getCreditContracts().getOption());
                    put(EnumTypeUtils.DerivativeType.OTHER, monthlyReturnCats.getCreditContracts().getOther());
                    put(EnumTypeUtils.DerivativeType.SWAP, monthlyReturnCats.getCreditContracts().getSwap());
                    put(EnumTypeUtils.DerivativeType.SWAPTION, monthlyReturnCats.getCreditContracts().getSwaption());
                    put(EnumTypeUtils.DerivativeType.WARRANT, monthlyReturnCats.getCreditContracts().getWarrent());
                }}}
        );
        insert3MonthDerivativeInfoHelper(
                connection,
                accessionNumber,
                EnumTypeUtils.CategoryType.EQUITY,
                new TreeMap<>() {{{
                    put(EnumTypeUtils.DerivativeType.FORWARD, monthlyReturnCats.getEquityContracts().getForward());
                    put(EnumTypeUtils.DerivativeType.FUTURE, monthlyReturnCats.getEquityContracts().getFuture());
                    put(EnumTypeUtils.DerivativeType.OPTION, monthlyReturnCats.getEquityContracts().getOption());
                    put(EnumTypeUtils.DerivativeType.OTHER, monthlyReturnCats.getEquityContracts().getOther());
                    put(EnumTypeUtils.DerivativeType.SWAP, monthlyReturnCats.getEquityContracts().getSwap());
                    put(EnumTypeUtils.DerivativeType.SWAPTION, monthlyReturnCats.getEquityContracts().getSwaption());
                    put(EnumTypeUtils.DerivativeType.WARRANT, monthlyReturnCats.getEquityContracts().getWarrent());
                }}}
        );
        insert3MonthDerivativeInfoHelper(
                connection,
                accessionNumber,
                EnumTypeUtils.CategoryType.FOREIGN,
                new TreeMap<>() {{{
                    put(EnumTypeUtils.DerivativeType.FORWARD, monthlyReturnCats.getForeignExchgContracts().getForward());
                    put(EnumTypeUtils.DerivativeType.FUTURE, monthlyReturnCats.getForeignExchgContracts().getFuture());
                    put(EnumTypeUtils.DerivativeType.OPTION, monthlyReturnCats.getForeignExchgContracts().getOption());
                    put(EnumTypeUtils.DerivativeType.OTHER, monthlyReturnCats.getForeignExchgContracts().getOther());
                    put(EnumTypeUtils.DerivativeType.SWAP, monthlyReturnCats.getForeignExchgContracts().getSwap());
                    put(EnumTypeUtils.DerivativeType.SWAPTION, monthlyReturnCats.getForeignExchgContracts().getSwaption());
                    put(EnumTypeUtils.DerivativeType.WARRANT, monthlyReturnCats.getForeignExchgContracts().getWarrent());
                }}}
        );
        insert3MonthDerivativeInfoHelper(
                connection,
                accessionNumber,
                EnumTypeUtils.CategoryType.OTHER,
                new TreeMap<>() {{{
                    put(EnumTypeUtils.DerivativeType.FORWARD, monthlyReturnCats.getOtherContracts().getForward());
                    put(EnumTypeUtils.DerivativeType.FUTURE, monthlyReturnCats.getOtherContracts().getFuture());
                    put(EnumTypeUtils.DerivativeType.OPTION, monthlyReturnCats.getOtherContracts().getOption());
                    put(EnumTypeUtils.DerivativeType.OTHER, monthlyReturnCats.getOtherContracts().getOther());
                    put(EnumTypeUtils.DerivativeType.SWAP, monthlyReturnCats.getOtherContracts().getSwap());
                    put(EnumTypeUtils.DerivativeType.SWAPTION, monthlyReturnCats.getOtherContracts().getSwaption());
                    put(EnumTypeUtils.DerivativeType.WARRANT, monthlyReturnCats.getOtherContracts().getWarrent());
                }}}
        );
    }

    /**
     * Inserts 3-month derivative information for a specific category type.
     *
     * @param connection       The database connection.
     * @param accessionNumber  The accession number for the report.
     * @param categoryType     The category type for which to insert derivative information.
     * @param monthNetGainsMap A map of derivative types to corresponding category data.
     * @throws SQLException if a database access error occurs or a SQL exception is thrown.
     */
    private static void insert3MonthDerivativeInfoHelper(Connection connection, String accessionNumber, EnumTypeUtils.CategoryType categoryType, TreeMap<EnumTypeUtils.DerivativeType, Category> monthNetGainsMap) throws SQLException {
        for (Map.Entry<EnumTypeUtils.DerivativeType, Category> entry : monthNetGainsMap.entrySet()) {
            // For each derivative in category, insert 3-month returns
            insertMonthDerivativeInfo(
                    connection,
                    accessionNumber,
                    1,
                    categoryType,
                    entry.getKey(),
                    insertIntoNetGains(
                            connection,
                            entry.getValue().getMonth1().getNetRealizedGain(),
                            entry.getValue().getMonth1().getNetUnrealizedAppr()
                    )
            );
            insertMonthDerivativeInfo(
                    connection,
                    accessionNumber,
                    2,
                    categoryType,
                    entry.getKey(),
                    insertIntoNetGains(
                            connection,
                            entry.getValue().getMonth2().getNetRealizedGain(),
                            entry.getValue().getMonth2().getNetUnrealizedAppr()
                    )
            );
            insertMonthDerivativeInfo(
                    connection,
                    accessionNumber,
                    3,
                    categoryType,
                    entry.getKey(),
                    insertIntoNetGains(
                            connection,
                            entry.getValue().getMonth3().getNetRealizedGain(),
                            entry.getValue().getMonth3().getNetUnrealizedAppr()
                    )
            );
        }
    }

    /**
     * Inserts data related to derivative information for a specific month, category, and net gains into the 'derivative_info' table.
     *
     * @param connection           The database connection to use for the insert operation.
     * @param accessionNumber      The accession number associated with the data.
     * @param monthTypeId          The identifier for the month type.
     * @param categoryType         The identifier for the category type.
     * @param netGainsId           The identifier for the corresponding net gains data.
     * @throws SQLException         If a database access error occurs or this method is called on a closed connection.
     */
    private static void insertMonthDerivativeInfo(Connection connection, String accessionNumber, Integer monthTypeId, EnumTypeUtils.CategoryType categoryType, EnumTypeUtils.DerivativeType derivativeType, Integer netGainsId) throws SQLException {
        String insertNPORT_P_derivative_info = "insert into derivative_info(accession_number, month_type_id, category_type_id, derivative_type_id, net_gains_id) VALUES (?, ?, ?, ?, ?);";

        try (
            PreparedStatement preparedStatement = connection.prepareStatement(insertNPORT_P_derivative_info)
        ) {
            preparedStatement.setString(1, accessionNumber);
            preparedStatement.setInt(2, monthTypeId);
            preparedStatement.setInt(3, categoryType.getTypeId());
            preparedStatement.setInt(4, derivativeType.getTypeId());
            preparedStatement.setInt(5, netGainsId);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Inserts three months' worth of category information data into the 'category_info' table for a given set of monthly return categories.
     *
     * @param connection           The database connection to use for the insert operations.
     * @param accessionNumber      The accession number associated with the data.
     * @param monthlyReturnCats   The set of monthly return categories for different contract types.
     * @throws SQLException         If a database access error occurs or this method is called on a closed connection.
     */
    private static void insert3MonthCategoryInfo(Connection connection, String accessionNumber, MonthlyReturnCats monthlyReturnCats) throws SQLException {
        insert3MonthCategoryInfoHelper(
                connection,
                accessionNumber,
                EnumTypeUtils.CategoryType.COMMODITY,
                new TreeMap<>() {{{
                    put(1, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getCommodityContracts().getMonth1().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getCommodityContracts().getMonth1().getNetUnrealizedAppr());
                    }}});
                    put(2, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getCommodityContracts().getMonth2().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getCommodityContracts().getMonth2().getNetUnrealizedAppr());
                    }}});
                    put(3, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getCommodityContracts().getMonth3().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getCommodityContracts().getMonth3().getNetUnrealizedAppr());
                    }}});
                }}}
        );
        insert3MonthCategoryInfoHelper(
                connection,
                accessionNumber,
                EnumTypeUtils.CategoryType.CREDIT,
                new TreeMap<>() {{{
                    put(1, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getCreditContracts().getMonth1().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getCreditContracts().getMonth1().getNetUnrealizedAppr());
                    }}});
                    put(2, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getCreditContracts().getMonth2().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getCreditContracts().getMonth2().getNetUnrealizedAppr());
                    }}});
                    put(3, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getCreditContracts().getMonth3().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getCreditContracts().getMonth3().getNetUnrealizedAppr());
                    }}});
                }}}
        );

        insert3MonthCategoryInfoHelper(
                connection,
                accessionNumber,
                EnumTypeUtils.CategoryType.EQUITY,
                new TreeMap<>() {{{
                    put(1, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getEquityContracts().getMonth1().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getEquityContracts().getMonth1().getNetUnrealizedAppr());
                    }}});
                    put(2, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getEquityContracts().getMonth2().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getEquityContracts().getMonth2().getNetUnrealizedAppr());
                    }}});
                    put(3, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getEquityContracts().getMonth3().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getEquityContracts().getMonth3().getNetUnrealizedAppr());
                    }}});
                }}}
        );
        insert3MonthCategoryInfoHelper(
                connection,
                accessionNumber,
                EnumTypeUtils.CategoryType.FOREIGN,
                new TreeMap<>() {{{
                    put(1, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getForeignExchgContracts().getMonth1().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getForeignExchgContracts().getMonth1().getNetUnrealizedAppr());
                    }}});
                    put(2, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getForeignExchgContracts().getMonth2().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getForeignExchgContracts().getMonth2().getNetUnrealizedAppr());
                    }}});
                    put(3, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getForeignExchgContracts().getMonth3().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getForeignExchgContracts().getMonth3().getNetUnrealizedAppr());
                    }}});
                }}}
        );
        insert3MonthCategoryInfoHelper(
                connection,
                accessionNumber,
                EnumTypeUtils.CategoryType.OTHER,
                new TreeMap<>() {{{
                    put(1, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getOtherContracts().getMonth1().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getOtherContracts().getMonth1().getNetUnrealizedAppr());
                    }}});
                    put(2, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getOtherContracts().getMonth2().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getOtherContracts().getMonth2().getNetUnrealizedAppr());
                    }}});
                    put(3, new NetGains() {{{
                        setNetRealizedGain(monthlyReturnCats.getOtherContracts().getMonth3().getNetRealizedGain());
                        setNetUnrealizedAppr(monthlyReturnCats.getOtherContracts().getMonth3().getNetUnrealizedAppr());
                    }}});
                }}}
        );
    }

    /**
     * Inserts three months' worth of category information data for a specific category type into the 'category_info' table.
     *
     * @param connection           The database connection to use for the insert operations.
     * @param accessionNumber      The accession number associated with the data.
     * @param categoryType         The category type for which data is being inserted.
     * @param monthNetGainsMap     A map containing net gains data for three months.
     * @throws SQLException         If a database access error occurs or this method is called on a closed connection.
     */
    private static void insert3MonthCategoryInfoHelper(Connection connection, String accessionNumber, EnumTypeUtils.CategoryType categoryType, TreeMap<Integer, NetGains> monthNetGainsMap) throws SQLException {
        for (int i = 1; i <= 3; i++) {
            insertMonthCategoryInfo(
                    connection,
                    accessionNumber,
                    i,
                    categoryType.getTypeId(),
                    insertIntoNetGains(
                            connection,
                            monthNetGainsMap.get(i).getNetRealizedGain(),
                            monthNetGainsMap.get(i).getNetUnrealizedAppr()
                    )
            );
        }
    }

    /**
     * Inserts data into the 'category_info' table with the provided values.
     *
     * @param connection     The database connection to use for the insert operation.
     * @param accessionNumber The accession number associated with the data.
     * @param monthTypeId    The month type identifier to insert.
     * @param categoryTypeId The category type identifier to insert.
     * @param netGainsId     The net gains identifier to insert.
     * @throws SQLException   If a database access error occurs or this method is called on a closed connection.
     */
    private static void insertMonthCategoryInfo(Connection connection, String accessionNumber, Integer monthTypeId, Integer categoryTypeId, Integer netGainsId) throws SQLException {
        String insertNPORT_P_category_info = "insert into category_info(accession_number, month_type_id, category_type_id, net_gains_id) VALUES (?, ?, ?, ?);";

        try (
            PreparedStatement preparedStatement = connection.prepareStatement(insertNPORT_P_category_info)
        ) {
            preparedStatement.setString(1, accessionNumber);
            preparedStatement.setInt(2, monthTypeId);
            preparedStatement.setInt(3, categoryTypeId);
            preparedStatement.setInt(4, netGainsId);

            preparedStatement.executeUpdate();
        }
    }

    /**
     * Inserts data into the 'net_gains' table and retrieves the generated key for the new record.
     *
     * @param connection       The database connection to use for the insert operation.
     * @param netRealizedGain  The net realized gain value to insert.
     * @param netUnrealizedAppr The net unrealized appreciation value to insert.
     * @return The result set for the inserted record in the 'net_gains' table.
     * @throws SQLException     If a database access error occurs or this method is called on a closed connection.
     */
    private static int insertIntoNetGains (Connection connection, Double netRealizedGain, Double netUnrealizedAppr) throws SQLException {
        String insertMonthsNetGains = "insert into net_gains(net_realized_gain, net_unrealized_appr) VALUES (?, ?);";

        try (
                PreparedStatement prepareMonthsNetGainsStatement = connection.prepareStatement(insertMonthsNetGains, Statement.RETURN_GENERATED_KEYS)
        ) {
            prepareMonthsNetGainsStatement.setDouble(1, netRealizedGain);
            prepareMonthsNetGainsStatement.setDouble(2, netUnrealizedAppr);

            prepareMonthsNetGainsStatement.executeUpdate();
            ResultSet resultSet = prepareMonthsNetGainsStatement.getGeneratedKeys();
            resultSet.next();

            // Retrieve and return the generated key for the inserted record
            return resultSet.getInt(1);
        }
    }

    /**
     * Batch inserts data into the 'class_info' table with the provided values.
     *
     * @param connection     The database connection to use for the insert operation.
     * @param accessionNumber The accession number associated with the data.
     * @param monthTypeId    The month type identifier to insert.
     * @param classId        The class identifier to insert.
     * @param percentReturn  The percent return value to insert.
     * @throws SQLException   If a database access error occurs or this method is called on a closed connection.
     */
    private static void insertClassInfo(Connection connection, String accessionNumber, Integer monthTypeId, String classId, Double percentReturn) throws SQLException {
        String insertNPORT_P_class_info = "insert into class_info(accession_number, month_type_id, class_id, percent_return) VALUES (?, ?, ?, ?);";

        try (
                PreparedStatement preparedStatement = connection.prepareStatement(insertNPORT_P_class_info)
        ) {
            preparedStatement.setString(1, accessionNumber);
            preparedStatement.setInt(2, monthTypeId);
            preparedStatement.setString(3, classId);
            preparedStatement.setDouble(4, percentReturn);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Inserts three months' worth of class information data into the 'class_info' table for a list of class percent returns.
     *
     * @param connection         The database connection to use for the insert operations.
     * @param accessionNumber    The accession number associated with the data.
     * @param monthlyTotReturns  A list of class percent returns for three months.
     * @throws SQLException       If a database access error occurs or this method is called on a closed connection.
     */
    private static void insert3MonthClassInfo(Connection connection, String accessionNumber, List<MonthlyClassPercentReturns> monthlyTotReturns) throws SQLException {
        for (MonthlyClassPercentReturns monthlyClassPercentReturns : monthlyTotReturns) {
            insertClassInfo(
                    connection,
                    accessionNumber,
                    1,
                    monthlyClassPercentReturns.getClassId(),
                    monthlyClassPercentReturns.getMonth1()
            );
            insertClassInfo(
                    connection,
                    accessionNumber,
                    2,
                    monthlyClassPercentReturns.getClassId(),
                    monthlyClassPercentReturns.getMonth2()
            );
            insertClassInfo(
                    connection,
                    accessionNumber,
                    3,
                    monthlyClassPercentReturns.getClassId(),
                    monthlyClassPercentReturns.getMonth3()
            );
        }
    }

    /**
     * Batch inserts net gains and months info data into the database.
     *
     * @param connection      The database connection to use for the inserts.
     * @param fundInfo        The FundInfo object containing the data to insert.
     * @param accessionNumber The accession number associated with the data.
     * @throws SQLException    If a database access error occurs or this method is called on a closed connection.
     */
    private static void insert3MonthInfo(Connection connection, FundInfo fundInfo, String accessionNumber) throws SQLException {
        insertIntoMonthsInfo(
                connection,
                accessionNumber,
                1,
                insertIntoNetGains(
                        connection,
                        fundInfo.getReturnInfo().getOthMon1().getNetRealizedGain(),
                        fundInfo.getReturnInfo().getOthMon1().getNetUnrealizedAppr()
                ),
                fundInfo.getMon1InvstFlowInfo().getRedemption(),
                fundInfo.getMon1InvstFlowInfo().getReinvestment(),
                fundInfo.getMon1InvstFlowInfo().getSales()
        );
        insertIntoMonthsInfo(
                connection,
                accessionNumber,
                2,
                insertIntoNetGains(
                        connection,
                        fundInfo.getReturnInfo().getOthMon2().getNetRealizedGain(),
                        fundInfo.getReturnInfo().getOthMon2().getNetUnrealizedAppr()
                ),
                fundInfo.getMon2InvstFlowInfo().getRedemption(),
                fundInfo.getMon2InvstFlowInfo().getReinvestment(),
                fundInfo.getMon2InvstFlowInfo().getSales()
        );
        insertIntoMonthsInfo(
                connection,
                accessionNumber,
                3,
                insertIntoNetGains(
                        connection,
                        fundInfo.getReturnInfo().getOthMon3().getNetRealizedGain(),
                        fundInfo.getReturnInfo().getOthMon3().getNetUnrealizedAppr()
                ),
                fundInfo.getMon3InvstFlowInfo().getRedemption(),
                fundInfo.getMon3InvstFlowInfo().getReinvestment(),
                fundInfo.getMon3InvstFlowInfo().getSales()
        );
    }

    /**
     * Inserts data into the 'months_info' table with the provided values.
     *
     * @param connection       The database connection to use for the insert operation.
     * @param accessionNumber  The accession number associated with the data.
     * @param monthTypeId      The month type identifier to insert.
     * @param netGainsId       The net gains identifier to insert.
     * @param redemption       The redemption value to insert.
     * @param reinvestment     The reinvestment value to insert.
     * @param sales            The sales value to insert.
     * @throws SQLException     If a database access error occurs or this method is called on a closed connection.
     */
    private static void insertIntoMonthsInfo(Connection connection, final String accessionNumber, final Integer monthTypeId, final Integer netGainsId, final Double redemption, final Double reinvestment, final Double sales) throws SQLException {
        String insertMonthsInfo = "insert into months_info(accession_number, month_type_id, net_gains_id, redemption, reinvestment, sales) VALUES (?, ?, ?, ?, ?, ?);";

        try (
                PreparedStatement prepareInsertIntoMonthsInfoStatement = connection.prepareStatement(insertMonthsInfo)
        ) {
            prepareInsertIntoMonthsInfoStatement.setString(1, accessionNumber);
            prepareInsertIntoMonthsInfoStatement.setInt(2, monthTypeId);
            prepareInsertIntoMonthsInfoStatement.setInt(3, netGainsId);
            prepareInsertIntoMonthsInfoStatement.setDouble(4, redemption);
            prepareInsertIntoMonthsInfoStatement.setDouble(5, reinvestment);
            prepareInsertIntoMonthsInfoStatement.setDouble(6, sales);

            prepareInsertIntoMonthsInfoStatement.executeUpdate();
        }
    }
}


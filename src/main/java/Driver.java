import util.DbManager;
import util.EdgarManager;
import util.EnumTypeUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Driver {
    public static int limit = 1; // Set your desired limit
    public static int offset = 1; // Initialize offset
    private static final Logger logger = Logger.getLogger(DbManager.class.getName());


    public static void main(String[] args) {

        try {
            FileHandler fileHandler = new FileHandler(Driver.class.getName());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            // Query CIK numbers from database
            List<Integer> cikList;
            if ((cikList = DbManager.getCikNumbersWithoutFiling(limit, offset, EnumTypeUtils.FormType.NPORT_P)) == null) {
                // Failed query, skip
                logger.severe("Failed to query CIK numbers.");
                return;
            }

            if (cikList.isEmpty()) {
                // No more CIKs to process
                break;
            }

            // Stores accession numbers for later storing in database
            Map<Integer, List<String>> cikAccessionMap = EdgarManager.getAccessionNumbers(cikList);

            try {
                // Update database with accession numbers
                DbManager.insertFilingWithAccessionNumbers(cikAccessionMap);
            } catch (SQLException e) {
                // Failed to update database with accession numbers
                logger.severe(e.getMessage());
            }

            return;
            // Get NPORT-P with accession number

            // Update database with file

            // Increment offset for the next batch
       //     offset += limit;
        }

    }

    private static void printCikAccessionMap(Map<Integer, List<String>> cikAccessionMap) {
        for (Map.Entry<Integer, List<String>> entry : cikAccessionMap.entrySet()) {
            Integer cik = entry.getKey();
            List<String> accessionNumbers = entry.getValue();

            System.out.println("CIK: " + cik);
            System.out.println("Accession Numbers: ");

            for (String accessionNumber : accessionNumbers) {
                System.out.println("  " + accessionNumber);
            }

            System.out.println(); // Add a blank line for separation
        }
    }
}

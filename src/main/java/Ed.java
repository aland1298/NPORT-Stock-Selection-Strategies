import util.DbManager;
import util.EdgarManager;
import util.EnumTypeUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Ed {
    public static int limit = 1; // Set your desired limit
    public static int offset = 1; // Initialize offset
    private static final Logger logger = Logger.getLogger(DbManager.class.getName());

    public static void main(String[] args) {
        initErrorLogger();

        if (args.length == 0) {
            System.out.println("No arguments provided. Usage: java Driver [arg1] [arg2] ...");
            return;
        }

        displayWelcomeMessage();
        displayMenuOptions();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("ed> ");

            switch (scanner.nextLine()) {

            }
        }
    }

    private static void displayWelcomeMessage() {
        System.out.println("Welcome, my name is Ed (short for Edgar)!");
    }

    private static void displayMenuOptions() {
        System.out.println("Menu: ");
        System.out.println("\thelp - Displays this menu");
        System.out.println("\tinit - WARNING");
        System.out.println("\texit - Terminates the program");
    }

    private static void initDB() {
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

            try {
                // Get NPORT-P with accession number
                DbManager.insertNPORTFilings(cikAccessionMap);
            } catch (SQLException e) {
                // Failed to update database with NPORT filings
                logger.severe(e.getMessage());
            }
            return;
            //     offset += limit;
        }
    }

    private static void testInsert() {
        try {
            Map<Integer, List<String>> cikAccessionMap = EdgarManager.getAccessionNumbers(List.of(2230));
            var map = new HashMap<Integer, List<String>>();
            var arr = new ArrayList<String>();
            arr.add(cikAccessionMap.get(2230).get(0));
            map.put(2230, arr);

            // Get NPORT-P with accession number
            DbManager.insertNPORTFilings(map);
        } catch (SQLException e) {
            // Failed to update database with NPORT filings
            logger.severe(e.getMessage());
        }
    }

    private static void initErrorLogger() {
        try {
            // Set error logger
            FileHandler fileHandler = new FileHandler("src\\main\\java\\logs\\" + Ed.class.getName());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

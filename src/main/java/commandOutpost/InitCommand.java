package commandOutpost;

import util.DbManager;
import util.EdgarManager;
import util.EnumTypeUtils;

import java.sql.SQLException;
import java.util.*;

public class InitCommand implements Command {
    public static int limit = 1; // Set your desired limit
    public static int offset = 1; // Initialize offset

    @Override
    public void execute() {
        if (!DbManager.isDatasource()) {
            // Datasource not yet established
            System.out.println("Datasource not configured. Try command 'db conn' to configure datasource.");
        } else {
            Scanner scanner = new Scanner(System.in);

            printWarning();

            System.out.print("Are you sure you want to continue? (y/n) ");
            String shouldContinue = scanner.nextLine();

            if (shouldContinue.equalsIgnoreCase("y")) {
                // Start process
                //testInsert();
                // process();
            }
        }
    }

    private void printWarning() {
        System.out.println(
                """
                    !WARNING!WARNING!WARNING!WARNING!WARNING!WARNING!WARNING!WARNING!WARNING!
                    !WARNING!                                                       !WARNING!
                    !WARNING!       This program is synchronous. Once this task     !WARNING!
                    !WARNING!       begins, it will run in the foreground. You      !WARNING!
                    !WARNING!       will have to end the program in order to use    !WARNING!
                    !WARNING!       this terminal. If you abruptly end this task    !WARNING!
                    !WARNING!       you will need to delete all of the progress     !WARNING!
                    !WARNING!       already completed and restart.                  !WARNING!
                    !WARNING!                                                       !WARNING!
                    !WARNING!WARNING!WARNING!WARNING!WARNING!WARNING!WARNING!WARNING!WARNING!
                """
        );
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
            System.err.println("Failed to insert NPORT filings.");
        }
    }

    private static void process() {
        while (true) {
            // Query CIK numbers from database
            List<Integer> cikList;
            if ((cikList = DbManager.getCikNumbersWithoutFiling(limit, offset, EnumTypeUtils.FormType.NPORT_P)) == null) {
                // Failed query, skip
                System.err.println("Failed to query CIK numbers.");
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
                System.out.println("Failed to insert accession numbers.");
            }

            try {
                // Get NPORT-P with accession number
                DbManager.insertNPORTFilings(cikAccessionMap);
            } catch (SQLException e) {
                // Failed to update database with NPORT filings
                System.err.println("Failed to insert NPORT filings");
            }
            return;
            //     offset += limit;
        }
    }

    private void displayProcessStatistic(final Integer processed, final Integer errors) {
        System.out.printf("Processed[%d] : Errors[%d]\n", processed, errors);
    }
}

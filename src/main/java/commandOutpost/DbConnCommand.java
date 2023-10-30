package commandOutpost;

import util.DbManager;

import java.util.Scanner;

public class DbConnCommand implements Command {

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);

        if (DbManager.isDatasource()) {
            System.out.println("Datasource is already configured to: ");
            System.out.println("\tURL: " + DbManager.getDatasource().getJdbcUrl());
            System.out.print("Reconfigure datasource? (y/n) ");
            String shouldReconfigure = scanner.nextLine();
            if (!shouldReconfigure.equalsIgnoreCase("y")) {
                // Keep datasource configurations
                return;
            }
        }

        System.out.print("URI: ");
        String uri = scanner.nextLine();

        System.out.print("Credentials? (y/n) ");
        String hasCredentials = scanner.nextLine();
        if (hasCredentials.equalsIgnoreCase("y")) {
            System.out.print("Username: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String password = scanner.nextLine();
            DbManager.establishConnection(uri, username, password);
        } else if (hasCredentials.equalsIgnoreCase("n")) {
            DbManager.establishConnection(uri, null, null);
        } else {
            // Invalid response
            System.out.println("Invalid response: " + hasCredentials);
        }
    }


}

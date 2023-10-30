package commandOutpost;

public class HelpCommand implements Command {

    @Override
    public void execute() {
        displayMenuOptions();
    }

    /**
     * Displays to the console the menu option.
     */
    private static void displayMenuOptions() {
        System.out.println("Menu: ");
        System.out.println("\thelp - Displays this menu.");
        System.out.println("\tinit - Downloads NPORT data to the database.");
        System.out.println("\texit - Terminates the program.");
        System.out.println("\tdb conn - Attempts to establish a connection to the database.");
        System.out.println("\tdb test - Tests the database connection.");
        System.out.println("\tdb close - Closes the datasource by closing the connection pool and setting the variable to null.");
    }
}

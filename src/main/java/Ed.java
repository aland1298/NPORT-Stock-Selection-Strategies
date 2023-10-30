import commandOutpost.CmdManager;

import java.io.IOException;
import java.util.*;

public class Ed {

    public static void main(String[] args) {
        displayWelcomeMessage();

        CmdManager cmdManager = null;
        try {
            cmdManager = new CmdManager();
        } catch (IOException ioException) {
            System.err.println("Failed to create instance of: " + CmdManager.class.getName());
            System.exit(1);
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("ed> ");
            String userInput = scanner.nextLine().trim();

            // Execute the user command
            cmdManager.executeCommand(userInput);
        }
    }

    /**
     * Displays to the console the welcome message.
     */
    private static void displayWelcomeMessage() {
        System.out.println("Welcome, my name is Ed (short for Edgar)!");
        System.out.println("WARNING: Edgar cannot multitask.");
    }
}

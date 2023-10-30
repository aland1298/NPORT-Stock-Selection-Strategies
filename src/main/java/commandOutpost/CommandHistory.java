package commandOutpost;

import java.util.LinkedList;
import java.util.List;

/**
 * CommandHistory class is responsible for managing command history with a maximum size.
 */
public class CommandHistory {
    private static final int MAX_HISTORY_SIZE = 25;
    private List<String> commandListHistory;
    private int previousCommandIndex = -1;

    /**
     * Initializes a new CommandHistory instance.
     */
    public CommandHistory() {
        commandListHistory = new LinkedList<>();
    }

    /**
     * Adds a command to the history. If the maximum history size is reached,
     * the oldest command is dropped.
     *
     * @param command The command to add to the history.
     */
    public void addCommand(String command) {
        if (commandListHistory.size() >= MAX_HISTORY_SIZE) {
            // Remove the oldest command
            commandListHistory.remove(commandListHistory.size() - 1);
        }
        commandListHistory.add(command);
        previousCommandIndex = -1;
    }

    /**
     * Retrieves the previous command from the history.
     *
     * @return The previous command from the history.
     */
    private String getPreviousHistory() {
        if (previousCommandIndex == commandListHistory.size() - 1) return commandListHistory.get(previousCommandIndex);
        return commandListHistory.get(previousCommandIndex++);
    }

    /**
     * Retrieves the next command from the history.
     *
     * @return The next command from the history.
     */
    private String getNextHistory() {
        if (previousCommandIndex == -1) return "";
        return commandListHistory.get(previousCommandIndex--);
    }
}

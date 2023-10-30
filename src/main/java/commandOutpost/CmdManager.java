package commandOutpost;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * CmdManager class is responsible for managing and executing commands.
 */
public class CmdManager {
    private Map<String, Command> commandMap;
    private CommandHistory commandHistory;

    /**
     * Initializes a new CmdManager instance, configures the logger, and registers the 'exit' command.
     *
     * @throws IOException If there is an issue with log file handling.
     */
    public CmdManager() throws IOException {
        commandHistory = new CommandHistory();
        commandMap = new HashMap<>();

        // Register commands
        registerCommand("exit", new ExitCommand());
        registerCommand("help", new HelpCommand());
        registerCommand("init", new InitCommand());
        registerCommand("db conn", new DbConnCommand());
        registerCommand("db test", new DbTestCommand());
        registerCommand("db close", new DbCloseCommand());
    }

    /**
     * Registers a command with the specified command name and associated command object.
     *
     * @param commandName The name of the command.
     * @param command     The associated command object.
     */
    public void registerCommand(String commandName, Command command) {
        commandMap.put(commandName, command);
    }

    /**
     * Executes the specified command by name. If the command is registered, it is executed.
     * Otherwise, a message is printed indicating that the command is unknown.
     *
     * @param commandName The name of the command to execute.
     */
    public void executeCommand(String commandName) {
        Command command = commandMap.get(commandName);
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Unknown command: " + commandName);
            System.out.println("Try 'help' to get a list of commands");
        }

        // Add the executed command to the command history
        this.commandHistory.addCommand(commandName);
    }
}

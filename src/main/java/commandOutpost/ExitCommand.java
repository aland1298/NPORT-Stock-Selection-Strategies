package commandOutpost;

public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Exiting the program...");
        System.exit(0);
    }
}

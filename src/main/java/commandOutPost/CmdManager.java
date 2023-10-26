package commandOutPost;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class CmdManager {
    private Logger logger = Logger.getLogger(CmdManager.class.getName());
    private List<String> commandList = new LinkedList<>();
    private int previousCommandIndex = 0;

    private enum Commands {
        EXIT()
    }

    public CmdManager() throws IOException {
        FileHandler fileHandler = new FileHandler("src\\main\\java\\logs\\" + CmdManager.class.getName());
        logger.addHandler(fileHandler);
    }

    private void addCommand(String command) {
        commandList.add(command);
    }

    private String getPreviousCommand() {
        if (previousCommandIndex == commandList.size() - 1) return commandList.get(previousCommandIndex);
        return commandList.get(previousCommandIndex++);
    }

    private String getNextCommand() {
        if (previousCommandIndex == 0) return "";
        return commandList.get(previousCommandIndex--);
    }
}

package commandOutpost;

import util.DbManager;

public class DbCloseCommand implements Command {
    @Override
    public void execute() {
        DbManager.closeConnection();
        System.out.println("Datasource closed. To reconfigure datasource, enter command 'db conn'.");
    }
}

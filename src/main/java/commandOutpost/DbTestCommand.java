package commandOutpost;

import util.DbManager;

public class DbTestCommand implements Command {
    @Override
    public void execute() {
        if (!DbManager.isDatasource()) {
            // Datasource was not configured
            System.out.println("Datasource is not configured.");
        } else {
            System.out.println("Datasource is connected to: " + DbManager.getDatasource().getJdbcUrl());
        }
    }
}

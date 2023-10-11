package entities.filings.nport_p;

import java.util.List;

public class ReturnInfo {
    private List<MonthlyTotReturn> monthlyTotReturns;
    private MonthlyReturnCats monthlyReturnCats;
    private MonthsReturn othMon1;
    private MonthsReturn othMon2;
    private MonthsReturn othMon3;

    public MonthsReturn getOthMon1() {
        return othMon1;
    }

    public void setOthMon1(MonthsReturn othMon1) {
        this.othMon1 = othMon1;
    }

    public MonthsReturn getOthMon2() {
        return othMon2;
    }

    public void setOthMon2(MonthsReturn othMon2) {
        this.othMon2 = othMon2;
    }

    public MonthsReturn getOthMon3() {
        return othMon3;
    }

    public void setOthMon3(MonthsReturn othMon3) {
        this.othMon3 = othMon3;
    }

    public List<MonthlyTotReturn> getMonthlyTotReturns() {
        return monthlyTotReturns;
    }

    public void setMonthlyTotReturns(List<MonthlyTotReturn> monthlyTotReturns) {
        this.monthlyTotReturns = monthlyTotReturns;
    }

    public MonthlyReturnCats getMonthlyReturnCats() {
        return monthlyReturnCats;
    }

    public void setMonthlyReturnCats(MonthlyReturnCats monthlyReturnCats) {
        this.monthlyReturnCats = monthlyReturnCats;
    }
}

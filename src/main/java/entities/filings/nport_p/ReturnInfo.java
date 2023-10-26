package entities.filings.nport_p;

import java.util.List;

public class ReturnInfo {
    private List<MonthlyClassPercentReturns> monthlyClassPercentReturns;
    private MonthlyReturnCats monthlyReturnCats;
    private NetGains othMon1;
    private NetGains othMon2;
    private NetGains othMon3;

    public NetGains getOthMon1() {
        return othMon1;
    }

    public void setOthMon1(NetGains othMon1) {
        this.othMon1 = othMon1;
    }

    public NetGains getOthMon2() {
        return othMon2;
    }

    public void setOthMon2(NetGains othMon2) {
        this.othMon2 = othMon2;
    }

    public NetGains getOthMon3() {
        return othMon3;
    }

    public void setOthMon3(NetGains othMon3) {
        this.othMon3 = othMon3;
    }

    public List<MonthlyClassPercentReturns> getMonthlyTotReturns() {
        return monthlyClassPercentReturns;
    }

    public void setMonthlyTotReturns(List<MonthlyClassPercentReturns> monthlyClassPercentReturns) {
        this.monthlyClassPercentReturns = monthlyClassPercentReturns;
    }

    public MonthlyReturnCats getMonthlyReturnCats() {
        return monthlyReturnCats;
    }

    public void setMonthlyReturnCats(MonthlyReturnCats monthlyReturnCats) {
        this.monthlyReturnCats = monthlyReturnCats;
    }
}

package entities.filings.nport_p;

public class MonthlyReturnCats {
    private Contracts commodityContracts;
    private Contracts creditContracts;
    private Contracts equityContracts;
    private Contracts foreignExchgContracts;
    private Contracts interestRtContracts;
    private Contracts otherContracts;

    public Contracts getCommodityContracts() {
        return commodityContracts;
    }

    public void setCommodityContracts(Contracts commodityContracts) {
        this.commodityContracts = commodityContracts;
    }

    public Contracts getCreditContracts() {
        return creditContracts;
    }

    public void setCreditContracts(Contracts creditContracts) {
        this.creditContracts = creditContracts;
    }

    public Contracts getEquityContracts() {
        return equityContracts;
    }

    public void setEquityContracts(Contracts equityContracts) {
        this.equityContracts = equityContracts;
    }

    public Contracts getForeignExchgContracts() {
        return foreignExchgContracts;
    }

    public void setForeignExchgContracts(Contracts foreignExchgContracts) {
        this.foreignExchgContracts = foreignExchgContracts;
    }

    public Contracts getInterestRtContracts() {
        return interestRtContracts;
    }

    public void setInterestRtContracts(Contracts interestRtContracts) {
        this.interestRtContracts = interestRtContracts;
    }

    public Contracts getOtherContracts() {
        return otherContracts;
    }

    public void setOtherContracts(Contracts otherContracts) {
        this.otherContracts = otherContracts;
    }
}

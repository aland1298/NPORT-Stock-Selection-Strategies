package entities.filings.nport_p;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class FormData {
    private GenInfo genInfo;
    private FundInfo fundInfo;
    @JacksonXmlProperty(localName = "invstOrSecs")
    private List<Security> invstPortfolio;

    public GenInfo getGenInfo() {
        return genInfo;
    }

    public void setGenInfo(GenInfo genInfo) {
        this.genInfo = genInfo;
    }

    public FundInfo getFundInfo() {
        return fundInfo;
    }

    public void setFundInfo(FundInfo fundInfo) {
        this.fundInfo = fundInfo;
    }

    public List<Security> getInvstPortfolio() {
        return invstPortfolio;
    }

    public void setInvstPortfolio(List<Security> invstPortfolio) {
        this.invstPortfolio = invstPortfolio;
    }
}

package entities.filings.nport_p;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class FundInfo {
    private Double totAssets;
    private Double netAssets;
    private ReturnInfo returnInfo;
    @JacksonXmlProperty(localName = "mon1Flow")
    private InvstFlowInfo mon1InvstFlowInfo;
    @JacksonXmlProperty(localName = "mon2Flow")
    private InvstFlowInfo mon2InvstFlowInfo;
    @JacksonXmlProperty(localName = "mon3Flow")
    private InvstFlowInfo mon3InvstFlowInfo;

    public InvstFlowInfo getMon1InvstFlowInfo() {
        return mon1InvstFlowInfo;
    }

    public void setMon1InvstFlowInfo(InvstFlowInfo mon1InvstFlowInfo) {
        this.mon1InvstFlowInfo = mon1InvstFlowInfo;
    }

    public InvstFlowInfo getMon2InvstFlowInfo() {
        return mon2InvstFlowInfo;
    }

    public void setMon2InvstFlowInfo(InvstFlowInfo mon2InvstFlowInfo) {
        this.mon2InvstFlowInfo = mon2InvstFlowInfo;
    }

    public InvstFlowInfo getMon3InvstFlowInfo() {
        return mon3InvstFlowInfo;
    }

    public void setMon3InvstFlowInfo(InvstFlowInfo mon3InvstFlowInfo) {
        this.mon3InvstFlowInfo = mon3InvstFlowInfo;
    }

    public Double getTotAssets() {
        return totAssets;
    }

    public void setTotAssets(Double totAssets) {
        this.totAssets = totAssets;
    }

    public Double getNetAssets() {
        return netAssets;
    }

    public void setNetAssets(Double netAssets) {
        this.netAssets = netAssets;
    }

    public ReturnInfo getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(ReturnInfo returnInfo) {
        this.returnInfo = returnInfo;
    }
}

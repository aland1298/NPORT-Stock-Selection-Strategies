package entities.filings.nport_p;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Security {
    @JacksonXmlProperty(localName = "name")
    private String isserName;
    @JacksonXmlProperty(localName = "title")
    private String issueName;
    private String cusip;
    private Double valUSD;
    @JacksonXmlProperty(localName = "pctVal")
    private Double percentOfNAV;
    @JacksonXmlProperty(localName = "payoffProfile")
    private String tradeType;
    private String fairValLevel;
    private String assetCat;
    private AssetConditional assetConditional;
    private String issuerCat;
    private String lei;
    @JacksonXmlProperty(localName = "invCountry")
    private String investorCountry;

    public String getLei() {
        return lei;
    }

    public void setLei(String lei) {
        this.lei = lei;
    }

    public Double getValUSD() {
        return valUSD;
    }

    public void setValUSD(Double valUSD) {
        this.valUSD = valUSD;
    }

    public Double getPercentOfNAV() {
        return percentOfNAV;
    }

    public void setPercentOfNAV(Double percentOfNAV) {
        this.percentOfNAV = percentOfNAV;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getFairValLevel() {
        return fairValLevel;
    }

    public void setFairValLevel(String fairValLevel) {
        this.fairValLevel = fairValLevel;
    }

    public String getAssetCat() {
        return assetCat;
    }

    public void setAssetCat(String assetCat) {
        this.assetCat = assetCat;
    }

    public AssetConditional getAssetConditional() {
        return assetConditional;
    }

    public void setAssetConditional(AssetConditional assetConditional) {
        this.assetConditional = assetConditional;
    }

    public String getIssuerCat() {
        return issuerCat;
    }

    public void setIssuerCat(String issuerCat) {
        this.issuerCat = issuerCat;
    }

    public String getInvestorCountry() {
        return investorCountry;
    }

    public void setInvestorCountry(String investorCountry) {
        this.investorCountry = investorCountry;
    }

    public String getIsserName() {
        return isserName;
    }

    public void setIsserName(String isserName) {
        this.isserName = isserName;
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getCusip() {
        return cusip;
    }

    public void setCusip(String cusip) {
        this.cusip = cusip;
    }
}

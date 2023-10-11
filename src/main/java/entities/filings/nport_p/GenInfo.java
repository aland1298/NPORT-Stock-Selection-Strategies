package entities.filings.nport_p;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.sql.Date;

public class GenInfo {
    private String seriesName;
    private String seriesId;
    @JacksonXmlProperty(localName = "repPdEnd")
    private Date fiscalPdEnd;
    @JacksonXmlProperty(localName = "repPdDate")
    private Date reported;
    private Boolean isFinalFiling;

    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public Date getFiscalPdEnd() {
        return fiscalPdEnd;
    }

    public void setFiscalPdEnd(Date fiscalPdEnd) {
        this.fiscalPdEnd = fiscalPdEnd;
    }

    public Date getReported() {
        return reported;
    }

    public void setReported(Date reported) {
        this.reported = reported;
    }

    public Boolean getFinalFiling() {
        return isFinalFiling;
    }

    public void setFinalFiling(Boolean finalFiling) {
        isFinalFiling = finalFiling;
    }
}

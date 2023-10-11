package entities.filings.nport_p;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class MonthlyTotReturn {
    private String classId;
    @JacksonXmlProperty(localName = "rtn1")
    private Double month1;
    @JacksonXmlProperty(localName = "rtn2")
    private Double month2;
    @JacksonXmlProperty(localName = "rtn3")
    private Double month3;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Double getMonth1() {
        return month1;
    }

    public void setMonth1(Double month1) {
        this.month1 = month1;
    }

    public Double getMonth2() {
        return month2;
    }

    public void setMonth2(Double month2) {
        this.month2 = month2;
    }

    public Double getMonth3() {
        return month3;
    }

    public void setMonth3(Double month3) {
        this.month3 = month3;
    }
}

package entities.filings.nport_p;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Category {
    @JacksonXmlProperty(localName = "instrMon1")
    private MonthsReturn month1;
    @JacksonXmlProperty(localName = "instrMon2")
    private MonthsReturn month2;
    @JacksonXmlProperty(localName = "instrMon3")
    private MonthsReturn month3;

    public MonthsReturn getMonth1() {
        return month1;
    }

    public void setMonth1(MonthsReturn month1) {
        this.month1 = month1;
    }

    public MonthsReturn getMonth2() {
        return month2;
    }

    public void setMonth2(MonthsReturn month2) {
        this.month2 = month2;
    }

    public MonthsReturn getMonth3() {
        return month3;
    }

    public void setMonth3(MonthsReturn month3) {
        this.month3 = month3;
    }
}

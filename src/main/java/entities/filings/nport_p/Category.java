package entities.filings.nport_p;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Category {
    @JacksonXmlProperty(localName = "instrMon1")
    private NetGains month1;
    @JacksonXmlProperty(localName = "instrMon2")
    private NetGains month2;
    @JacksonXmlProperty(localName = "instrMon3")
    private NetGains month3;

    public NetGains getMonth1() {
        return month1;
    }

    public void setMonth1(NetGains month1) {
        this.month1 = month1;
    }

    public NetGains getMonth2() {
        return month2;
    }

    public void setMonth2(NetGains month2) {
        this.month2 = month2;
    }

    public NetGains getMonth3() {
        return month3;
    }

    public void setMonth3(NetGains month3) {
        this.month3 = month3;
    }
}

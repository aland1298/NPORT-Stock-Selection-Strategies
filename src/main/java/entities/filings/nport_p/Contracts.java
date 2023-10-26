package entities.filings.nport_p;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Contracts {
    @JacksonXmlProperty(localName = "mon1")
    private NetGains month1;
    @JacksonXmlProperty(localName = "mon2")
    private NetGains month2;
    @JacksonXmlProperty(localName = "mon3")
    private NetGains month3;
    @JacksonXmlProperty(localName = "forwardCategory")
    private Category forward;
    @JacksonXmlProperty(localName = "futureCategory")
    private Category future;
    @JacksonXmlProperty(localName = "optionCategory")
    private Category option;
    @JacksonXmlProperty(localName = "swaptionCategory")
    private Category swaption;
    @JacksonXmlProperty(localName = "swapCategory")
    private Category swap;
    @JacksonXmlProperty(localName = "warrantCategory")
    private Category warrant;
    @JacksonXmlProperty(localName = "otherCategory")
    private Category other;

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

    public Category getForward() {
        return forward;
    }

    public void setForward(Category forward) {
        this.forward = forward;
    }

    public Category getFuture() {
        return future;
    }

    public void setFuture(Category future) {
        this.future = future;
    }

    public Category getOption() {
        return option;
    }

    public void setOption(Category option) {
        this.option = option;
    }

    public Category getSwaption() {
        return swaption;
    }

    public void setSwaption(Category swaption) {
        this.swaption = swaption;
    }

    public Category getSwap() {
        return swap;
    }

    public void setSwap(Category swap) {
        this.swap = swap;
    }

    public Category getWarrent() {
        return warrant;
    }

    public void setWarrant(Category warrant) {
        this.warrant = warrant;
    }

    public Category getOther() {
        return other;
    }

    public void setOther(Category other) {
        this.other = other;
    }
}

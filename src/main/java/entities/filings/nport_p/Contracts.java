package entities.filings.nport_p;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Contracts {
    @JacksonXmlProperty(localName = "mon1")
    private MonthsReturn month1;
    @JacksonXmlProperty(localName = "mon2")
    private MonthsReturn month2;
    @JacksonXmlProperty(localName = "mon3")
    private MonthsReturn month3;
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
    @JacksonXmlProperty(localName = "warrentCategory")
    private Category warrent;
    @JacksonXmlProperty(localName = "otherCategory")
    private Category other;

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
        return warrent;
    }

    public void setWarrent(Category warrent) {
        this.warrent = warrent;
    }

    public Category getOther() {
        return other;
    }

    public void setOther(Category other) {
        this.other = other;
    }
}

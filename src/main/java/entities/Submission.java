package entities;

import java.util.List;

public class Submission {
    private String cik;
    private String entityType;
    private String sic;
    private String sicDescription;
    private int insiderTransactionForOwnerExists;
    private int insiderTransactionForIssuerExists;
    private String name;
    private List<String> tickers;
    private List<String> exchanges;
    private String ein;
    private String description;
    private String website;
    private String investorWebsite;
    private String category;
    private String fiscalYearEnd;
    private String stateOfIncorporation;
    private String stateOfIncorporationDescription;
    private Addresses addresses;
    private String phone;
    private String flags;
    private List<FormerNames> formerNames; // You can create a separate class for former names
    private Filings filings;

    public static class FormerNames {
        private String name;
        private String from;
        private String to;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }
    }

    public static class Addresses {
        private Address mailing;
        private Address business;

        public Address getMailing() {
            return mailing;
        }

        public void setMailing(Address mailing) {
            this.mailing = mailing;
        }

        public Address getBusiness() {
            return business;
        }

        public void setBusiness(Address business) {
            this.business = business;
        }
    }

    public static class Address {
        private String street1;
        private String street2;
        private String city;
        private String stateOrCountry;
        private String zipCode;
        private String stateOrCountryDescription;

        public String getStreet1() {
            return street1;
        }

        public void setStreet1(String street1) {
            this.street1 = street1;
        }

        public String getStreet2() {
            return street2;
        }

        public void setStreet2(String street2) {
            this.street2 = street2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStateOrCountry() {
            return stateOrCountry;
        }

        public void setStateOrCountry(String stateOrCountry) {
            this.stateOrCountry = stateOrCountry;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getStateOrCountryDescription() {
            return stateOrCountryDescription;
        }

        public void setStateOrCountryDescription(String stateOrCountryDescription) {
            this.stateOrCountryDescription = stateOrCountryDescription;
        }
    }

    public static class Filings {
        private RecentFilings recent;
        private List<Object> files;

        public RecentFilings getRecent() {
            return recent;
        }

        public void setRecent(RecentFilings recent) {
            this.recent = recent;
        }

        public List<Object> getFiles() {
            return files;
        }

        public void setFiles(List<Object> files) {
            this.files = files;
        }
    }

    public static class RecentFilings {
        private List<String> accessionNumber;
        private List<String> filingDate;
        private List<String> reportDate;
        private List<String> acceptanceDateTime;
        private List<String> act;
        private List<String> form;
        private List<String> fileNumber;
        private List<String> filmNumber;
        private List<String> items;
        private List<Long> size;
        private List<Short> isXBRL;
        private List<Short> isInlineXBRL;
        private List<String> primaryDocument;
        private List<String> primaryDocDescription;

        public List<String> getAccessionNumber() {
            return accessionNumber;
        }

        public void setAccessionNumber(List<String> accessionNumber) {
            this.accessionNumber = accessionNumber;
        }

        public List<String> getFilingDate() {
            return filingDate;
        }

        public void setFilingDate(List<String> filingDate) {
            this.filingDate = filingDate;
        }

        public List<String> getReportDate() {
            return reportDate;
        }

        public void setReportDate(List<String> reportDate) {
            this.reportDate = reportDate;
        }

        public List<String> getAcceptanceDateTime() {
            return acceptanceDateTime;
        }

        public void setAcceptanceDateTime(List<String> acceptanceDateTime) {
            this.acceptanceDateTime = acceptanceDateTime;
        }

        public List<String> getAct() {
            return act;
        }

        public void setAct(List<String> act) {
            this.act = act;
        }

        public List<String> getForm() {
            return form;
        }

        public void setForm(List<String> form) {
            this.form = form;
        }

        public List<String> getFileNumber() {
            return fileNumber;
        }

        public void setFileNumber(List<String> fileNumber) {
            this.fileNumber = fileNumber;
        }

        public List<String> getFilmNumber() {
            return filmNumber;
        }

        public void setFilmNumber(List<String> filmNumber) {
            this.filmNumber = filmNumber;
        }

        public List<String> getItems() {
            return items;
        }

        public void setItems(List<String> items) {
            this.items = items;
        }

        public List<Long> getSize() {
            return size;
        }

        public void setSize(List<Long> size) {
            this.size = size;
        }

        public List<Short> getIsXBRL() {
            return isXBRL;
        }

        public void setIsXBRL(List<Short> isXBRL) {
            this.isXBRL = isXBRL;
        }

        public List<Short> getIsInlineXBRL() {
            return isInlineXBRL;
        }

        public void setIsInlineXBRL(List<Short> isInlineXBRL) {
            this.isInlineXBRL = isInlineXBRL;
        }

        public List<String> getPrimaryDocument() {
            return primaryDocument;
        }

        public void setPrimaryDocument(List<String> primaryDocument) {
            this.primaryDocument = primaryDocument;
        }

        public List<String> getPrimaryDocDescription() {
            return primaryDocDescription;
        }

        public void setPrimaryDocDescription(List<String> primaryDocDescription) {
            this.primaryDocDescription = primaryDocDescription;
        }
    }

    public String getCik() {
        return cik;
    }

    public void setCik(String cik) {
        this.cik = cik;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getSic() {
        return sic;
    }

    public void setSic(String sic) {
        this.sic = sic;
    }

    public String getSicDescription() {
        return sicDescription;
    }

    public void setSicDescription(String sicDescription) {
        this.sicDescription = sicDescription;
    }

    public int getInsiderTransactionForOwnerExists() {
        return insiderTransactionForOwnerExists;
    }

    public void setInsiderTransactionForOwnerExists(int insiderTransactionForOwnerExists) {
        this.insiderTransactionForOwnerExists = insiderTransactionForOwnerExists;
    }

    public int getInsiderTransactionForIssuerExists() {
        return insiderTransactionForIssuerExists;
    }

    public void setInsiderTransactionForIssuerExists(int insiderTransactionForIssuerExists) {
        this.insiderTransactionForIssuerExists = insiderTransactionForIssuerExists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTickers() {
        return tickers;
    }

    public void setTickers(List<String> tickers) {
        this.tickers = tickers;
    }

    public List<String> getExchanges() {
        return exchanges;
    }

    public void setExchanges(List<String> exchanges) {
        this.exchanges = exchanges;
    }

    public String getEin() {
        return ein;
    }

    public void setEin(String ein) {
        this.ein = ein;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getInvestorWebsite() {
        return investorWebsite;
    }

    public void setInvestorWebsite(String investorWebsite) {
        this.investorWebsite = investorWebsite;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFiscalYearEnd() {
        return fiscalYearEnd;
    }

    public void setFiscalYearEnd(String fiscalYearEnd) {
        this.fiscalYearEnd = fiscalYearEnd;
    }

    public String getStateOfIncorporation() {
        return stateOfIncorporation;
    }

    public void setStateOfIncorporation(String stateOfIncorporation) {
        this.stateOfIncorporation = stateOfIncorporation;
    }

    public String getStateOfIncorporationDescription() {
        return stateOfIncorporationDescription;
    }

    public void setStateOfIncorporationDescription(String stateOfIncorporationDescription) {
        this.stateOfIncorporationDescription = stateOfIncorporationDescription;
    }

    public Addresses getAddresses() {
        return addresses;
    }

    public void setAddresses(Addresses addresses) {
        this.addresses = addresses;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public List<FormerNames> getFormerNames() {
        return formerNames;
    }

    public void setFormerNames(List<FormerNames> formerNames) {
        this.formerNames = formerNames;
    }

    public Filings getFilings() {
        return filings;
    }

    public void setFilings(Filings filings) {
        this.filings = filings;
    }
}

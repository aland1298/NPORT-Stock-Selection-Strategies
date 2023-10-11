package enums;

public enum FilingType {
    NPORT_P("NPORT-P", "primary_doc", "xml");

    private final String form;
    private final String primaryDoc;
    private final String extensionType;

    FilingType(String form, String primaryDoc, String extensionType) {
        this.form = form;
        this.primaryDoc = primaryDoc;
        this.extensionType = extensionType;
    }

    public String getExtensionType() {
        return extensionType;
    }

    public String getForm() {
        return form;
    }

    public String getPrimaryDoc() {
        return primaryDoc;
    }
}

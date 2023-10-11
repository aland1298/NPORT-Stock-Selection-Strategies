package entities.filings;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import entities.filings.nport_p.FormData;

@JacksonXmlRootElement(localName = "edgarSubmission")
public class NPORT_P {
    private FormData formData;

    public FormData getFormData() {
        return formData;
    }

    public void setFormData(FormData formData) {
        this.formData = formData;
    }
}

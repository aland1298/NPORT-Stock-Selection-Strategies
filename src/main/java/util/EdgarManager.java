package util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.jr.ob.JSON;
import entities.Submission;
import entities.filings.NPORT_P;
import enums.FilingType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Establishes functionalities to query the Edgar database. NO THREAD SAFE
 */
public class EdgarManager {
    private static final String BASE_SUBMISSIONS_URL = "https://data.sec.gov/";
    private static final String BASE_ARCHIEVES_URL = "https://www.sec.gov/Archives/edgar/data/";
    private static final String USER_AGENT = "me@me123haha.com";
    private static final int MAX_REQUESTS_PER_SECOND = 10;
    private static final Logger logger = Logger.getLogger(EdgarManager.class.getName());

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    private EdgarManager() {/* IGNORED */}

    static {
        try {
            FileHandler fileHandler = new FileHandler(EdgarManager.class.getName());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves submission data for a specified CIK (Central Index Key) from the Submission API.
     *
     * @param cik The CIK (Central Index Key) for which submission data is to be retrieved.
     * @return A string containing the JSON response from the Submission API. Or NULL if an error occurred while
     * processing the submission.
     */
    private static Submission getSubmission(final String cik) {
        try {
            // Create an HTTP request to fetch submission data for the specified CIK.
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_SUBMISSIONS_URL + "submissions/CIK" + padCikToTenDigits(cik) + ".json"))
                    .setHeader("User-Agent", USER_AGENT)
                    .build();

            // Send the HTTP request and retrieve the response.
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the HTTP response is successful (status code 200).
            if (response.statusCode() == 200) {
                return JSON.std.beanFrom(Submission.class, response.body());
            } else {
                // If the response status code is not 200, throw an IOException with an error message.
                logger.warning("Failed to retrieve data for CIK " + cik + ". HTTP Status Code: " + response.statusCode());
            }
        } catch (Exception e) {
            // If an exception occurs during the request or response handling, throw an IOException with an error message.
            logger.warning("An error occurred while processing CIK " + cik);
        }

        return null;
    }

    /**
     * Retrieves the NPORT-P data for a specific CIK and accession number from the SEC Archives.
     *
     * @param cik            The Central Index Key (CIK) of the investment company.
     * @param accessionNumber The unique accession number associated with the NPORT-P filing.
     * @return An instance of the NPORT_P class representing the parsed NPORT-P data, or null if the request fails.
     */
    public static NPORT_P getNPORT_P(final String cik, final String accessionNumber) {
        try {
            // Create an HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_ARCHIEVES_URL + cik + "/" + accessionNumber + "/primary_doc.xml"))
                    .setHeader("User-Agent", USER_AGENT)
                    .build();

            // Send the HTTP request and retrieve the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is 200 (OK)
            if (response.statusCode() == 200) {
                XmlMapper xmlMapper = new XmlMapper();
                xmlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

                return xmlMapper.readValue(response.body(), NPORT_P.class);
            } else {
                // If the response status code is not 200, throw an IOException with an error message.
                logger.warning("Failed to retrieve data for CIK " + cik + " and Accession " + accessionNumber + ". HTTP Status Code: " + response.statusCode());
            }

        } catch (Exception e) {
            // If an exception occurs during the request or response handling, throw an IOException with an error message.
            logger.warning("An error occurred while processing CIK " + cik + "and Accession " + accessionNumber);
        }

        return null;
    }

    /**
     * Retrieves and maps accession numbers for a list of CIK numbers associated with form NPORT-P filings.
     *
     * @param cikList A list of Central Index Key (CIK) numbers for investment companies.
     * @return A map that associates each CIK number with a list of corresponding accession numbers for NPORT-P filings.
     */
    public static Map<Integer, List<String>> getAccessionNumbers(List<Integer> cikList) {
        // Stores accession numbers for later storing in database
        Map<Integer, List<String>> cikAccessionMap = new HashMap<>(cikList.size());

        for (Integer cik : cikList) {
            // Query submission
            Submission submissionData;
            if ((submissionData = getSubmission(String.valueOf(cik))) != null) {
                cikAccessionMap.put(cik, new LinkedList<>());

                // Get all accession numbers of form NPORT-P
                for (int i = 0; i < submissionData.getFilings().getRecent().getForm().size(); i++) {
                    if (submissionData.getFilings().getRecent().getForm().get(i).equalsIgnoreCase(FilingType.NPORT_P.getForm())) {
                        // Add accession number to map
                        cikAccessionMap.get(cik).add(submissionData.getFilings().getRecent().getAccessionNumber().get(i));
                    }
                }
            }
        }

        return cikAccessionMap;
    }

    private static String padCikToTenDigits(String cik) throws IllegalArgumentException {
        int cikLength = cik.length();

        if (cikLength < 10) {
            return "0".repeat(10 - cikLength) + cik;
        } else if (cikLength == 10) {
            return cik;
        } else {
            throw new IllegalArgumentException("CIK number should be 10 digits or less. Invalid param: " + cik);
        }
    }
}

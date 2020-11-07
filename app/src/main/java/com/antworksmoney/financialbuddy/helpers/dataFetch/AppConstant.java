package com.antworksmoney.financialbuddy.helpers.dataFetch;

import java.security.MessageDigest;

public class AppConstant {

    public static final int PERMISSION_LOCATION_REQUEST = 0x01;

    public static final int REQUEST_CHECK_SETTINGS_ATT = 0x02;

    public static final int PERMISSION_LOC_HOME = 0x03;

    public static final int PERMISSION_READ_PHONE_STATE = 0x04;

    public static final int PERMISSION_LOC_SIGN_IN = 0x05;

    public static final int SELECT_REGIONAL_DATA = 0x06;

    public static final int ADD_MONEY_ONLINE = 0x07;

    public static final int READ_EXTERNAL_STORAGE = 0x08;

    public static String BaseUrl = "https://antworksmoney.com/financial_buddy/";

    public static String BaseUrlBlogs = "http://estateahead.com/fbuddy/api/";

    public static String UatUrl = "Bbps_uat_api/";

    public static String BBPS_API = "Bbps_api/";

    public static String commonAPIUrl = "https://antworksp2p.com/p2papi/";

    public static String borrowerBaseUrl = "https://antworksp2p.com/p2papiborrower/";


    public static String Refercode = "https://play.google.com/store/apps/details?id=com.antworksmoney.financialbuddy&referrer=utm_source%3Dgothird%26utm_medium%3Dcpc%26anid%3Dadmob\"";

    public static final int MY_SOCKET_TIMEOUT_MS = 10000;

    public static final String NEW_MESSAGE_RECIEVED = "0";

    public static final String LOAN_STATUS_TRACKER = "loanStatus",
            LOAN_TYPE_OBJECT = "loanType",
            PROPOSAL_ID = "proposalId",
            LOAN_AMOUNT = "loanAmount",
            SELECTED_LOAN_TYPE = "selectedLoanType",
            OCCUPATION_ID = "occupationId",
            LOAN_DETAILS = "loanDetails",
            LOAN_NAME = "loanName",
            LOAN_AGREEMENT_OBJECT = "loanAgreementObject";

    public static String YouTubeApiKey = "AIzaSyBxxQ-znyHzpM2GYeOcgFPSFVbeH3Hsi-k";

    public static String getPasswordHash(String hsString) {
        String hashedData = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] data = md.digest(hsString.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i++) {
                sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedData = sb.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return hashedData;
    }
}

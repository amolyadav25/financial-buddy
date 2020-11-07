package com.antworksmoney.financialbuddy.views.fragments.Loan;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.PersonalLoanPOJO.LoanEligibilityModel;
import com.antworksmoney.financialbuddy.PersonalLoanPOJO.PersonalLoanEligibility;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.cashePOJO.ApplicantInformation;
import com.antworksmoney.financialbuddy.cashePOJO.CasheCreateCustomerResponse;
import com.antworksmoney.financialbuddy.cashePOJO.CashePojoRequest;
import com.antworksmoney.financialbuddy.cashePOJO.ContactInformation;
import com.antworksmoney.financialbuddy.cashePOJO.PersonalInformation;
import com.antworksmoney.financialbuddy.cashestatusupdatePOJO.CasheStatusUpdateRequest;
import com.antworksmoney.financialbuddy.cashestatusupdatePOJO.CasheStatusUpdateResponse;
import com.antworksmoney.financialbuddy.checksumPOJO.ChecksumPOJO;
import com.antworksmoney.financialbuddy.helpers.AllApiInterface;
import com.antworksmoney.financialbuddy.helpers.Entity.BankInfoEntity;
import com.antworksmoney.financialbuddy.helpers.Entity.LoanInfoEntity;
import com.antworksmoney.financialbuddy.helpers.adapters.BankOfferDataListAdapter;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.paysenseupdatePOJO.PaySenseUpdateRequest;
import com.antworksmoney.financialbuddy.paysenseupdatePOJO.PaySenseUpdateResponse;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.Loan.CasheURL.ApiClientCashe;
import com.antworksmoney.financialbuddy.views.fragments.Loan.CasheURL.ApiClientCheckSum;
import com.antworksmoney.financialbuddy.views.fragments.Loan.PaySenseURL.ApiClient;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoanOfferRequestFragment extends Fragment {

    private static LoanInfoEntity mLoanInfoEntity;

    public static LoanOfferRequestFragment newInstance(LoanInfoEntity loanInfoEntity) {
        mLoanInfoEntity = loanInfoEntity;
        return new LoanOfferRequestFragment();
    }

    private static final String TAG = "LoanOfferRequest";

    private ImageView toolBarIcon;

    private AppCompatActivity mActivity;

    private Context mContext;

    private ProgressBar dataLoaderProgressBar;

    private RecyclerView dataList;

    private JSONObject outerObject, innerObject;
    private JSONObject outerObject1, innerObject1;
    private RequestQueue dataObjectRequest;
    LinearLayout linearLayoutApprovedLimit;
Button buttonUploadDocument;
    private TextView offerHeading,textViewgetApprovedLimit,textViewApprovedLimit;
    WebView webViewURL;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loan_offer_request, container, false);

        toolBarIcon = rootView.findViewById(R.id.toolBarIcon);

        mActivity = (AppCompatActivity) getActivity();

        mContext = getContext();
        Log.e("Mytag","LoanOffer");
        dataLoaderProgressBar = rootView.findViewById(R.id.dataLoader);
        linearLayoutApprovedLimit = rootView.findViewById(R.id.linearLayoutApprovedLimit);
        webViewURL = rootView.findViewById(R.id.webViewURL);
        offerHeading = rootView.findViewById(R.id.offerHeading);
        textViewgetApprovedLimit = rootView.findViewById(R.id.textViewgetApprovedLimit);
        textViewApprovedLimit = rootView.findViewById(R.id.textViewApprovedLimit);
        dataList = rootView.findViewById(R.id.dataList);
        dataList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        dataList.setHasFixedSize(true);
        buttonUploadDocument = rootView.findViewById(R.id.buttonUploadDocument);
        outerObject = new JSONObject();

        innerObject = new JSONObject();
        innerObject1 = new JSONObject();
        outerObject1 = new JSONObject();
        toolBarIcon.setOnClickListener(view -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });

        dataObjectRequest = Volley.newRequestQueue(mContext);

        return rootView;
    }

//   public  String generateCheckSum(String data, String secretKey) throws java.security.SignatureException{
//       String result;
//       try {
//           Mac mac = Mac.getInstance("HmacSHA1");
//           SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
//           mac.init(key);
//           byte[] authentication = mac.doFinal(data.getBytes());
//           result = new String(android.util.Base64.encode(authentication,0));
//
//       } catch (Exception e) {
//           throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
//       }
//       return result;
//    }
//    public static String getChecksum(Serializable object) throws IOException, NoSuchAlgorithmException {
//        ByteArrayOutputStream baos = null;
//        ObjectOutputStream oos = null;
//        try {
//            baos = new ByteArrayOutputStream();
//            oos = new ObjectOutputStream(baos);
//            oos.writeObject(object);
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] thedigest = md.digest(baos.toByteArray());
//            return DatatypeConverter.printHexBinary(thedigest);
//        } finally {
//            oos.close();
//            baos.close();
//        }
//    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {

//            String date = "";
//
//            try {
//                date = mLoanInfoEntity.getDateOfBirth().split("/")[2]+"-"+
//                        mLoanInfoEntity.getDateOfBirth().split("/")[1]+"-"+
//                        mLoanInfoEntity.getDateOfBirth().split("/")[0];
//            } catch (Exception e){
//                e.printStackTrace();
//            }


            innerObject.put("loan_type", mLoanInfoEntity.getLoanType());
            innerObject.put("loan_name", mLoanInfoEntity.getLoanId());
            innerObject.put("state", mLoanInfoEntity.getState());
            innerObject.put("city", mLoanInfoEntity.getCity());
            innerObject.put("gender", mLoanInfoEntity.getGender());
            innerObject.put("dob", mLoanInfoEntity.getDateOfBirth());
            innerObject.put("maritalstatus", mLoanInfoEntity.getMaritalStatus());
            innerObject.put("nationality", mLoanInfoEntity.getNationality());
            innerObject.put("qualification", mLoanInfoEntity.getQualification());
            innerObject.put("occupation", mLoanInfoEntity.getOccupation());
            innerObject.put("company_type", mLoanInfoEntity.getCompanyType());
            innerObject.put("company_name", mLoanInfoEntity.getCompanyName());
            innerObject.put("mode_of_salary",mLoanInfoEntity.getSalaryProcess());
            innerObject.put("bankName",mLoanInfoEntity.getBankName());
            innerObject.put("emi",mLoanInfoEntity.getEMI());
            innerObject.put("income",mLoanInfoEntity.getSalary());
            innerObject.put("professionType",mLoanInfoEntity.getProfessionType());
            innerObject.put("netWorth",mLoanInfoEntity.getNetWorth());
            innerObject.put("totalExperience",mLoanInfoEntity.getTotalExperience());
            innerObject.put("turnover1",mLoanInfoEntity.getGrossTurnOver());
            innerObject.put("turnover2",mLoanInfoEntity.getGrossTurnOver2());
            innerObject.put("turnover3",mLoanInfoEntity.getGrossTurnOver3());
            innerObject.put("ownerShip",mLoanInfoEntity.getOfficeOwnerShip());
            innerObject.put("auditDone",mLoanInfoEntity.getAuditDone());
            innerObject.put("defaultedOnLoanCard",mLoanInfoEntity.getDefaultedOnLoan());
            innerObject.put("industryType",mLoanInfoEntity.getIndustryType());
            innerObject.put("officePhoneNumber",mLoanInfoEntity.getOfficePhoneNumber());
            innerObject.put("amount", mLoanInfoEntity.getLoanAmount());
            innerObject.put("property_type",mLoanInfoEntity.getPropertyType());
            innerObject.put("dateOfIncorporation",mLoanInfoEntity.getDateOfIncorporation());
            innerObject.put("CIN",mLoanInfoEntity.getCIN());
            innerObject.put("chequeBouncedInLastSixMonth",mLoanInfoEntity.getCheckBounced());
            innerObject.put("ratedByFinancialAgency",mLoanInfoEntity.getCompanyRatedByAgency());
            innerObject.put("cibil_score","");
            innerObject.put("property_value","");
            innerObject.put("location_of_property_pincode","");
            innerObject.put("builder_name","");
            outerObject.put("userData", innerObject);
            Log.e(TAG,outerObject.toString());
            innerObject1.put("phone","+91"+mLoanInfoEntity.getPhoneNumber());
            innerObject1.put("email",mLoanInfoEntity.getEmail());
            innerObject1.put("phone_verified",true);
            innerObject1.put("consumer_bureau_pull_consent",true);
            innerObject1.put("terms_accepted",true);

        } catch (Exception e) {
            e.printStackTrace();
        }
       paysenseApiCall();
   getOfferFromAPI();
   casheRegister();
    }

    private void casheRegister() {
        CashePojoRequest body = new CashePojoRequest();
        body.setPartnerName("AntWorks_Partner");
        // body.setReferenceId("3456789322");
        // body.setApplicantId("8977469922");
        body.setLoanAmount(mLoanInfoEntity.getLoanAmount());
        //    body.setProductTypeName("CASHe_180");
        ApplicantInformation applicantInformation = new ApplicantInformation();
        applicantInformation.setCompanyName(mLoanInfoEntity.getCompanyName());
        Log.e("Mytag","test1"+mLoanInfoEntity.getCompanyName());
        //applicantInformation.setDesignation(mLoanInfoEntity);
        applicantInformation.setEmploymentType(mLoanInfoEntity.getProfessionType());
        Log.e("Mytag","test2"+"Salaried");
        applicantInformation.setMonthlyIncome(mLoanInfoEntity.getSalary());
        Log.e("Mytag","test3"+"5000");
        //applicantInformation.setWorkSector("test");
        // applicantInformation.setJobFunction("terminal");
        // applicantInformation.setLandmarkOffice("test");
        //applicantInformation.setOfficeAddress1("test");
        //applicantInformation.setOfficeAddress2("test");
        //applicantInformation.setNumberOfYearsInCurrentWork("test");
        // applicantInformation.setWorkingSince("test");
        applicantInformation.setSalaryReceivedTypeId("1");
        // applicantInformation.setOfficeCity("Mumbai");
        //applicantInformation.setOfficePhoneNo("7042375619");
        // applicantInformation.setOrganization("antworks");
        //applicantInformation.setOfficialEmail("amol.yadav12@antworksmoney.com");
        // applicantInformation.setOfficeState("gurgaon");
        //applicantInformation.setOfficePincode("122018");
        PersonalInformation personalInformation = new PersonalInformation();
        personalInformation.setFirstName(mLoanInfoEntity.getName());
        Log.e("Mytag","test4"+mLoanInfoEntity.getName());
        personalInformation.setLastName(mLoanInfoEntity.getLast_name());
        Log.e("Mytag","test5"+mLoanInfoEntity.getLast_name());
        // personalInformation.setAadhaar("849981547801");
        personalInformation.setPAN(mLoanInfoEntity.getPan());
        Log.e("Mytag","test6"+mLoanInfoEntity.getPan());
        personalInformation.setPincode(mLoanInfoEntity.getPostalcode());
        Log.e("Mytag","test7"+mLoanInfoEntity.getPostalcode());
        // personalInformation.setAddressLine1("nashik");
        //personalInformation.setAddressLine2("nashik");
        personalInformation.setDOB(mLoanInfoEntity.getDateOfBirth());
        personalInformation.setGender(mLoanInfoEntity.getGender());
        Log.e("Mytag","test8"+mLoanInfoEntity.getDateOfBirth());
        personalInformation.setCity(mLoanInfoEntity.getCity());
        Log.e("Mytag","test9"+mLoanInfoEntity.getCity());
        personalInformation.setHighestQualification(mLoanInfoEntity.getQualification());
        Log.e("Mytag","test10"+mLoanInfoEntity.getQualification());
        //personalInformation.setLandmarkAddressLine3(mLoanInfoEntity.get);
        personalInformation.setMaritalStatus(mLoanInfoEntity.getMaritalStatus());
        Log.e("Mytag","test11"+mLoanInfoEntity.getMaritalStatus());
        // personalInformation.setNumberOfKids("1");
        // personalInformation.setResidingWith("Family");
        // personalInformation.setTypeOfAccommodation("Own");
        // personalInformation.setState("Maharastra");
        // personalInformation.setSpouseEmploymentStatus("Employed");
        // personalInformation.setNumberOfYearsAtCurrentAddress("gurgaon");
        // FinancialInformation financialInformation = new FinancialInformation();
        //financialInformation.setAccountNumber("32226152779");
        //financialInformation.setIFSCCode("SBIN0006333");
        //financialInformation.setPrimaryExistingBankName("State Bank Of India");
        // PartnerBankDetails partnerBankDetails = new PartnerBankDetails();
//        partnerBankDetails.setAccountNumber("32226152779");
//        partnerBankDetails.setIFSCCode("SBIN0006333");
//        partnerBankDetails.setPrimaryExistingBankName("State Bank Of India");
//        partnerBankDetails.setRemarks("panchvati");
        ContactInformation contactInformation = new ContactInformation();
        contactInformation.setEmailId(mLoanInfoEntity.getEmail());
        Log.e("Mytag","test12"+mLoanInfoEntity.getEmail());
        contactInformation.setMobile(mLoanInfoEntity.getPhoneNumber());
        Log.e("Mytag","test13"+mLoanInfoEntity.getPhoneNumber());
//        EKYCCustomer ekycCustomer = new EKYCCustomer();
//        ekycCustomer.setAadharNo("849981547808");
//        ekycCustomer.setCompressedAddress("nashik");
//        ekycCustomer.setDob("29-07-1978");
//        Log.e("Mytag","test14"+mLoanInfoEntity.getDateOfBirth());
//        ekycCustomer.setGender("Male");
//        ekycCustomer.setName(mLoanInfoEntity.getName());
//        Log.e("Mytag","test15"+mLoanInfoEntity.getName());
//        Poa poa = new Poa();
//        poa.setCo("S/O: Subir Samanta");
//        poa.setDist("Ranchi");
//        poa.setHouse("C-2/22");
//        poa.setLm("BIT");
//        poa.setPc("835215");
//        poa.setPo("Mesra");
//        poa.setState("Jharkhand");
//        poa.setVtc("Mesra");
//        poa.setSubdist("Kanke");
//        poa.setStreet("Middle Campus");
//        ekycCustomer.setPoa(poa);
        body.setApplicantInformation(applicantInformation);
        body.setContactInformation(contactInformation);
        // body.setFinancialInformation(financialInformation);
        // body.setEKYCCustomer(ekycCustomer);
        // body.setPartnerBankDetails(partnerBankDetails);
        body.setPersonalInformation(personalInformation);

        try {
            //   String checkSum =    calculateCheckSum(mapmain,"AntWorks@PRdC8$He$2o!#");
            // map.put("Content-Type", "application/json");
//            Log.e("Mytag","body1"+body.getApplicantId());
//            Log.e("Mytag","body2"+body.getLoanAmount());
//            Log.e("Mytag","body3"+body.getPartnerName());
//            Log.e("Mytag","body4"+body.getProductTypeName());
//            Log.e("Mytag","body5"+body.getReferenceId());
//            Log.e("Mytag","body6"+body.getApplicantInformation());
//            Log.e("Mytag","body7"+body.getContactInformation());
//            Log.e("Mytag","body8"+body.getEKYCCustomer());
//            Log.e("Mytag","body9"+body.getPartnerBankDetails());
//            Log.e("Mytag","body10"+body.getPersonalInformation());
//            Log.e("Mytag","body11"+body.getFinancialInformation());
//            Log.e("Mytag","body12"+body.getFinancialInformation().getAccountNumber());
//            Log.e("Mytag","body13"+body.getFinancialInformation().getIFSCCode());
//            Log.e("Mytag","body14"+body.getFinancialInformation().getPrimaryExistingBankName());
//            Log.e("Mytag","body15"+body.getPersonalInformation().getAddressLine2());
//            Log.e("Mytag","body16"+body.getPersonalInformation().getAddressLine1());
//            Log.e("Mytag","body17"+body.getPersonalInformation().getAadhaar());
//            Log.e("Mytag","body18"+body.getPersonalInformation().getCity());
//            Log.e("Mytag","body19"+body.getPersonalInformation().getDOB());
//            Log.e("Mytag","body20"+body.getPersonalInformation().getHighestQualification());
//            Log.e("Mytag","body21"+body.getPersonalInformation().getFirstName());
//            Log.e("Mytag","body22"+body.getPersonalInformation().getLastName());
//            Log.e("Mytag","body23"+body.getPersonalInformation().getMaritalStatus());
//            Log.e("Mytag","body24"+body.getPersonalInformation().getTypeOfAccommodation());
//            Log.e("Mytag","body25"+body.getPersonalInformation().getSpouseEmploymentStatus());
//            Log.e("Mytag","body26"+body.getPersonalInformation().getGender());
//            Log.e("Mytag","body27"+body.getPersonalInformation().getLandmarkAddressLine3());
//            Log.e("Mytag","body28"+body.getPersonalInformation().getState());
//            Log.e("Mytag","body29"+body.getPersonalInformation().getResidingWith());
//            Log.e("Mytag","body30"+body.getPersonalInformation().getNumberOfYearsAtCurrentAddress());
//            Log.e("Mytag","body31"+body.getEKYCCustomer().getAadharNo());
//            Log.e("Mytag","body32"+body.getEKYCCustomer().getCompressedAddress());
//            Log.e("Mytag","body33"+body.getEKYCCustomer().getDob());
//            Log.e("Mytag","body34"+body.getEKYCCustomer().getGender());
//            Log.e("Mytag","body35"+body.getEKYCCustomer().getName());
//            Log.e("Mytag","body36"+body.getEKYCCustomer().getPoa().getCo());
//            Log.e("Mytag","body37"+body.getEKYCCustomer().getPoa().getDist());
//            Log.e("Mytag","body38"+body.getEKYCCustomer().getPoa().getHouse());
//            Log.e("Mytag","body39"+body.getEKYCCustomer().getPoa().getLm());
//            Log.e("Mytag","body40"+body.getEKYCCustomer().getPoa().getState());
//            Log.e("Mytag","body41"+body.getEKYCCustomer().getPoa().getStreet());
//            Log.e("Mytag","body42"+body.getEKYCCustomer().getPoa().getSubdist());
//            Log.e("Mytag","body43"+body.getEKYCCustomer().getPoa().getVtc());
//            Log.e("Mytag","body44"+body.getEKYCCustomer().getPoa().getPc());
//            Log.e("Mytag","body45"+body.getEKYCCustomer().getPoa().getPo());
//            Log.e("Mytag","body46"+body.getContactInformation().getEmailId());
//            Log.e("Mytag","body47"+body.getContactInformation().getMobile());
//            Log.e("Mytag","body48"+body.getPartnerBankDetails().getAccountNumber());
//            Log.e("Mytag","body49"+body.getPartnerBankDetails().getIFSCCode());
//            Log.e("Mytag","body50"+body.getPartnerBankDetails().getPrimaryExistingBankName());
//            Log.e("Mytag","body51"+body.getApplicantInformation().getCompanyName());
//            Log.e("Mytag","body52"+body.getApplicantInformation().getDesignation());
//            Log.e("Mytag","body53"+body.getApplicantInformation().getEmploymentType());
//            Log.e("Mytag","body54"+body.getApplicantInformation().getLandmarkOffice());
//            Log.e("Mytag","body55"+body.getApplicantInformation().getMonthlyIncome());
//            Log.e("Mytag","body56"+body.getApplicantInformation().getNumberOfYearsInCurrentWork());
//            Log.e("Mytag","body57"+body.getApplicantInformation().getOfficeAddress1());
//            Log.e("Mytag","body58"+body.getApplicantInformation().getOfficeAddress2());
//            Log.e("Mytag","body59"+body.getApplicantInformation().getOfficeCity());
//            Log.e("Mytag","body60"+body.getApplicantInformation().getWorkSector());
//            Log.e("Mytag","body61"+body.getApplicantInformation().getWorkingSince());
//            Log.e("Mytag","body62"+body.getApplicantInformation().getOrganization());
//            Log.e("Mytag","body63"+body.getApplicantInformation().getOfficeState());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
//            Log.e("Mytag","body64"+body.getApplicantInformation().getJobFunction());
            Log.e("Mytag","AmolYYYYY");
            AllApiInterface apiService2 = ApiClientCheckSum.getClient().create(AllApiInterface.class);
            Call<ChecksumPOJO> call2 = apiService2.check_sum(body);
            call2.enqueue(new Callback<ChecksumPOJO>() {
                @Override
                public void onResponse(Call<ChecksumPOJO>call, Response<ChecksumPOJO> response) {
                    Log.e("Mytagc","response.code()"+response.code());
                    if (response.code() == 400 ) {
                        Log.d(TAG, "onResponse - Status : " + response.code());
                        Gson gson = new Gson();
                        // TypeAdapter<RegisterResponse> adapter = gson.getAdapter(RegisterResponse.class);
                        try {
                            if (response.errorBody() != null)
                                Log.e("Mytagc","response"+response.errorBody().string());
                            //   registerResponse =
                            //// adapter.fromJson(
                            //   response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else
                    {
                        Log.e("Mytagc","ResponseLoangetCheckSum"+response.body().getCheckSum());
                        AllApiInterface apiService = ApiClientCashe.getClient().create(AllApiInterface.class);
                        Call<CasheCreateCustomerResponse> call3 = apiService.responseCasheRegister(response.body().getCheckSum(),body);
                        call3.enqueue(new Callback<CasheCreateCustomerResponse>() {
                            @Override
                            public void onResponse(Call<CasheCreateCustomerResponse>call, Response<CasheCreateCustomerResponse> response) {
                                Log.e("Mytagc","esponse.code()"+response.code());
                                Log.e("Mytagc","bodyresponse"+response.body());
                                if (response.code() == 400 ) {
                                    Log.d(TAG, "onResponse - Status : " + response.code());
                                    Gson gson = new Gson();
                                    // TypeAdapter<RegisterResponse> adapter = gson.getAdapter(RegisterResponse.class);
                                    try {
                                        if (response.errorBody() != null)
                                            Log.e("Mytagc","response"+response.errorBody().string());

                                        //   registerResponse =
                                        //// adapter.fromJson(
                                        //   response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else
                                {
                                    if(response.body()!=null) {
                                        Log.e("Mytagc", "Response1" + response.body().getMessage());
                                        Log.e("Mytagc", "Response2" + response.body().getStatus());
                                        //Log.e("Mytagc","Response3"+response.body().getPayLoad());
                                        Log.e("Mytagc", "Response3" + response.body().getStatusCode());
                                        CasheStatusUpdateRequest cashePojoRequest = new CasheStatusUpdateRequest();
                                        cashePojoRequest.setMobile(body.getContactInformation().getMobile());

                                        AllApiInterface apiService = com.antworksmoney.financialbuddy.helpers.ApiClient.getClient().create(AllApiInterface.class);
                                        Call<CasheStatusUpdateResponse> call3 = apiService.updateCashe(cashePojoRequest);
                                        call3.enqueue(new Callback<CasheStatusUpdateResponse>() {
                                            @Override
                                            public void onResponse(Call<CasheStatusUpdateResponse> call, Response<CasheStatusUpdateResponse> response) {
                                                Log.e("Mytagc", "esponse.code()" + response.code());
                                                Log.e("Mytagc", "bodyresponse" + response.body());
                                                if (response.code() == 400) {
                                                    Log.d(TAG, "onResponse - Status : " + response.code());
                                                    Gson gson = new Gson();
                                                    // TypeAdapter<RegisterResponse> adapter = gson.getAdapter(RegisterResponse.class);
                                                    try {
                                                        if (response.errorBody() != null)
                                                            Log.e("Mytagc", "response" + response.errorBody().string());

                                                        //   registerResponse =
                                                        //// adapter.fromJson(
                                                        //   response.errorBody().string());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    Log.e("Mytagc", "Response1" + response.body().getMessage());
                                                    Log.e("Mytagc", "Response2" + response.body().getStatus());
                                                    //Log.e("Mytagc","Response3"+response.body().getPayLoad());
                                                    Log.e("Mytagc", "Response3" + response.body().getMessage());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<CasheStatusUpdateResponse> call, Throwable t) {
                                                // Log error here since request failed
                                                Log.e("Mytagc", "test22211" + t.toString());
                                            }
                                        });

                                    }


                                }
                            }
                            @Override
                            public void onFailure(Call<CasheCreateCustomerResponse> call, Throwable t) {
                                // Log error here since request failed
                                Log.e("Mytagc", "test22211"+t.toString());
                            }
                        });
                    }
                }
                @Override
                public void onFailure(Call<ChecksumPOJO> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("Mytagc", "test222"+t.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Mytagc", "test322"+e.getMessage());
        }
    }


    private void paysenseApiCall() {

        AllApiInterface apiService =
                ApiClient.getClient().create(AllApiInterface.class);
Log.e("Mytag","phonepaysenseApiCall"+mLoanInfoEntity.getPhoneNumber());
        Log.e("Mytag","emailpaysenseApiCall"+mLoanInfoEntity.getEmail());
        Log.e("Mytag","sessionidpaysenseApiCall"+mLoanInfoEntity.getX_sessionid());

        Map<String, String> map = new HashMap<>();
        map.put("x-ps-source-key",  "X21X4sbqpLDw8v6bdJBh4zlyevvrtemq");
        map.put("x-sessionid",  mLoanInfoEntity.getX_sessionid());
        LoanEligibilityModel body = new LoanEligibilityModel();
if(mLoanInfoEntity.getOccupation().equals("Salaried")) {
    body.setEmploymentType("salaried");
}else {
    body.setEmploymentType("self_employed");
}
        body.setFirstName(mLoanInfoEntity.getName());
        body.setLastName(mLoanInfoEntity.getLast_name());
      body.setDateOfBirth(mLoanInfoEntity.getDateOfBirth());
        body.setPan(mLoanInfoEntity.getPan());
        body.setGender(mLoanInfoEntity.getGender());
        body.setMonthlyIncome(mLoanInfoEntity.getSalary());
        body.setPostalCode(mLoanInfoEntity.getPostalcode());

        Call<PersonalLoanEligibility> call = apiService.personalloancheckeligibility(map,body);
        call.enqueue(new Callback<PersonalLoanEligibility>() {
            @Override
            public void onResponse(Call<PersonalLoanEligibility>call, Response<PersonalLoanEligibility> response) {
Log.e("Mytag","paysense"+response.code());


                if (response.code() == 400 ) {
                    Log.d(TAG, "onResponse - Status : " + response.code());
                    Gson gson = new Gson();
                    textViewApprovedLimit.setVisibility(View.GONE);
                        textViewgetApprovedLimit.setVisibility(View.GONE);
                        buttonUploadDocument.setVisibility(View.GONE);
                    linearLayoutApprovedLimit.setVisibility(View.GONE);

                   // TypeAdapter<RegisterResponse> adapter = gson.getAdapter(RegisterResponse.class);
                    try {
                        if (response.errorBody() != null)
                            Log.e("Mytag","responsepaysenseApiCall"+response.errorBody().string());

                        //   registerResponse =
                                   //// adapter.fromJson(
                                         //   response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {

                    if (response.body() != null) {
                        Log.e("Mytag", "response.code()paysenseApiCall" + response.code());
                        Log.e("Mytag","paysenseresponse"+response.body());
                        if (response.code() == 200 ) {
                            if(response.body().getDetailsRequired()!=null) {
                                mLoanInfoEntity.setUrl(response.body().getDetailsRequired().get(0).getUrl());

                            }
if(response.body().getApprovedLimit()!=null)
{
    textViewApprovedLimit.setVisibility(View.VISIBLE);
    textViewgetApprovedLimit.setVisibility(View.VISIBLE);
    buttonUploadDocument.setVisibility(View.VISIBLE);
    linearLayoutApprovedLimit.setVisibility(View.VISIBLE);
    textViewgetApprovedLimit.setText(String.format(response.body().getApprovedLimit().toString()));
    Log.e("Mytag","response.body().getApprovedLimit().toString()"+response.body().getApprovedLimit().toString());
    Log.e("Mytag","response.body().getIsDeclined().toString()"+response.body().getIsDeclined().toString());
    Log.e("Mytag","mLoanInfoEntity.getPhoneNumber()"+mLoanInfoEntity.getPhoneNumber());
    PaySenseUpdateRequest paySenseUpdateRequest = new PaySenseUpdateRequest();
    paySenseUpdateRequest.setApprovedLimit(response.body().getApprovedLimit().toString());
    paySenseUpdateRequest.setIsDeclined(response.body().getIsDeclined().toString());
    paySenseUpdateRequest.setMobile(mLoanInfoEntity.getPhoneNumber());
    AllApiInterface apiService = com.antworksmoney.financialbuddy.helpers.ApiClient.getClient().create(AllApiInterface.class);
    Call<PaySenseUpdateResponse> call3 = apiService.updatePaysense(paySenseUpdateRequest);
    call3.enqueue(new Callback<PaySenseUpdateResponse>() {
        @Override
        public void onResponse(Call<PaySenseUpdateResponse>call, Response<PaySenseUpdateResponse> response) {
            Log.e("Mytagffc","esponse.code()"+response.code());
            Log.e("Mytagffc","bodyresponse"+response.body().getMessage());

            if (response.code() == 400 ) {
                Log.d(TAG, "onResponse - Status : " + response.code());
                Gson gson = new Gson();
                // TypeAdapter<RegisterResponse> adapter = gson.getAdapter(RegisterResponse.class);
                try {
                    if (response.errorBody() != null)
                        Log.e("Mytag","response"+response.errorBody().string());

                    //   registerResponse =
                    //// adapter.fromJson(
                    //   response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else
            {
                Log.e("Mytag","Response1"+response.body().getMessage());
                Log.e("Mytag","Response2"+response.body().getStatus());
                //Log.e("Mytagc","Response3"+response.body().getPayLoad());
                Log.e("Mytag","Response3"+response.body().getMessage());
            }
        }
        @Override
        public void onFailure(Call<PaySenseUpdateResponse> call, Throwable t) {
            // Log error here since request failed
            Log.e("Mytag", "test22211"+t.toString());
        }
    });
}

                        }else
                        {
                            textViewgetApprovedLimit.setVisibility(View.GONE);
                            buttonUploadDocument.setVisibility(View.GONE);
                            textViewApprovedLimit.setVisibility(View.GONE);
                            linearLayoutApprovedLimit.setVisibility(View.GONE);

                        }


                        buttonUploadDocument.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FragmentTransaction transaction = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homeParent, PayUCreditFragment.newInstance(mLoanInfoEntity));
                                transaction.addToBackStack(null).commit();
                            }
                        });
                    }else
                    {

                    }
                }
            }
            @Override
            public void onFailure(Call<PersonalLoanEligibility> call, Throwable t) {
                // Log error here since request failed
                Log.e("Mytag", t.toString());
            }
        });
    }
    private void getOfferFromAPI() {

//        Log.e(TAG,AppConstant.BaseUrl + "loanoffer");

        JsonObjectRequest dataRequest = new JsonObjectRequest(
                Request.Method.POST,
                AppConstant.BaseUrl + "loanoffer",
                outerObject,
                response -> {
                    Log.e(TAG, response.toString());
                    dataLoaderProgressBar.setVisibility(View.GONE);

                    ArrayList<BankInfoEntity> entityArrayList = new ArrayList<>();

                    try {
                        JSONArray dataArray = response.getJSONArray("UserData");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = (JSONObject) response.getJSONArray("UserData").get(i);
                            BankInfoEntity entity = new BankInfoEntity();
                                    entity.setLoanid(dataObject.getString("loanid"));
                                    entity.setBank_id(dataObject.getString("bank_id"));
                                    entity.setMin_age(dataObject.getString("min_age"));
                                    entity.setMax_age(dataObject.getString("max_age"));
                                    entity.setGender(dataObject.getString("gender"));
                                    entity.setEmployement_status(dataObject.getString("employement_status"));
                                    entity.setMin_salary(dataObject.getString("min_salary"));
                                    entity.setMin_average_income(dataObject.getString("min-average_income"));
                                    entity.setMin_loan_amount(dataObject.getString("loan_amount_calculate"));
                                    entity.setMax_loan_amount(dataObject.getString("max_loan_amount"));
                                    entity.setProcessing_fee_type(dataObject.getString("processing_fee_type"));
                                    entity.setFixed_processing_fee(dataObject.getString("fixed_processing_fee"));
                                    entity.setMin_processing_fee(dataObject.getString("full_emi_calulate"));
                                    entity.setMax_processing_fee(dataObject.getString("max_processing_fee"));
                                    entity.setInr_rate_fixed(dataObject.getString("inr_rate_fixed"));
                                    entity.setInt_fixed_amount(dataObject.getString("int_fixed_amount"));
                                    entity.setMin_int_rate(dataObject.getString("min_int_rate"));
                                    entity.setMax_int_rate(dataObject.getString("max_int_rate"));
                                    entity.setTenure_month_start(dataObject.getString("tenor_calculate"));
                                    entity.setTenure_month_end(dataObject.getString("tenure_month_end"));
                                    entity.setMinimum_cibil(dataObject.getString("minimum_cibil"));
                                    entity.setMin_residence_yrs(dataObject.getString("min_residence_yrs"));
                                    entity.setMin_employement_yrs(dataObject.getString("min_employement_yrs"));
                                    entity.setNationality(dataObject.getString("nationality"));
                                    entity.setCompany_id(dataObject.getString("company_id"));
                                    entity.setLoan_type_id(dataObject.getString("loan_type_id"));
                                    entity.setCredit_card_type(dataObject.getString("credit_card_type"));
                                    entity.setCredit_card_image(dataObject.getString("credit_card_image"));
                                    entity.setLoan_description(dataObject.getString("loan_description"));
                                    entity.setF1(dataObject.getString("f1"));
                                    entity.setF2(dataObject.getString("f2"));
                                    entity.setF3(dataObject.getString("f3"));
                                    entity.setF4(dataObject.getString("f4"));
                                    entity.setF5(dataObject.getString("f5"));
                                    entity.setStatus(dataObject.getString("status"));
                                    entity.setDate_added(dataObject.getString("date_added"));
                                    entity.setDate_modified(dataObject.getString("date_modified"));
                                    entity.setFor_crm("");
                                    entity.setName(dataObject.getString("name"));
                                    entity.setImage(dataObject.getString("image"));
                                    entity.setAbout(dataObject.getString("about"));
                                    entity.setAdded_on(dataObject.getString("added_on"));

                                    entityArrayList.add(entity);
                        }

                        BankOfferDataListAdapter adapter = new BankOfferDataListAdapter(mContext,entityArrayList,dataList);
                        dataList.setAdapter(adapter);

                        adapter.setOnClick(new BankOfferDataListAdapter.OnItemClicked() {
                            @Override
                            public void onItemClick(int position) {
                                FragmentTransaction transaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homeParent,LoanVerifyDetailsFragment.newInstance(entityArrayList.get(position),mLoanInfoEntity));
                                transaction.addToBackStack(null).commit();

                            }
                        });


                    } catch (Exception e) {
                        try {
                            offerHeading.setText(response.getJSONObject("data").getString("message"));
                            offerHeading.setTextSize(16);
                            offerHeading.setGravity(Gravity.CENTER);
                        }catch (Exception e1){
                            e1.printStackTrace();
                        }
                        e.printStackTrace();

                    }
                },
                error -> {
                    dataLoaderProgressBar.setVisibility(View.GONE);
                    Log.e(TAG, error.toString());
                });

        dataRequest.setShouldCache(false);
        dataRequest.setRetryPolicy(new DefaultRetryPolicy(
                AppConstant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        dataObjectRequest.add(dataRequest);
    }
}

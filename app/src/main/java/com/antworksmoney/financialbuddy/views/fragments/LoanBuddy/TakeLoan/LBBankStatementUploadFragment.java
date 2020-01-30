package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import uk.me.hardill.volley.multipart.MultipartRequest;

import static android.app.Activity.RESULT_OK;


public class LBBankStatementUploadFragment extends Fragment {

    public LBBankStatementUploadFragment() {
        // Required empty public constructor
    }

    public static LBBankStatementUploadFragment newInstance() {
        return new LBBankStatementUploadFragment();
    }

    private TextView et_user_account;

    private EditText et_user_password;

    private Button selectInvoiceButton, ProceedButton;

    private final int PICK_IMAGE = 510;

    private File selectedFile;

    private CoordinatorLayout snackBarView;

    private RelativeLayout progress_bar;

    private Button skipButton;

    private static final String TAG = "LBBankStatementUploadFr";

    private SharedPreferences mSharedPreferences;

    private Toolbar mToolbar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lbbank_statement_upload, container, false);

        et_user_account = rootView.findViewById(R.id.et_user_account);

        et_user_password = rootView.findViewById(R.id.et_user_password);

        selectInvoiceButton = rootView.findViewById(R.id.selectInvoiceButton);

        ProceedButton = rootView.findViewById(R.id.ProceedButton);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        skipButton = rootView.findViewById(R.id.skipButton);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        ProceedButton.setOnClickListener(view -> {
            if (et_user_account.getText().toString().trim().equalsIgnoreCase("")) {
                et_user_account.setError("Select File !!");
            } else {
              saveBankStatement();
            }
        });

        skipButton.setOnClickListener(view -> {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.homeParent, LBWaitingForApprovalFragment.newInstance());
            transaction.addToBackStack(null).commit();
        });

        selectInvoiceButton.setOnClickListener(view -> {
            checkStoragePermission();
        });

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });


        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("15")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "14");
            editor.apply();
        }

        return rootView;
    }

    private void checkStoragePermission() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                requestPermissions(permissions, AppConstant.READ_EXTERNAL_STORAGE);
            } else {
                openFileManager();
            }
        } else {
            openFileManager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstant.READ_EXTERNAL_STORAGE) {
            if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileManager();
            } else {
                showSnackBar("Can't proceed without phone state !! -> Application Settings", R.color.red);
            }
        }
    }

    private void openFileManager() {
        Intent intent = new Intent(getContext(), FileChooser.class);
        intent.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal());
        intent.putExtra(Constants.ALLOWED_FILE_EXTENSIONS,  "pdf;");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (resultCode == RESULT_OK) {
                et_user_account.setText(data.getData().getPath());
                selectedFile = new File(data.getData().getPath());
            }
        }
    }

    private void saveBankStatement() {

        progress_bar.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("Authorization", mSharedPreferences.getString("loginToken",""));


        MultipartRequest request = new MultipartRequest(
                AppConstant.borrowerBaseUrl+ "borrowerres/bankStatement",
                params,
                response -> {
                    progress_bar.setVisibility(View.GONE);

                    try {

                        JSONObject responseObject = new JSONObject(new String(
                                response.data, HttpHeaderParser.parseCharset(response.headers)));

                        Log.e(TAG, responseObject.toString());

                        if (responseObject.getString("status").trim().equalsIgnoreCase("1")) {

                            showSnackBar("Bank Statement added successfully !!", R.color.green);
                        } else {
                            showSnackBar("Failed to upload Bank Statement !!", android.R.color.holo_red_dark);
                        }

                    } catch (Exception e) {

                        showSnackBar("Failed to upload Bank Statement !!", android.R.color.holo_red_dark);
                        e.printStackTrace();
                    }
                },
                error -> {
                    progress_bar.setVisibility(View.GONE);
                    showSnackBar("Failed to upload Bank Statement !!", android.R.color.holo_red_dark);
                    Log.e(TAG, error.toString());
                });

        request.addPart(new MultipartRequest.FormPart("password",
                et_user_password.getText().toString().trim()));

        request.addPart(new MultipartRequest.FilePart("bank_statement",
                getMimeType(et_user_account.getText().toString().trim()),
                et_user_account.getText().toString().trim(),
                convertFileToBytes(selectedFile)));

        Volley.newRequestQueue(getContext()).add(request);

    }

    private byte[] convertFileToBytes(File requestedUserFile) {
        byte[] b = new byte[(int) requestedUserFile.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(requestedUserFile);
            fileInputStream.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return b;
    }

    private static String getMimeType(String url) {
        String type = null;
        String extension = null;
        int i = url.lastIndexOf('.');
        if (i > 0)
            extension = url.substring(i + 1);
        if (extension != null)
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        return type;
    }



    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green) {

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBWaitingForApprovalFragment.newInstance());
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }

}

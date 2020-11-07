package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.Documents;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.aditya.filebrowser.Constants;
import com.aditya.filebrowser.FileChooser;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import uk.me.hardill.volley.multipart.MultipartRequest;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;


public class LBDocumentsUploadFragment extends Fragment {

    public LBDocumentsUploadFragment() {
        // Required empty public constructor
    }

    public static LBDocumentsUploadFragment newInstance() {
        return new LBDocumentsUploadFragment();
    }

    private Toolbar mToolbar;

    private TextView et_user_pan_card;

    private Button choosePANDocButton, ProceedButton;

    private final int CAMERA_REQUEST = 500;

    private CoordinatorLayout snackBarView;

    private RelativeLayout progress_bar;

    private SharedPreferences mSharedPreferences;

    private File documentFile;

    private static final String TAG = "LBKYCUploadFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lb_documents_upload, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        ProceedButton = rootView.findViewById(R.id.ProceedButton);

        et_user_pan_card = rootView.findViewById(R.id.et_user_pan_card);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        choosePANDocButton = rootView.findViewById(R.id.choosePANDocButton);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });


        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        choosePANDocButton.setOnClickListener(view1 -> {
            checkStoragePermission();
        });


        ProceedButton.setOnClickListener(view13 -> {
            if (et_user_pan_card.getText().toString().trim().equalsIgnoreCase("")) {
                et_user_pan_card.setError("Invalid Document Image !!");
                et_user_pan_card.requestFocus();
            } else {
                updateDocumentDetailsToServer();
            }
        });
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
        intent.putExtra(Constants.ALLOWED_FILE_EXTENSIONS, "png;jpg;jpeg");
        startActivityForResult(intent, CAMERA_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    et_user_pan_card.setText(data.getData().getPath());
                    documentFile = new File(data.getData().getPath());
                }

                break;

        }
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


    private void updateDocumentDetailsToServer() {

        progress_bar.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("Authorization", mSharedPreferences.getString("loginToken", ""));

//        Log.e(TAG,  mSharedPreferences.getString("loginToken",""));

        MultipartRequest request = new MultipartRequest(
                AppConstant.borrowerBaseUrl + "borrowerres/documentUpload",
                params,
                response -> {
                    progress_bar.setVisibility(View.GONE);

                    try {

                        JSONObject responseObject = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));

                        Log.e(TAG, responseObject.toString());

                        if (responseObject.getString("status").trim().equalsIgnoreCase("1")) {

                            showSnackBar("Document Uploaded successfully !!", R.color.green);
                        } else {
                            showSnackBar("Failed to upload document !!", android.R.color.holo_red_dark);
                        }

                    } catch (Exception e) {

                        showSnackBar("Failed to upload document !!", android.R.color.holo_red_dark);

                        e.printStackTrace();
                    }
                },
                error -> {
                    progress_bar.setVisibility(View.GONE);
                    showSnackBar("Failed to upload document  !!", android.R.color.holo_red_dark);
                    Log.e(TAG, error.toString());
                });

        request.addPart(new MultipartRequest.FormPart("document_type", "pan"));

        request.addPart(new MultipartRequest.FilePart("document",
                getMimeType(et_user_pan_card.getText().toString().trim()),
                et_user_pan_card.getText().toString().trim(),
                convertFileToBytes(documentFile)));


        Volley.newRequestQueue(getContext()).add(request);

    }


    private void showSnackBar(String message, int backgroundColor) {
        final Snackbar snackbar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getContext().getResources().getColor(backgroundColor));
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (getActivity() != null && backgroundColor == R.color.green) {
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                }
            }
        });
        snackbar.show();
    }

}

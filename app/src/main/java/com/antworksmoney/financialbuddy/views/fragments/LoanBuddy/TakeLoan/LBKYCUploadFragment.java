package com.antworksmoney.financialbuddy.views.fragments.LoanBuddy.TakeLoan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import uk.me.hardill.volley.multipart.MultipartRequest;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;


public class LBKYCUploadFragment extends Fragment {

    public LBKYCUploadFragment() {
        // Required empty public constructor
    }

    public static LBKYCUploadFragment newInstance() {
        return new LBKYCUploadFragment();
    }

    private Toolbar mToolbar;

    private TextView et_user_pan_card, et_user_address;

    private TextView pictureName;

    private Button chooseAddressDocButton, choosePANDocButton, ProceedButton;

    private ImageView selectSelfieButton;

    private final int CAMERA_REQUEST = 500, PICK_IMAGE = 510;

    private CoordinatorLayout snackBarView;

    private RelativeLayout progress_bar;

    private SharedPreferences mSharedPreferences;

    private File selfieImage, panImage, addressImage;

    private String selectedFileField = "";

    private static final String TAG = "LBKYCUploadFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_lbkycupload, container, false);

        mToolbar = rootView.findViewById(R.id.mToolbar);

        pictureName = rootView.findViewById(R.id.pictureName);

        chooseAddressDocButton = rootView.findViewById(R.id.chooseAddressDocButton);

        ProceedButton = rootView.findViewById(R.id.ProceedButton);

        selectSelfieButton = rootView.findViewById(R.id.selectSelfieButton);

        et_user_pan_card = rootView.findViewById(R.id.et_user_pan_card);

        snackBarView = rootView.findViewById(R.id.snackBarView);

        progress_bar = rootView.findViewById(R.id.progress_bar);

        et_user_address = rootView.findViewById(R.id.et_user_address);

        choosePANDocButton = rootView.findViewById(R.id.choosePANDocButton);

        mSharedPreferences = getActivity().getSharedPreferences("PersonalDetails", Context.MODE_PRIVATE);

        mToolbar.setNavigationOnClickListener(v -> {
            if (getActivity() != null) {
                ((HomeActivity) getActivity())
                        .getmDrawerLayout()
                        .openDrawer(GravityCompat.START);
            }
        });


        if (!mSharedPreferences.getString(AppConstant.LOAN_STATUS_TRACKER, "").trim().equalsIgnoreCase("12")) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.LOAN_STATUS_TRACKER, "11");
            editor.apply();
        }

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        chooseAddressDocButton.setOnClickListener(view1 -> {
            selectedFileField = "ADDRESS";
            checkStoragePermission();
        });

        choosePANDocButton.setOnClickListener(view14 -> {
            selectedFileField = "PAN";
            checkStoragePermission();
        });


        selectSelfieButton.setOnClickListener(view12 -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });


        ProceedButton.setOnClickListener(view13 -> {
            if (et_user_pan_card.getText().toString().trim().equalsIgnoreCase("")) {
                et_user_pan_card.setError("Invalid PAN Image !!");
                et_user_pan_card.requestFocus();
            } else if (et_user_address.getText().toString().trim().equalsIgnoreCase("")) {
                et_user_address.setError("Invalid Address Proof Image !!");
                et_user_address.requestFocus();
            } else if (pictureName.getText().toString().trim().equalsIgnoreCase("")) {
                pictureName.setError("No File Selected !!");
                pictureName.requestFocus();
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
        intent.putExtra(Constants.ALLOWED_FILE_EXTENSIONS,  "png;jpg;jpeg");
        startActivityForResult(intent, PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {

                    selfieImage = createImageFile();
                    if (selfieImage != null) {
                        FileOutputStream fileOutputStream;
                        try {
                            fileOutputStream = new FileOutputStream(selfieImage);
                            ((Bitmap) data.getExtras().get("data")).compress(
                                    Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                            fileOutputStream.flush();
                            pictureName.setText(selfieImage.getAbsolutePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        showSnackBar("Failed to get Image !!!", R.color.red);
                    }
                }
                break;

            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (selectedFileField.trim().equalsIgnoreCase("PAN")) {
                        et_user_pan_card.setText(data.getData().getPath());
                        panImage = new File(data.getData().getPath());
                    } else {
                        et_user_address.setText(data.getData().getPath());
                        addressImage = new File(data.getData().getPath());
                    }

                }
                break;

        }
    }


    public File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File mFileTemp = null;
        String root = getContext().getDir("my_sub_dir", Context.MODE_PRIVATE).getAbsolutePath();
        File myDir = new File(root + "/Img");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        try {
            mFileTemp = File.createTempFile(imageFileName, ".png", myDir.getAbsoluteFile());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return mFileTemp;
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

        Log.e(TAG, mSharedPreferences.getString("loginToken", ""));

        MultipartRequest request = new MultipartRequest(
                AppConstant.borrowerBaseUrl + "borrowerres/updateKyc",
                params,
                response -> {
                    progress_bar.setVisibility(View.GONE);

                    try {

                        JSONObject responseObject = new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));

                        Log.e(TAG, responseObject.toString());

                        if (responseObject.getString("status").trim().equalsIgnoreCase("1")) {

                            showSnackBar("KYC added successfully !!", R.color.green);
                        } else {
                            showSnackBar("Failed to update KYC !!", android.R.color.holo_red_dark);
                        }

                    } catch (Exception e) {

                        showSnackBar("Failed to update KYC !!", android.R.color.holo_red_dark);

                        e.printStackTrace();
                    }
                },
                error -> {
                    progress_bar.setVisibility(View.GONE);
                    showSnackBar("Failed to upload invoice !!", android.R.color.holo_red_dark);
                    Log.e(TAG, error.toString());
                });

        request.addPart(new MultipartRequest.FilePart("pan_file",
                getMimeType(et_user_pan_card.getText().toString().trim()),
                et_user_pan_card.getText().toString().trim(),
                convertFileToBytes(panImage)));

        request.addPart(new MultipartRequest.FilePart("docs_type",
                getMimeType(et_user_address.getText().toString().trim()),
                et_user_address.getText().toString().trim(),
                convertFileToBytes(addressImage)));

        request.addPart(new MultipartRequest.FilePart("selfiImage",
                getMimeType(pictureName.getText().toString().trim()),
                pictureName.getText().toString().trim(),
                convertFileToBytes(selfieImage)));

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
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.homeParent, LBCoBorrowerFragment.newInstance(true));
                    transaction.addToBackStack(null).commit();
                }
            }
        });
        snackbar.show();
    }

}

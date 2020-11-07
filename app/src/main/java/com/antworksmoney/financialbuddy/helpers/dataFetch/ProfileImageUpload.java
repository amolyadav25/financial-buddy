package com.antworksmoney.financialbuddy.helpers.dataFetch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import static android.content.Context.MODE_PRIVATE;

public class ProfileImageUpload extends AsyncTask {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;

    private static final String TAG = "ProfileImageUpload";

    Bitmap image;

    String phoneNumber;

    SharedPreferences preferences;

//    int notificationID = 100;
//    NotificationManager notificationManager;

    public ProfileImageUpload(Bitmap image, Context context, String number) {
        this.image = image;
        this.mContext = context;
        this.phoneNumber = number;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification.Builder notificationBuilder = new Notification.Builder(mContext);
//        notificationBuilder.setOngoing(true)
//                .setContentTitle("Uploading Image to server")
//                .setContentText("We are uploading your image to the server. Please wait....")
//                .setProgress(100, 0, false)
//                .setSmallIcon(R.drawable.applogo);
//
//        Notification notification = notificationBuilder.build();
//        notificationManager.notify(notificationID, notification);
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        preferences = mContext.getSharedPreferences("PersonalDetails", MODE_PRIVATE);

        if (image != null) {
            try {
                Log.e(TAG, "reached here");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                JSONObject innerObject = new JSONObject();
                innerObject.put("image", imageString);
                innerObject.put("contact", phoneNumber);

                JSONObject dataobject = new JSONObject();
                dataobject.put("userData", innerObject);

                JsonObjectRequest objectRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        AppConstant.BaseUrl + "update-image",
                        dataobject,
                        response -> {
                            try {

                                Log.e(TAG, "amol"+response.toString());

                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("user_image_url", response.getJSONObject("data")
                                        .getJSONObject("userData")
                                        .getString("profile_image_url"));
                                Log.e("Mytag","userimage"+response.getJSONObject("data")
                                        .getJSONObject("userData")
                                        .getString("profile_image_url"));
                                editor.apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        },
                        error -> Log.e(TAG, error.toString()));

                objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                        AppConstant.MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                RequestQueue rQueue = Volley.newRequestQueue(mContext);
                rQueue.add(objectRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

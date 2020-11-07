package com.antworksmoney.financialbuddy.helpers.messaging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.helpers.AllApiInterface;
import com.antworksmoney.financialbuddy.helpers.Database.Db_Helper;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.antworksmoney.financialbuddy.updateTokenPOJO.updateTokenPOJO;
import com.antworksmoney.financialbuddy.updateTokenPOJO.uploadTokenRequest;
import com.antworksmoney.financialbuddy.views.activities.HomeActivity;
import com.antworksmoney.financialbuddy.views.fragments.Loan.PaySenseURL.ApiClient;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";
    private NotificationChannel mChannel = null;
    private Db_Helper mDataBaseObject;
    private SharedPreferences mSharedPreferences;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "onMessageReceived");
        Log.e(TAG, "remoteMessage.getFrom()"+remoteMessage.getFrom());
        Log.e(TAG, "remoteMessage.getData()"+remoteMessage.getData());
        Map<String, String> data = remoteMessage.getData();
        Log.e(TAG, "remoteMessage.getData()title"+data.get("title"));
        Log.e(TAG, "remoteMessage.getData()body"+data.get("body"));
        Log.e(TAG, "remoteMessage.getData()click_action"+data.get("click_action"));
      //  sendNotification(remoteMessage.getNotification(), data);
        Log.e(TAG, "getNotification"+remoteMessage.getNotification().getTitle());
        Log.e(TAG, "Message Received title1"+remoteMessage.getData().get("title"));
        Log.e(TAG, "Message Received message1"+remoteMessage.getData().get("message"));
        Log.e(TAG, "Message Received page_name"+remoteMessage.getData().get("click_action"));
        Log.e(TAG,"title"+ remoteMessage.getNotification().getTitle());


//        Intent seeDetails = new Intent(getApplicationContext(), HomeActivity.class);
//        seeDetails.putExtra("menuFragment", remoteMessage.getData().get("page_name"));
//        seeDetails.putExtra("title", remoteMessage.getData().get("title"));
//        seeDetails.putExtra("message", remoteMessage.getData().get("message"));
//        seeDetails.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, seeDetails,
//                PendingIntent.FLAG_ONE_SHOT);
//        int notificationId = new Random().nextInt(60000);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(remoteMessage.getData().get("title"))
//                .setContentText(remoteMessage.getData().get("message"))
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
        try {
            Intent seeDetails = new Intent(getApplicationContext(), HomeActivity.class);
         //   seeDetails.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mDataBaseObject = new Db_Helper(getApplicationContext());
            mSharedPreferences = getSharedPreferences("PersonalDetails", MODE_PRIVATE);
            Log.e("Amol","click_action"+remoteMessage.getData().get("click_action"));
            Log.e("Amol","title"+ remoteMessage.getNotification().getTitle());
            Log.e("Amol","body"+ remoteMessage.getNotification().getBody());
            seeDetails.putExtra("click_action",remoteMessage.getData().get("click_action"));
        seeDetails.putExtra("title", remoteMessage.getNotification().getTitle());
        seeDetails.putExtra("message", remoteMessage.getNotification().getBody());
            PendingIntent pendingIntentVerify = PendingIntent.getActivity(
                    getApplicationContext(),
                    R.string.default_notification_channel_id,
                    seeDetails,
                    PendingIntent.FLAG_ONE_SHOT);


          //  Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("image-url"));

//            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
//                    .bigPicture(bitmap)
//                    .setSummaryText(remoteMessage.getData().get("summary"));

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Log.e(TAG, "Message Received");

            mDataBaseObject.insertNotificationData("0",
                    remoteMessage.getNotification().getBody(),
                    remoteMessage.getData().get("click_action"),
                    String.valueOf(Calendar.getInstance().getTime()),
                    remoteMessage.getNotification().getTitle());

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(AppConstant.NEW_MESSAGE_RECIEVED,"1");
            editor.apply();

//            if (HomeFragment.newNotificationText != null){
//                new Handler(Looper.getMainLooper()).post(() ->
//                        HomeFragment.newNotificationText.setVisibility(View.VISIBLE));
//            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationCompat.Builder builder;

                if (mChannel == null) {
                    mChannel = new NotificationChannel(
                            getString(R.string.default_notification_channel_id),
                            remoteMessage.getNotification().getTitle(),
                            NotificationManager.IMPORTANCE_HIGH);

                    Log.e(TAG, "Channel Null");

                    mChannel.setDescription(remoteMessage.getNotification().getBody());
                    mChannel.enableVibration(true);
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(mChannel);
                    }
                }

                Log.e(TAG, "Message Received");

                builder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_notification_channel_id));
                builder.setContentTitle(remoteMessage.getNotification().getTitle())
                        .setSmallIcon(R.drawable.app_logo_square)
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setLargeIcon( BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.app_logo_square))
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                        .addAction(R.drawable.ic_arrow_drop_down_grey_24dp, "APPLY NOW !!", pendingIntentVerify)
                        .setSound(RingtoneManager.getDefaultUri(Notification.DEFAULT_SOUND));
                       // .setStyle(style);
                Notification notification = builder.build();

                if (notificationManager != null) {
                    notificationManager.notify(R.string.default_notification_channel_id, notification);
                }
            } else {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setAutoCancel(false)
                       // .setStyle(style)
                        .setSmallIcon(R.drawable.app_logo_square)
                        .setLargeIcon( BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.app_logo_square))
                        .addAction(R.drawable.ic_arrow_drop_down_grey_24dp, "APPLY NOW !!", pendingIntentVerify)
                        .setSound(defaultSoundUri);
                if (notificationManager != null) {
                    notificationManager.notify(R.string.default_notification_channel_id, notificationBuilder.build());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//    }
//    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
//        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
//                .setContentTitle(notification.getTitle())
//                .setContentText(notification.getBody())
//                .setAutoCancel(true)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setContentIntent(pendingIntent)
//                .setContentInfo(notification.getTitle())
//                .setLargeIcon(icon)
//                .setColor(Color.RED)
//                .setLights(Color.RED, 1000, 300)
//                .setDefaults(Notification.DEFAULT_VIBRATE)
//                .setSmallIcon(R.mipmap.ic_launcher);
//
//        try {
//            String picture_url = data.get("picture_url");
//            if (picture_url != null && !"".equals(picture_url)) {
//                URL url = new URL(picture_url);
//                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                notificationBuilder.setStyle(
//                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
//                );
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Notification Channel is required for Android O and above
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    "channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
//            );
//            channel.setDescription("channel description");
//            channel.setShowBadge(true);
//            channel.canShowBadge();
//            channel.enableLights(true);
//            channel.setLightColor(Color.RED);
//            channel.enableVibration(true);
//            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(0, notificationBuilder.build());

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e("Amol", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e("aml", "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e("amolll", "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.e("amolll", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();
        // [END dispatch_job]
    }
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

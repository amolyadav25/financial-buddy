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
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.antworksmoney.financialbuddy.helpers.Database.Db_Helper;
import com.antworksmoney.financialbuddy.helpers.dataFetch.AppConstant;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.antworksmoney.financialbuddy.R;
import com.antworksmoney.financialbuddy.views.activities.SplashActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingServ";
    private NotificationChannel mChannel = null;
    private Db_Helper mDataBaseObject;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "Message Received");
        try {
            Intent seeDetails = new Intent(getApplicationContext(), SplashActivity.class);
            seeDetails.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mDataBaseObject = new Db_Helper(getApplicationContext());
            mSharedPreferences = getSharedPreferences("PersonalDetails", MODE_PRIVATE);

            PendingIntent pendingIntentVerify = PendingIntent.getActivity(
                    getApplicationContext(),
                    R.string.default_notification_channel_id,
                    seeDetails,
                    PendingIntent.FLAG_ONE_SHOT);


            Bitmap bitmap = getBitmapfromUrl(remoteMessage.getData().get("image-url"));

            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .setSummaryText(remoteMessage.getData().get("summary"));

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Log.e(TAG, "Message Received");

            mDataBaseObject.insertNotificationData("0",
                    remoteMessage.getData().get("image-url"),
                    remoteMessage.getData().get("action-url"),
                    String.valueOf(Calendar.getInstance().getTime()),
                    remoteMessage.getData().get("title"));

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
                            remoteMessage.getData().get("title"),
                            NotificationManager.IMPORTANCE_HIGH);

                    Log.e(TAG, "Channel Null");

                    mChannel.setDescription(remoteMessage.getData().get("message"));
                    mChannel.enableVibration(true);
                    if (notificationManager != null) {
                        notificationManager.createNotificationChannel(mChannel);
                    }
                }

                Log.e(TAG, "Message Received");

                builder = new NotificationCompat.Builder(getApplicationContext(), getString(R.string.default_notification_channel_id));
                builder.setContentTitle(remoteMessage.getData().get("title"))
                        .setSmallIcon(R.drawable.app_logo_square)
                        .setContentText(remoteMessage.getData().get("message"))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setAutoCancel(true)
                        .setLargeIcon( BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.app_logo_square))
                        .setBadgeIconType(R.drawable.app_logo_square)
                        .addAction(R.drawable.ic_arrow_drop_down_grey_24dp, "APPLY NOW !!", pendingIntentVerify)
                        .setSound(RingtoneManager.getDefaultUri(Notification.DEFAULT_SOUND))
                        .setStyle(style);
                Notification notification = builder.build();

                if (notificationManager != null) {
                    notificationManager.notify(R.string.default_notification_channel_id, notification);
                }

            } else {

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setAutoCancel(false)
                        .setStyle(style)
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

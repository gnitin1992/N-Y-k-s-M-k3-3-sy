package net.studiesmadesimple.nayaks.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.studiesmadesimple.nayaks.R;
import net.studiesmadesimple.nayaks.activities.MainActivity;
import net.studiesmadesimple.nayaks.data.NotificationData;
import net.studiesmadesimple.nayaks.database.MyDatabaseHelper;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sagar on 12/6/2016.
 */

public class NayaksFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    private MyDatabaseHelper databaseHelper;
    private ArrayList<String> notificationValues = new ArrayList<>();

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            // DO THE OPERTAIONS;
            String messageId = remoteMessage.getMessageId();
            long messageSentTime = remoteMessage.getSentTime();


            Date date = new Date(messageSentTime);
            Format format = new SimpleDateFormat(" dd/MM/yyyy HH:mm:ss");
            String abc =  format.format(date);

            Map<String,String> xyz = remoteMessage.getData();
            String t = xyz.get("title");
            String tex = xyz.get("body");


            sendNotification(messageId,t,tex,abc);


        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            String messageId = remoteMessage.getMessageId();
            long messageSentTime = remoteMessage.getSentTime();


            Date date = new Date(messageSentTime);
            Format format = new SimpleDateFormat(" dd/MM/yyyy HH:mm:ss");
            String abc =  format.format(date);


            Map<String,String> xyz = remoteMessage.getData();
            String t = xyz.get("title");
            String tex = xyz.get("text");

//            sendNotification(messageId,t,tex,abc);
            sendNotification(messageId,t,tex,abc);



        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String notificationId,String title,String message,String sentTime) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

//        databaseHelper = new MyDatabaseHelper(getApplicationContext());
//        databaseHelper.addNotification(new NotificationData(notificationId,title,message,sentTime));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
package bmc.example.remoteconfigdude.Service.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import bmc.example.remoteconfigdude.MainActivity;
import bmc.example.remoteconfigdude.R;
import bmc.example.remoteconfigdude.Utils.AppConstants;
import bmc.example.remoteconfigdude.Utils.Tools;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (AppConstants.IS_DEBUG) {
            Tools.logLine(this, "Message received [ " + remoteMessage + " ]");
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText("Message")
                .setContentTitle(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }
}

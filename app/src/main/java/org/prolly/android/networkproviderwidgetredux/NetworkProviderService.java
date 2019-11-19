package org.prolly.android.networkproviderwidgetredux;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import androidx.core.app.NotificationCompat;

public class NetworkProviderService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        TelephonyManager telephonyManager = ((TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE));
        telephonyManager.listen(new NetworkProviderPhoneListener(this), PhoneStateListener.LISTEN_SERVICE_STATE);

        return Service.START_STICKY;
    }
    @Override
    public void onCreate() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            String channelId = "some_channel_id";
            CharSequence channelName = "Some Channel";
            int importance = NotificationManager.IMPORTANCE_MIN;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(notificationChannel);

            Notification notification = new NotificationCompat.Builder(this, channelId)
                    .setLocalOnly(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();

            startForeground(1337, notification);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

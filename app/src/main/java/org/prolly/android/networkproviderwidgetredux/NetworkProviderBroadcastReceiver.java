package org.prolly.android.networkproviderwidgetredux;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

public class NetworkProviderBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("broadcastreceiver","Received Intent: '" + intent.getAction() + "'");
        if(intent.getAction() == "android.intent.action.PACKAGE_REPLACED") {
            if (intent.getData().getSchemeSpecificPart().equals(context.getPackageName())) {
                context.getApplicationContext().registerReceiver(new NetworkProviderWidgetRedux(), new IntentFilter("android.intent.action.SERVICE_STATE"));
            }
        } else {
            context.getApplicationContext().registerReceiver(new NetworkProviderWidgetRedux(), new IntentFilter("android.intent.action.SERVICE_STATE"));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, NetworkProviderService.class));
        } else {
            context.startService(new Intent(context, NetworkProviderService.class));
        }
    }
}

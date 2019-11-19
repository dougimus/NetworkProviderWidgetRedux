package org.prolly.android.networkproviderwidgetredux;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetworkProviderWidgetRedux extends AppWidgetProvider {

/*    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.d("Widget " + appWidgetId,"updateAppWidget Called" );

        //context.getApplicationContext().registerReceiver(new NetworkProviderWidgetRedux(), new IntentFilter("android.intent.action.SERVICE_STATE"));

        String widgetTextColor = NetworkProviderWidgetReduxConfigureActivity.loadTitlePref(context, appWidgetId);
        Log.d("Widget " + appWidgetId,"Loaded '" + widgetTextColor + "' as font color" );


        TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        telephonyManager.listen(new NetworkProviderPhoneListener(context),PhoneStateListener.LISTEN_SERVICE_STATE);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.network_provider_widget_redux);
        views.setTextColor(R.id.appwidget_text, Color.parseColor(widgetTextColor));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
*/
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("nowidgetid","onUpdate was Called");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, NetworkProviderService.class));
        } else {
            context.startService(new Intent(context, NetworkProviderService.class));
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            NetworkProviderWidgetReduxConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

  /*  @Override
    public void onEnabled(Context context) {
        Log.d("nowidgetid","onEnabled was Called");
    }*/

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("nowidgetid","Received Intent: '" + intent.getAction() + "'");
        if (intent.getAction() == "android.intent.action.SERVICE_STATE" || intent.getAction() == "android.appwidget.action.APPWIDGET_UPDATE_OPTIONS") {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, NetworkProviderWidgetRedux.class));
            this.onUpdate(context, appWidgetManager, ids);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


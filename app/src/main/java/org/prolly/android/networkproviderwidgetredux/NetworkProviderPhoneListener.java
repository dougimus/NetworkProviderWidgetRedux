package org.prolly.android.networkproviderwidgetredux;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Color;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.util.Log;
import android.widget.RemoteViews;

public class NetworkProviderPhoneListener extends PhoneStateListener {

    private Context context;

    public NetworkProviderPhoneListener(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void onServiceStateChanged(ServiceState serviceState) {
        String OperatorName;

        Context context = this.context;

        switch(serviceState.getState()) {
            case ServiceState.STATE_EMERGENCY_ONLY:
                OperatorName = context.getString(R.string.state_emergency);
                break;
            case ServiceState.STATE_IN_SERVICE:
                try {
                    OperatorName = serviceState.getOperatorAlphaLong();
                }
                catch(NullPointerException e) {
                    OperatorName = context.getString(R.string.state_error);
                }
                break;
            case ServiceState.STATE_OUT_OF_SERVICE:
                OperatorName = context.getString(R.string.state_no_signal);
                break;
            case ServiceState.STATE_POWER_OFF:
                OperatorName = context.getString(R.string.state_radio_off);
                break;
            default:
                OperatorName = context.getString(R.string.state_error);
        }

        Log.d("PhoneListener","Operator is named: '" + OperatorName + "'");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.network_provider_widget_redux);
        views.setTextViewText(R.id.appwidget_text, OperatorName);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, NetworkProviderWidgetRedux.class));
        for (int appWidgetId : ids) {
            String widgetTextColor = NetworkProviderWidgetReduxConfigureActivity.loadTitlePref(context, appWidgetId);
            try {
                views.setTextColor(R.id.appwidget_text, Integer.parseInt(widgetTextColor));
            }
            catch(Exception e) {
                try {
                    views.setTextColor(R.id.appwidget_text, Color.parseColor(widgetTextColor));
                    NetworkProviderWidgetReduxConfigureActivity.saveTitlePref(context, appWidgetId, Integer.toString(Color.parseColor(widgetTextColor)));
                }
                catch(Exception f) {
                    views.setTextColor(R.id.appwidget_text, Color.WHITE);
                    NetworkProviderWidgetReduxConfigureActivity.saveTitlePref(context, appWidgetId, Integer.toString(Color.WHITE));
                }
            }

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}

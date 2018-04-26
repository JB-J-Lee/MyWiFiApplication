package com.myjb.dev.wifi;

import android.content.Context;
import android.os.AsyncTask;

public class MyWiFiTask extends AsyncTask<Void, Void, Boolean> {

    private Context context;
    private SpecificWiFi.OnConnectionResultListener listener;

    public MyWiFiTask(Context context, SpecificWiFi.OnConnectionResultListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        SpecificWiFi wifi = new SpecificWiFi(context, listener, "yourssid", "yourpassword");
        try {
            return wifi.connectWiFi();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (listener != null)
            listener.onWiFiResult(result);
    }
}

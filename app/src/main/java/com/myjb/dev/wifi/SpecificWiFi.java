package com.myjb.dev.wifi;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class SpecificWiFi {
    private static final String TAG = "SpecificWiFi";

    public interface OnConnectionResultListener {
        void onWiFiResult(boolean result);
    }

    private Context context;
    private OnConnectionResultListener listener;
    private String ssid;
    private String password;

    public SpecificWiFi(Context context, OnConnectionResultListener listener, @NonNull String ssid, @NonNull String password) {
        this.context = context;
        this.listener = listener;
        this.ssid = ssid;
        this.password = password;
    }

    public boolean connectWiFi() throws InterruptedException {
        if (context == null) {
            return false;
        }

        if (isConnected()) {
            return true;
        }

        removeConnectionInfo();

        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wm.isWifiEnabled()) {
            wm.setWifiEnabled(true);

            while (!wm.startScan()) {
                Thread.sleep(1000);
            }
        }

        WifiConfiguration wfc = getConfigurationWPA(false);

        boolean result = false;
        int retry = 0;
        while (retry < 10) {
            Thread.sleep(1000 * (retry + 1));
            result = connect(wfc);
            if (result)
                break;
            retry++;
        }
        Log.e(TAG, "[connectWiFi] retry : " + retry);

        return result;
    }

    private boolean isConnected() {
        if (context == null) {
            return false;
        }

        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (!wm.isWifiEnabled()) {
            return false;
        }

        WifiInfo wi = wm.getConnectionInfo();

        if (wi == null || wi.getSSID() == null) {
            return false;
        }

        if (wi.getSSID().equalsIgnoreCase(ssid) || wi.getSSID().equalsIgnoreCase("\"" + ssid + "\"")) {
            return true;
        }

        return false;
    }

    private boolean removeConnectionInfo() {
        if (context == null) {
            return false;
        }

        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        List<WifiConfiguration> configuredNetworks = wm.getConfiguredNetworks();
        if (configuredNetworks == null) {
            return false;
        }

        boolean registered = false;
        for (WifiConfiguration wc : configuredNetworks) {
            if (wc.SSID.equalsIgnoreCase("\"" + ssid + "\"")) {
                boolean disableNetwork = wm.disableNetwork(wc.networkId);
                boolean removeNetwork = wm.removeNetwork(wc.networkId);
                Log.e(TAG, "[connectWiFi] disableNetwork : " + disableNetwork + ", removeNetwork : " + removeNetwork);

                registered = true;
            }
        }

        return registered;
    }

    /**
     * Open
     */
    private WifiConfiguration getConfigurationOpen(boolean hidden) {
        WifiConfiguration wfc = new WifiConfiguration();
        wfc.SSID = "\"".concat(ssid).concat("\"");
        wfc.priority = 40;

        wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        wfc.hiddenSSID = hidden;

        return wfc;
    }

    /**
     * WEP
     */
    private WifiConfiguration getConfigurationWEP(boolean hidden) {
        WifiConfiguration wfc = new WifiConfiguration();
        wfc.SSID = "\"".concat(ssid).concat("\"");
        wfc.priority = 40;

        wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

        wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);

        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);

        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

        int length = password.length();
        if ((length == 10 || length == 26 || length == 58) && password.matches("[0-9A-Fa-f]*"))
            wfc.wepKeys[0] = password;
        else
            // wfc.wepKeys[0] = '"' + PASSWORD + '"';
            wfc.wepKeys[0] = "\"".concat(password).concat("\"");
        wfc.wepTxKeyIndex = 0;

        wfc.hiddenSSID = hidden;

        return wfc;
    }

    /**
     * WPA, WPA2
     */
    private WifiConfiguration getConfigurationWPA(boolean hidden) {
        WifiConfiguration wfc = new WifiConfiguration();
        wfc.SSID = "\"".concat(ssid).concat("\"");
        wfc.priority = 40;

        wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

        wfc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        wfc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);

        wfc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wfc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);

        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wfc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

        wfc.preSharedKey = "\"".concat(password).concat("\"");

        wfc.hiddenSSID = hidden;

        return wfc;
    }

    /**
     * Connect AP
     */
    private boolean connect(WifiConfiguration wc) {
        if (context == null) {
            return false;
        }

        boolean isConfigured = false;
        int networkID = -1;

        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        List<WifiConfiguration> wifiConfigurationList = wm.getConfiguredNetworks();
        if (wifiConfigurationList != null) {
            for (WifiConfiguration w : wifiConfigurationList) {
                if (w.SSID.equalsIgnoreCase("\"" + wc.SSID + "\"")) {
                    Log.e(TAG, "getConfiguredNetworks : " + w.SSID);

                    isConfigured = true;
                    networkID = w.networkId;
                    break;
                }
            }
        }

        if (!isConfigured)
            networkID = wm.addNetwork(wc);

        boolean enableNetwork = false;
        if (networkID != -1)
            enableNetwork = wm.enableNetwork(networkID, true);

        Log.e(TAG, "SSID : " + wc.SSID + " " + (enableNetwork ? "Connected!" : "Connect Fail!") + ", networkID : " + networkID);

        return enableNetwork;
    }
}

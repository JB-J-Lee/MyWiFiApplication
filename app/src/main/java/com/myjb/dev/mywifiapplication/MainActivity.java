package com.myjb.dev.mywifiapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.myjb.dev.wifi.MyWiFiTask;
import com.myjb.dev.wifi.SpecificWiFi;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.action_wifi, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.action, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new MyWiFiTask(getBaseContext(), new SpecificWiFi.OnConnectionResultListener() {
                                    @Override
                                    public void onWiFiResult(final boolean result) {
                                        Log.e(TAG, "[onWiFiResult] " + result);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getBaseContext(), "[onWiFiResult] " + result, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }).execute();
                            }
                        }).show();
            }
        });
    }
}

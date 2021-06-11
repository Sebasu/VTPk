package com.vagoscorp.virtualterminal.prokey;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView stat;
    Button enPro;

    Intent appIntent;
    Intent appMIntent;

    public static final String PRO = "PRO";
    public static final int isPRO = 2016;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PackageManager manager = getPackageManager();
        setContentView(R.layout.activity_main);
        stat = findViewById(R.id.state);
        enPro = findViewById(R.id.enPro);
        appIntent = manager.getLaunchIntentForPackage("com.vagoscorp.virtualterminal");
        appMIntent = new Intent(Intent.ACTION_VIEW);
        appMIntent.setData(Uri.parse("market://details?id=com.vagoscorp.virtualterminal"));
    }

    @Override
    protected void onDestroy() {
        if (appIntent != null) {
            appIntent.putExtra(PRO, isPRO);
            startActivity(appIntent);
            finish();
        } else {
            if (appMIntent.resolveActivity(getPackageManager()) != null)
                startActivity(appMIntent);
        }
        super.onDestroy();
    }
}
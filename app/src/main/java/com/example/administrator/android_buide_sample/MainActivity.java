package com.example.administrator.android_buide_sample;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by lz on 2016/7/5.
 */
public class MainActivity extends AppCompatActivity {

    private TextView packageName;

    private TextView versionName;

    private TextView versionCode;
    private TextView channelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUIComponent();
        setValue();
        //String channelKey = applicationInfo.metaData.getString("CHANNEL_KEY");
//        ApplicationInfo applicationInfo = null;
//        try {
//            applicationInfo = packageManager.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }


//        productKey = applicationInfo.metaData.getString(UUCIN_APP_KEY);
//        channelKey = applicationInfo.metaData.getString(UUCIN_CHANNEL_KEY);
//        appName = applicationInfo.metaData.getString(UUCIN_APP_NAME);
//        market = applicationInfo.metaData.getString(UUCIN_CHANNEL_KEY);

    }

    private void setValue() {
        packageName.setText(getPackageName());
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        versionName.setText(packageInfo.versionName + "");
        versionCode.setText(packageInfo.versionCode + "");

        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String channelNameKey = applicationInfo.metaData.getString("CHANNEL_KEY");
        channelName.setText(channelNameKey);
    }

    @Nullable
    private void initUIComponent() {
        packageName = (TextView) findViewById(R.id.package_name);
        versionName = (TextView) findViewById(R.id.version_name);
        versionCode = (TextView) findViewById(R.id.version_code);
        channelName = (TextView) findViewById(R.id.channal_name);
    }
}

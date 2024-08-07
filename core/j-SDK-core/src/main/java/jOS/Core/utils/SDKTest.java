package jOS.Core.utils;

import static android.os.Build.VERSION.RELEASE;
import static android.os.Build.VERSION.RELEASE_OR_PREVIEW_DISPLAY;
import static jOS.Core.Build.jOS_RELEASE;
import static jOS.Core.Build.j_DEVICE;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import jOS.Core.R;
import jOS.Core.jSDKCoreApp;

public class SDKTest {

    public static void Test(AppCompatActivity context) {
        context.getSupportActionBar().setSubtitle("AAAA");
        TextView text = context.findViewById(R.id.textView);
        Button button = context.findViewById(R.id.button);
        Button button2 = context.findViewById(R.id.button2);
        String android_sdk_test = "Android: " + androidver();
        String j_sdk_test = "jOS: " + jOS_RELEASE;
        String j_verify_test = "is j Device: " + j_DEVICE();
        String all = android_sdk_test + " " + j_sdk_test + " " + j_verify_test;
        text.setText(all);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent().setComponent(new ComponentName(context.getApplicationInfo().packageName, "jOS.Core.SDKChangelogActivity")));
            }
        });
    }

    private static String androidver() {
        Log.i(jSDKCoreApp.TAG, String.valueOf(android.os.Build.VERSION.SDK_INT));
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return RELEASE_OR_PREVIEW_DISPLAY;
        }
        return RELEASE;
    }
}

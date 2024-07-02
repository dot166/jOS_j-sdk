package jOS.Core;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

public class jSDKCoreApp extends Application {
    public static final String TAG = "jSDK";

    public void onCreate() {
        super.onCreate();
    }

    private Activity mCurrentActivity = null;
    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity currentActivity){
        this.mCurrentActivity = currentActivity;
        Log.i(TAG, String.valueOf(mCurrentActivity));
        Log.i(TAG, String.valueOf(currentActivity));
    }
}

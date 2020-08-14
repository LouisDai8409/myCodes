package com.rs.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.rs.activity.MainActivity;

/**
 * 权限的检查和请求
 */
public abstract class PermissionUtil {
    private final static String TAG = "PermissionUtil";

    public static void checkPermissions(Activity activity) {

        if (Build.VERSION.SDK_INT >= 23) {
            Log.d(TAG, "checkPermissions: need it");
            int total_grant = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO)
                    + ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    + ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (total_grant != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            }else{
                if(activity instanceof MainActivity){
                    ( (MainActivity)activity).readyToStart();
                }
            }

        }
    }
}

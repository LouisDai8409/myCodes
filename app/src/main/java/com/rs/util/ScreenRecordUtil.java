package com.rs.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.rs.service.RecordService;

import java.io.File;

public abstract class ScreenRecordUtil {

    private static RecordService service;

    private static int result_code;

    private static Intent result_data;

    private static int sWidth, sHeight, sDensity;

    private static String savePath;

    public static int getsWidth() {
        return sWidth;
    }

    public static int getsHeight() {
        return sHeight;
    }

    public static int getsDensity() {
        return sDensity;
    }

    public static String getSavePath() {
        return savePath;
    }

    public static void init(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        sWidth = dm.widthPixels;
        sHeight = dm.heightPixels;
        sDensity = dm.densityDpi;
        savePath = activity.getExternalFilesDir(Environment.DIRECTORY_MOVIES).getPath();
    }

    public static void setRecordService(RecordService service) {
        ScreenRecordUtil.service = service;
    }

    public static void startRecord() {
        ScreenRecordUtil.service.startRecord();
    }

    public static void stopRecord() {
        ScreenRecordUtil.service.stopRecord();
    }

    public static void prepare() {
        ScreenRecordUtil.service.prepare();
    }

    public static void setBase(int result_code, Intent result_data) {
        ScreenRecordUtil.result_code = result_code;
        ScreenRecordUtil.result_data = result_data;
    }

    public static int getResult_code() {
        return result_code;
    }

    public static Intent getResult_data() {
        return result_data;
    }


}

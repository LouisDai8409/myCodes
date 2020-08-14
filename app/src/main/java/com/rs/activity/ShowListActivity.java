package com.rs.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.example.recordscreen.R;
import com.rs.util.FileUtil;
import com.rs.util.ScreenRecordUtil;

import java.io.File;

public class ShowListActivity extends AppCompatActivity {
    private final static String TAG = "ShowListActivity";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_show_list);
        findRecords();
    }

    public void findRecords() {
        File[] mp4s = getExternalFilesDir(Environment.DIRECTORY_MOVIES).listFiles();

        for (File each : mp4s) {
            String time = FileUtil.getDuration(each.getPath());
            Log.d(TAG, "findRecords: "+time);
        }
    }
}
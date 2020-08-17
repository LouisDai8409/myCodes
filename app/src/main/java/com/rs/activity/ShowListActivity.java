package com.rs.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.recordscreen.R;
import com.rs.pojo.Record;
import com.rs.util.FileUtil;
import com.rs.util.RecordsAdapter;
import com.rs.util.ScreenRecordUtil;

import java.io.File;
import java.util.List;

public class ShowListActivity extends AppCompatActivity {
    private final static String TAG = "ShowListActivity";

    private ListView showRecords;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_show_list);
        this.showRecords = findViewById(R.id.showRecords);
        findRecords();
    }

    public void findRecords() {
        File[] mp4s = getExternalFilesDir(Environment.DIRECTORY_MOVIES).listFiles();

        List<Record> recordList = FileUtil.createRecordsInfo(mp4s);

        RecordsAdapter adapter = new RecordsAdapter(this,R.layout.record_list,recordList);

        this.showRecords.setAdapter(adapter);
    }

    public void say(View view){

    }
}
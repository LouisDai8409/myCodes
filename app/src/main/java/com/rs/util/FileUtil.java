package com.rs.util;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.rs.pojo.Record;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class FileUtil {

    private static final String TAG = "FileUtil";

    private static MediaMetadataRetriever retriever;

    static {
        retriever = new MediaMetadataRetriever();
    }

    /**
     * 时间 格式展示
     * @param media_file_path
     * @return
     */
    public static String getDuration(String media_file_path) {
        retriever.setDataSource(media_file_path);
        int second = (Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))) / 1000;
        int minute = second / 60;
        second = second - (minute * 60);

        String str_time = (minute < 10 ? "0" + minute : minute + "") + ":" + (second < 10 ? "0" + second : second + "");
        return str_time;
    }

    public static List<Record> createRecordsInfo(File[] mp4s){
        List<Record> records = new ArrayList<>();
        for (File each : mp4s) {
            Record r = new Record();
            r.setDuration(getDuration(each.getPath()));
            r.setFileName(each.getName());
            r.setPath(each.getPath());
            records.add(r);
        }
        return records;
    }
}

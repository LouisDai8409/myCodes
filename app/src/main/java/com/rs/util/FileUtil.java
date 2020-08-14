package com.rs.util;

import android.media.MediaMetadataRetriever;

public abstract class FileUtil {

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
}

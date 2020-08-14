package com.rs.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.rs.util.ScreenRecordUtil;

import java.io.File;
import java.io.IOException;

public class RecordService extends Service {
    private final static String TAG = "RecordService";

    private MediaProjection mediaProjection;
    private MediaRecorder mediaRecorder;
    private VirtualDisplay virtualDisplay;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new RecordBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 录制服务已经启动");

    }

    public void prepare() {
        Log.d(TAG, "prepare: 录屏对象准备中...");
        createMediaProjection();
        createMediaRecord();
        createVirtualDisplay();
    }


    private void createMediaProjection() {
        mediaProjection = ((MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE))
                .getMediaProjection(ScreenRecordUtil.getResult_code(), ScreenRecordUtil.getResult_data());
        Log.d(TAG, "createMediaProjection: 创建完毕了吗？" + (mediaProjection != null ? true : false));
    }

    private void createMediaRecord() {
        MediaRecorder temp = new MediaRecorder();
        temp.setAudioSource(MediaRecorder.AudioSource.MIC);
        temp.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        temp.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        // 以下配置一定要注意顺序
        String path = ScreenRecordUtil.getSavePath() + File.separator + "Record_" + System.currentTimeMillis() + ".mp4";
        temp.setOutputFile(path);
        temp.setVideoSize(ScreenRecordUtil.getsWidth(), ScreenRecordUtil.getsHeight());
        temp.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        temp.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        temp.setVideoEncodingBitRate(5 * ScreenRecordUtil.getsWidth() * ScreenRecordUtil.getsHeight()); // 控制解析度
        temp.setVideoFrameRate(40); //控制帧数

        try {
            temp.prepare();
            Log.d(TAG, "createMediaRecord: 录屏器准备完毕");
            Log.d(TAG, "createMediaRecord: 文件保存位置:" + path);
            mediaRecorder = temp;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createVirtualDisplay() {
        virtualDisplay = this.mediaProjection.createVirtualDisplay("mainPage",
                ScreenRecordUtil.getsWidth(), ScreenRecordUtil.getsHeight(),
                ScreenRecordUtil.getsDensity(), DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(), null, null);
        Log.d(TAG, "createVirtualDisplay: 虚拟显示器准备完毕");
    }

    public void startRecord() {
        mediaRecorder.start();
        Log.d(TAG, "startRecord: 屏幕录制已经开始");
    }

    public void stopRecord() {
        if (virtualDisplay != null) {
            virtualDisplay.release();
            virtualDisplay = null;
        }
        if (mediaRecorder != null) {
            mediaRecorder.setOnErrorListener(null);
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder = null;
        }
        if (mediaProjection != null) {
            mediaProjection.stop();
            mediaProjection = null;
        }
        Log.d(TAG, "onDestroy: 屏幕录制已经结束");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: 执行");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: 执行");
        return super.onUnbind(intent);
    }

    public class RecordBinder extends Binder {
        public RecordService getService() {
            return RecordService.this;
        }
    }
}

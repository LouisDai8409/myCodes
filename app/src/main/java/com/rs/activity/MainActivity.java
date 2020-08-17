package com.rs.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.recordscreen.R;
import com.rs.service.RecordService;
import com.rs.util.PermissionUtil;
import com.rs.util.ScreenRecordUtil;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";

    private Button start, stop, list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        list = findViewById(R.id.list);

        // 获取权限
        PermissionUtil.checkPermissions(this);
        bindService();
        ScreenRecordUtil.init(this);

    }

    /**
     * 开始录制以后调用
     */
    public void readyToStart() {
        start.setEnabled(true);
        stop.setEnabled(false);
        list.setEnabled(true);
    }

    /**
     * 停止录制以后调用
     */
    public void readyToStop() {
        start.setEnabled(false);
        stop.setEnabled(true);
        list.setEnabled(false);
    }

    /**
     * 启动并绑定服务
     */
    public void bindService(){
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                RecordService.RecordBinder binder = (RecordService.RecordBinder) iBinder;
                RecordService service = binder.getService();
                ScreenRecordUtil.setRecordService(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        // 绑定服务
        bindService(new Intent(this, RecordService.class), serviceConnection, BIND_AUTO_CREATE);

        Log.d(TAG, "onActivityResult: 服务bind 成功");
    }

    /**
     * 查看权限授予情况，如没有全部授予  则不能够进行视频录制
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int every : grantResults) {
            if (every == PackageManager.PERMISSION_DENIED) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("禁用这些权限导致您不能进行屏幕录制，请在设置中开启")
                        .setPositiveButton("好的", null).create();
                dialog.show();
                return;
            }
        }
        readyToStart();
        Log.d(TAG, "onRequestPermissionsResult: 权限都已经授予");

    }

    private final static int CAPTURE_REQ_CODE = 1002;

    /**
     * 开始录制屏幕
     * @param view
     */
    public void startRecord(View view) {
        // 开始请求录制屏幕
        MediaProjectionManager manager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        Intent capture = manager.createScreenCaptureIntent();
        startActivityForResult(capture, CAPTURE_REQ_CODE);
    }

    /**
     * 用户允许录制后的操作
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAPTURE_REQ_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    ScreenRecordUtil.setBase(resultCode, data);
//                    Intent service = new Intent(this, RecordService.class);
//                    service.putExtra("resultCode", resultCode);
//                    service.putExtra("data", data);
//                    startService(service);
                    readyToStop();
                    ScreenRecordUtil.prepare();
                    ScreenRecordUtil.startRecord();

                } else {
                    Log.d(TAG, "onActivityResult: 录屏被禁止");
                }
        }
    }

    /**
     * 停止录制屏幕
     * @param view
     */
    public void stopRecord(View view) {
        ScreenRecordUtil.stopRecord();
        readyToStart();
    }

    public void showList(View view){
        Intent it = new Intent(this, ShowListActivity.class);
        startActivity(it);
    }




}
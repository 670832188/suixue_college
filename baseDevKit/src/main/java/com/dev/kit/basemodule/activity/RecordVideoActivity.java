package com.dev.kit.basemodule.activity;

import android.content.Intent;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.daasuu.camerarecorder.CameraRecordListener;
import com.daasuu.camerarecorder.CameraRecorder;
import com.daasuu.camerarecorder.CameraRecorderBuilder;
import com.daasuu.camerarecorder.LensFacing;
import com.dev.kit.basemodule.R;
import com.dev.kit.basemodule.util.Config;
import com.dev.kit.basemodule.util.ToastUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by cuiyan on 2018/8/20.
 */
public class RecordVideoActivity extends BaseActivity {
    public static final String RECODE_FILE_PATH = "recordFilePath";
    private CameraRecorder cameraRecorder;
    private String recordFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        recordFilePath = getIntent().getStringExtra(RECODE_FILE_PATH);
        initRecorder();
        initView();
    }

    private void initView() {
        setOnClickListener(R.id.btn_start, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(recordFilePath)) {
                    recordFilePath = getVideoOutputFilePath();
                }
                cameraRecorder.start(recordFilePath);
            }
        });
        setOnClickListener(R.id.btn_stop, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraRecorder.stop();
            }
        });
    }

    private void initRecorder() {
        GLSurfaceView surfaceView = findViewById(R.id.GLSurfaceView);
        cameraRecorder = new CameraRecorderBuilder(this, surfaceView)
                .lensFacing(LensFacing.BACK)
                .videoSize(720, 1280)
                .cameraSize(1280, 720)
                .recordNoFilter(true)
                .setBitRate((int) (720 * 1280 * 1.5))
                .cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onRecordStart() {

                    }

                    @Override
                    public void onGetFlashSupport(boolean b) {

                    }

                    @Override
                    public void onRecordComplete() {
                        ToastUtil.showToast(RecordVideoActivity.this, "视频录制完成");

                        Handler handler = new Handler(getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                File file = new File(recordFilePath);
                                Uri contentUri = Uri.fromFile(file);
                                mediaScanIntent.setData(contentUri);
                                sendBroadcast(mediaScanIntent);
                                Intent intent = new Intent();
                                intent.putExtra(RECODE_FILE_PATH, recordFilePath);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }, 1000);

                    }

                    @Override
                    public void onError(Exception e) {

                    }

                    @Override
                    public void onCameraThreadFinish() {

                    }
                })
                .build();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraRecorder != null) {
            cameraRecorder.release();
        }
    }

    private String getVideoOutputFilePath() {
        String videoDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Config.VIDEO_CACHE_DIR_NAME;
        File videoDirFile = new File(videoDirPath);
        if (!videoDirFile.exists() && !videoDirFile.mkdirs()) {
            return null;
        }
        String videoFileName = "suiXue_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".mp4";
        return videoDirPath + File.separator + videoFileName;
    }
}

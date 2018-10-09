package com.dev.kit.basemodule.activity;

import android.content.Intent;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by cuiyan on 2018/8/20.
 */
public class RecordVideoActivity extends BaseActivity {
    public static final String RECODE_FILE_PATH = "recordFilePath";
    public static final String VIDEO_WIDTH = "videoWidth";
    public static final String VIDEO_HEIGHT = "videoHeight";
    private CameraRecorder cameraRecorder;
    private String recordFilePath;
    private static final int videoWidth = 720;
    private static final int videoHeight = 1280;
    private static final int cameraWidth = 1280;
    private static final int cameraHeight = 720;

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
                .videoSize(videoWidth, videoHeight)
                .cameraSize(cameraWidth, cameraHeight)
                .recordNoFilter(true)
                .setBitRate((int) (videoWidth * videoHeight * 1.5))
                .cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onRecordStart() {

                    }

                    @Override
                    public void onGetFlashSupport(boolean b) {

                    }

                    @Override
                    public void onRecordComplete() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent();
                                intent.putExtra(RECODE_FILE_PATH, recordFilePath);
                                intent.putExtra(VIDEO_WIDTH, videoWidth);
                                intent.putExtra(VIDEO_HEIGHT, videoHeight);
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

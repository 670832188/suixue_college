package com.suixue.edu.college.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.util.LogUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.adapter.VideoFrameAdapter;

import java.util.ArrayList;

/**
 * Author: cuiyan
 * Date:   18/10/7 17:16
 * Desc:
 */
public class VideoEditActivity extends BaseActivity {
    public static final int FRAME_SHOW_COUNT = 7;
    public static final String TARGET_VIDEO_PATH = "targetVideoPath";
    public static final String MAX_ENABLED_TIME = "maxEnabledTime";
    public static final String CONVERT_TO_GIF = "convertToGif";
    private String targetVideoPath;
    // 最大选取时长s
    private int maxEnabledTime;
    private boolean convertToGif;

    private VideoView videoView;
    private RecyclerView rvVideoFrame;
    private TextView tvCancel;
    private TextView tvConfirm;
    private ImageView ivVideoPlay;
    private VideoFrameAdapter adapter;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        targetVideoPath = intent.getStringExtra(TARGET_VIDEO_PATH);
        maxEnabledTime = intent.getIntExtra(MAX_ENABLED_TIME, 3);
        convertToGif = intent.getBooleanExtra(CONVERT_TO_GIF, false);
        initView();
        loadPreviewFrames();
    }

    private void initView() {
        videoView = findViewById(R.id.video_view);
        rvVideoFrame = findViewById(R.id.rv_video_frame);
        tvCancel = findViewById(R.id.tv_cancel);
        tvConfirm = findViewById(R.id.tv_confirm);
        ivVideoPlay = findViewById(R.id.iv_video_play);
        adapter = new VideoFrameAdapter(this, maxEnabledTime, new ArrayList<Bitmap>());
        rvVideoFrame.setAdapter(adapter);
        rvVideoFrame.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
    }

    private void loadPreviewFrames() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(targetVideoPath);
                int durationUs = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;
                int frameInterval = (int) (maxEnabledTime * 1000 * 1000 / (float) FRAME_SHOW_COUNT);
                for (long timePointUs = 0; timePointUs < durationUs; timePointUs += frameInterval) {
                    final Bitmap bitmap = retriever.getFrameAtTime(timePointUs, MediaMetadataRetriever.OPTION_CLOSEST);
                    if (bitmap != null) {
                        final Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
                        if (bitmap != previewBitmap) {
                            bitmap.recycle();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.appendItem(previewBitmap, false);
                            }
                        });
                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.appendItem(bitmap, false);
                            }
                        });
                    }
                }
            }
        }).start();
    }

}

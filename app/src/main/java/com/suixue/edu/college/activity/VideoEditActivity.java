package com.suixue.edu.college.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.util.Config;
import com.dev.kit.basemodule.view.NetProgressDialog;
import com.suixue.edu.college.R;
import com.suixue.edu.college.adapter.VideoFrameAdapter;
import com.suixue.edu.college.gifEncoder.GifEncoderUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Author: cuiyan
 * Date:   18/10/7 17:16
 * Desc:
 */
public class VideoEditActivity extends BaseActivity implements View.OnClickListener {
    public static final int FRAME_SHOW_COUNT = 3;
    public static final String TARGET_VIDEO_PATH = "targetVideoPath";
    public static final String MAX_ENABLED_TIME = "maxEnabledTime";
    public static final String CONVERT_TO_GIF = "convertToGif";
    public static final String OUTPUT_FILE_PATH = "outputFilePath";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    private String targetVideoPath;
    // 最大选取时长s
    private int maxEnabledTime;
    private boolean convertToGif;

    private VideoView videoView;
    private VideoFrameAdapter adapter;
    private ImageView ivVideoPlay;
    private Handler handler = new Handler();
    // 视频帧选取时间间隔：单位纳秒
    private int frameInterval;
    private int seekTimePoint;

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
        ivVideoPlay = findViewById(R.id.iv_video_play);
        setOnClickListener(R.id.tv_cancel, this);
        setOnClickListener(R.id.tv_confirm, this);
        setOnClickListener(R.id.iv_video_play, this);
        initVideoView();
        initRvFrame();
    }

    private void initVideoView() {
        videoView = findViewById(R.id.video_view);
        videoView.setVideoPath(targetVideoPath);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivVideoPlay.setImageResource(R.drawable.ic_video_play);
            }
        });
    }

    private void initRvFrame() {
        adapter = new VideoFrameAdapter(this, maxEnabledTime, new ArrayList<Bitmap>());
        RecyclerView rvVideoFrame = findViewById(R.id.rv_video_frame);
        rvVideoFrame.setAdapter(adapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false);
        rvVideoFrame.setLayoutManager(layoutManager);
        rvVideoFrame.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                View firstVisibleChild = layoutManager.findViewByPosition(firstVisiblePosition);
                int childWidth = ((ViewGroup) firstVisibleChild).getChildAt(0).getWidth();
                int scrollX = childWidth * firstVisiblePosition - firstVisibleChild.getLeft();
                seekTimePoint = (int) ((float) scrollX / childWidth * frameInterval / 1000);
                videoView.seekTo(seekTimePoint);
            }
        });
    }

    private void loadPreviewFrames() {
        final NetProgressDialog progressDialog = NetProgressDialog.getInstance(this, getString(R.string.tip_video_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(targetVideoPath);
                int durationUs = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;
                frameInterval = (int) (maxEnabledTime * 1000 * 1000 / (float) (FRAME_SHOW_COUNT - 1));
                for (long timePointUs = 0; timePointUs < durationUs; timePointUs += frameInterval) {
                    final Bitmap bitmap = retriever.getFrameAtTime(timePointUs, MediaMetadataRetriever.OPTION_CLOSEST);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.appendItem(bitmap, false);
                        }
                    });
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setLoadComplete(true);
                    }
                });
                retriever.release();
                progressDialog.dismiss();
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            seekTimePoint = videoView.getCurrentPosition();
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
        if (seekTimePoint == 0) {
            seekTimePoint = 10;
        }
        videoView.seekTo(seekTimePoint);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel: {
                finish();
                break;
            }
            case R.id.tv_confirm: {
                if (convertToGif) {
                    convertGif();
                } else {
                    clipVideo();
                }
                break;
            }
            case R.id.iv_video_play: {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    ivVideoPlay.setImageResource(R.drawable.ic_video_play);
                } else {
                    videoView.start();
                    ivVideoPlay.setImageResource(R.drawable.ic_video_pause);
                }
                break;
            }
        }
    }

    private void convertGif() {
        final NetProgressDialog progressDialog = NetProgressDialog.getInstance(this, getString(R.string.tip_gif_making));
        progressDialog.setCancelable(false);
        String outputFilePath = Config.getOutputImgDirPath() + File.separator + "suiXue_" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".gif";
        new GifEncoderUtil().generateGifByVideoPath(targetVideoPath, outputFilePath, 3, seekTimePoint, maxEnabledTime * 1000, new GifEncoderUtil.OnGifEncodeListener() {
            @Override
            public void onStart() {
                progressDialog.show();
            }

            @Override
            public void onSuccess(String gifPath, int width, int height) {
                progressDialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra(OUTPUT_FILE_PATH, gifPath);
                intent.putExtra(CONVERT_TO_GIF, convertToGif);
                intent.putExtra(WIDTH, width);
                intent.putExtra(HEIGHT, height);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                progressDialog.dismiss();
            }
        });
    }

    private void clipVideo() {
        showToast("视频剪切功能尚未实现");
    }
}

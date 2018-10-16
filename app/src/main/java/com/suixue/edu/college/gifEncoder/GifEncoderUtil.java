package com.suixue.edu.college.gifEncoder;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuiyan on 2018/9/29.
 */
public class GifEncoderUtil {

    private int width;
    private int height;
    private int desiredFrameRate;
    private int frameIntervalUs;
    private Handler handler = new Handler(Looper.getMainLooper());

    /**
     * @param startTimePoint   视频片段截取时间起点，单位ms
     * @param clipTimeDuration 视频片段截取时长，单位ms
     */
    public void generateGifByVideoPath(@NonNull final String videoPath, @NonNull final String outputGifPath, final int frameRate, final int startTimePoint, final int clipTimeDuration, @NonNull final OnGifEncodeListener listener) {
        File file = new File(videoPath);
        if (!file.exists()) {
            throw new RuntimeException("input video file not found: " + videoPath);
        }
        desiredFrameRate = frameRate == 0 ? 10 : frameRate;
        frameIntervalUs = (int) (1000 * 1000f / desiredFrameRate);
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onStart();
                    }
                });
                try {
                    List<Bitmap> bitmapList = generateBitmaps(videoPath, startTimePoint, clipTimeDuration);
                    width = bitmapList.get(0).getWidth();
                    height = bitmapList.get(0).getHeight();
                    GifEncoder gifEncoder = new GifEncoder();
                    gifEncoder.setFrameRate(desiredFrameRate);
                    gifEncoder.setSize(width, height);
                    gifEncoder.start(new FileOutputStream(outputGifPath));
                    for (Bitmap bitmap : bitmapList) {
                        gifEncoder.addFrame(bitmap);
                    }
                    gifEncoder.finish();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(outputGifPath, width, height);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e);
                }
            }
        }).start();
    }

    private List<Bitmap> generateBitmaps(String videoPath, int startTimePoint, int clipTimeDuration) {
        List<Bitmap> bitmapList = new ArrayList<>();
        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        final int durationUs = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) * 1000;
        startTimePoint *= 1000;
        clipTimeDuration *= 1000;
        if (startTimePoint < 0 || startTimePoint >= durationUs) {
            startTimePoint = 0;
        }
        if (clipTimeDuration <= 0 || clipTimeDuration > durationUs - startTimePoint) {
            clipTimeDuration = durationUs - startTimePoint;
        }
        for (long timePointUs = startTimePoint; timePointUs < clipTimeDuration + startTimePoint; timePointUs += frameIntervalUs) {
            Bitmap bitmap = retriever.getFrameAtTime(timePointUs, MediaMetadataRetriever.OPTION_CLOSEST);
            if (bitmap != null) {
                // 压缩
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                if (width > 480) {
                    width = 480;
                    height = (int) (height * 480f / bitmap.getWidth());
                }
                Bitmap dst = Bitmap.createScaledBitmap(bitmap, width, height, false);

                if (bitmap != dst) {
                    bitmap.recycle();
                }
                bitmapList.add(dst);
            }
        }
        retriever.release();
        return bitmapList;
    }

    public interface OnGifEncodeListener {
        void onStart();

        void onSuccess(String gifPath, int width, int height);

        void onError(Throwable throwable);
    }
}

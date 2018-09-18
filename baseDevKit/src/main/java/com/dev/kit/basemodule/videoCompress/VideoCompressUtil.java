package com.dev.kit.basemodule.videoCompress;

import android.os.AsyncTask;

import com.dev.kit.basemodule.util.LogUtil;

/**
 * Created by cuiyan on 2018/9/18.
 */
public class VideoCompressUtil {
    public static synchronized void compressVideo(String videoPath, String outputVideoPath, IVideoCompress listener) {
        LogUtil.e("mytag", "compressVideo: inputPath" + videoPath + " outputPath--->" + outputVideoPath);
        VideoCompressTask task = new VideoCompressTask(videoPath, outputVideoPath, listener);
        task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }
}

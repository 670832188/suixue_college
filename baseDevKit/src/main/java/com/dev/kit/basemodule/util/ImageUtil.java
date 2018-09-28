package com.dev.kit.basemodule.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.shaohui.advancedluban.Luban;
import me.shaohui.advancedluban.OnCompressListener;
import me.shaohui.advancedluban.OnMultiCompressListener;

/**
 * image工具类
 * Created by cy on 2017/12/5.
 */

public class ImageUtil {

    public static synchronized Bitmap cropSquareBitmap(Bitmap bitmap) {//从中间截取一个正方形
        return cropSquareBitmap(bitmap, Integer.MAX_VALUE);
    }

    public static synchronized Bitmap cropSquareBitmap(Bitmap bitmap, int maxWH) {//从中间截取一个正方形
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = Math.min(maxWH, Math.min(w, h));
        return Bitmap.createBitmap(bitmap, (bitmap.getWidth() - cropWidth) / 2, (bitmap.getHeight() - cropWidth) / 2, cropWidth, cropWidth);
    }

    public static synchronized void compressImg(Context context, File imgFile, @NonNull final OnCompressListener listener) {
        Luban luban;
        File cacheFile = getCacheDir();
        if (cacheFile != null) {
            luban = Luban.compress(imgFile, cacheFile).putGear(Luban.THIRD_GEAR);
        } else {
            luban = Luban.compress(context, imgFile).putGear(Luban.THIRD_GEAR);
        }
        luban.launch(listener);
    }

    public static synchronized void compressImgByFiles(Context context, List<File> imgFileList, int compressMode, @NonNull final OnMultiCompressListener listener) {
        Luban luban;
        File cacheFile = getCacheDir();
        if (cacheFile != null) {
            luban = Luban.compress(imgFileList, cacheFile).putGear(Luban.THIRD_GEAR);
        } else {
            luban = Luban.compress(context, imgFileList).putGear(Luban.THIRD_GEAR);
        }
        luban.launch(listener);
    }

    public static synchronized void compressImgByPaths(Context context, List<String> imgPathList, @NonNull final OnMultiCompressListener listener) {
        List<File> imgFileList = new ArrayList<>();
        for (String path : imgPathList) {
            imgFileList.add(new File(path));
        }
        Luban luban;
        File cacheFile = getCacheDir();
        if (cacheFile != null) {
            luban = Luban.compress(imgFileList, cacheFile).putGear(Luban.THIRD_GEAR);
        } else {
            luban = Luban.compress(context, imgFileList).putGear(Luban.THIRD_GEAR);
        }
        luban.launch(listener);
    }

    private static File getCacheDir() {
        String cacheDirName = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Config.IMG_CACHE_DIR_NAME;
        File cacheDirFile = new File(cacheDirName);
        if (cacheDirFile.exists()) {
            return cacheDirFile;
        } else {
            if (cacheDirFile.mkdirs()) {
                return cacheDirFile;
            }
        }
        return null;
    }
}

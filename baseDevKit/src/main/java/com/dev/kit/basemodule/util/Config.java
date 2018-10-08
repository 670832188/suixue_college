package com.dev.kit.basemodule.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by cuiyan on 2018/9/28.
 */
public class Config {
    public static final String BASE_CACHE_DIR_NAME = "suiXueEdu";
    public static final String VIDEO_CACHE_DIR_NAME = BASE_CACHE_DIR_NAME + File.separator + "videos";
    public static final String IMG_CACHE_DIR_NAME = BASE_CACHE_DIR_NAME + File.separator + "pictures";

    public static synchronized String getOutputImgDirPath() {
        String targetDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Config.IMG_CACHE_DIR_NAME;
        File dirFile = new File(targetDir);
        if (!dirFile.exists()) {
            if (!dirFile.mkdirs()) {
                return null;
            }
        }
        return dirFile.getAbsolutePath();
    }
}

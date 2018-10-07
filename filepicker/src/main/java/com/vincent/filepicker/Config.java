package com.vincent.filepicker;

/**
 * Author: cuiyan
 * Date:   18/10/7 11:07
 * Desc:
 */
public class Config {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_AUDIO = 2;
    public static final int TYPE_FILE = 3;
    public static final int DEFAULT_MAX_SELECT_NUMBER = 9;
    public static final String IS_TAKEN_AUTO_SELECTED = "IsTakenAutoSelected";
    public static final String NEED_CAMERA = "IsNeedCamera";
    public static final String NEED_FOLDER_LIST = "needFolderList";
    public static final String MAX_SELECT_NUMBER = "maxSelectNumber";

    // for video and audio
    public static final String MAX_DURATION = "maxDuration";
    public static final String MIN_DURATION = "minDuration";
    public static final String ONLY_MP4 = "onlyMp4";
}

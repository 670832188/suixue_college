package com.vincent.filepicker.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.vincent.filepicker.activity.VideoPickActivity;

import static com.vincent.filepicker.Config.IS_TAKEN_AUTO_SELECTED;
import static com.vincent.filepicker.Config.MAX_DURATION;
import static com.vincent.filepicker.Config.MAX_SELECT_NUMBER;
import static com.vincent.filepicker.Config.MIN_DURATION;
import static com.vincent.filepicker.Config.NEED_CAMERA;
import static com.vincent.filepicker.Config.NEED_FOLDER_LIST;
import static com.vincent.filepicker.Config.ONLY_MP4;

/**
 * Author: cuiyan
 * Date:   18/10/7 10:46
 * Desc:
 */
public class VideoSelectBuilder {
    // 视频最大时长：s
    private int maxDuration;
    // 视频最小时长：s
    private int minDuration;
    private int maxSelectNumber;
    private boolean needCamera;
    private boolean needFolderList;
    private boolean onlyMp4;
    private boolean isTakenAutoSelected;
    private int requestCode;
    private FragmentActivity activity;

    public VideoSelectBuilder(FragmentActivity activity, int requestCode) {
        this.activity = activity;
        this.requestCode = requestCode;
    }

    public VideoSelectBuilder setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
        return this;
    }

    public VideoSelectBuilder setMinDuration(int minDuration) {
        this.minDuration = minDuration;
        return this;
    }

    public VideoSelectBuilder setMaxSelectNumber(int maxSelectNumber) {
        this.maxSelectNumber = maxSelectNumber;
        return this;
    }

    public VideoSelectBuilder needCamera(boolean needCamera) {
        this.needCamera = needCamera;
        return this;
    }

    public VideoSelectBuilder isTakenAutoSelected(boolean isTakenAutoSelected) {
        this.isTakenAutoSelected = isTakenAutoSelected;
        return this;
    }

    public VideoSelectBuilder onlyMp4(boolean onlyMp4) {
        this.onlyMp4 = onlyMp4;
        return this;
    }

    public void start() {
        Intent intent = new Intent(activity, VideoPickActivity.class);
        if (maxDuration <= 0) {
            maxDuration = Integer.MAX_VALUE;
        }
        if (minDuration >= maxDuration) {
            minDuration = 0;
        }
        intent.putExtra(MAX_DURATION, maxDuration);
        intent.putExtra(MIN_DURATION, minDuration);
        intent.putExtra(MAX_SELECT_NUMBER, maxSelectNumber);
        intent.putExtra(NEED_CAMERA, needCamera);
        intent.putExtra(IS_TAKEN_AUTO_SELECTED, isTakenAutoSelected);
        intent.putExtra(NEED_FOLDER_LIST, needFolderList);
        intent.putExtra(ONLY_MP4, onlyMp4);
        activity.startActivityForResult(intent, requestCode);
    }

}

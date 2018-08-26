package com.suixue.edu.college;

import android.app.Application;

import com.suixue.edu.college.util.PreferenceUtil;

/**
 * Author: cuiyan
 * Date:   18/8/26 22:51
 * Desc:
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        PreferenceUtil.init(this);
    }
}

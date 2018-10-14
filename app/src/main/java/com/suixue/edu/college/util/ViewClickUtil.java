package com.suixue.edu.college.util;

import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Author: cuiyan
 * Date:   18/10/14 18:54
 * Desc:
 */
public class ViewClickUtil {
    /**
     * 防止连续点击
     *
     * @param view            被点击的view
     * @param minTimeInterval 相邻两次点击事件最小时间间隔，单位毫秒
     */
    public static synchronized void onViewClick(final View view, int minTimeInterval, final OnClickCallBack clickCallBack) {
        RxView.clicks(view).throttleFirst(minTimeInterval, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (clickCallBack != null) {
                            clickCallBack.onClick(view);
                        }
                    }
                });
    }

    public interface OnClickCallBack {
        void onClick(View view);
    }
}

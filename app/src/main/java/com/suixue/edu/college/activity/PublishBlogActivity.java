package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.kit.basemodule.activity.BaseStateViewActivity;
import com.suixue.edu.college.R;

/**
 * Created by cuiyan on 2018/9/17.
 */
public class PublishBlogActivity extends BaseStateViewActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showContent(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup contentRoot) {
        return inflater.inflate(R.layout.activity_publish_blog, contentRoot, false);
    }
}

package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.kit.basemodule.activity.BaseStateViewActivity;
import com.suixue.edu.college.R;

/**
 * 游客模式用户在此页面选择感兴趣的消息分类
 * Created by cuiyan on 2018/9/5.
 */
public class VisitorInterestActivity extends BaseStateViewActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showContent(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup contentRoot) {
        return inflater.inflate(R.layout.activity_visitor_interest, contentRoot, false);
    }

    private void init() {

    }
}

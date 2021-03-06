package com.suixue.edu.college.activity;

import android.content.Intent;
import android.os.Bundle;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.suixue.edu.college.R;
import com.suixue.edu.college.util.PreferenceUtil;

public class TransferActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        init();
    }

    private void init() {
        if (!PreferenceUtil.isVisitorMode() && PreferenceUtil.getUserInfo() == null) {
            startActivity(new Intent(this, SplashActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }
}

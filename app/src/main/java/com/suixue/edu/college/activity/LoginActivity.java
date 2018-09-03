package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.view.View;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.view.AutoLinkStyleTextView;
import com.suixue.edu.college.R;

/**
 * Created by cuiyan on 2018/9/3.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    private void init() {
        setOnClickListener(R.id.iv_left, this);
        setOnClickListener(R.id.tv_right, this);
        AutoLinkStyleTextView tvProtocolTip = findViewById(R.id.tv_protocol_tip);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left: {
                finish();
                break;
            }
            case R.id.tv_right: {
                break;
            }
        }
    }
}

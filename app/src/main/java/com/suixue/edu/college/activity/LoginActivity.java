package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.view.AutoLinkStyleTextView;
import com.suixue.edu.college.R;
import com.suixue.edu.college.config.Constants;
import com.suixue.edu.college.util.PreferenceUtil;

/**
 * Created by cuiyan on 2018/9/3.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        setOnClickListener(R.id.iv_left, this);
        setOnClickListener(R.id.tv_right, this);
        AutoLinkStyleTextView tvProtocolTip = findViewById(R.id.tv_protocol_tip);
        final String[] protocolName = getResources().getStringArray(R.array.protocol_name_array);
        tvProtocolTip.setClickSpanTextValues(new AutoLinkStyleTextView.ClickCallBack() {
            @Override
            public void onClick(int position) {
                showToast(protocolName[position]);
            }
        }, protocolName);
        if (!TextUtils.isEmpty(PreferenceUtil.getStringValue(Constants.DAY_OF_BIRTH, ""))) {

        }
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

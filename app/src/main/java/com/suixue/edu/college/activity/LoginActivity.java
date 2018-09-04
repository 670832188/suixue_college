package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.view.AutoLinkStyleTextView;
import com.suixue.edu.college.R;
import com.suixue.edu.college.config.Constants;
import com.suixue.edu.college.util.PreferenceUtil;

/**
 * Created by cuiyan on 2018/9/3.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener , View.OnFocusChangeListener{
    private EditText etYearOfBirth;
    private EditText etPhone;
    private EditText etSecurityCode;
    private EditText etUserName;
    private TextView tvSendSecurityCode;
    private ImageView ivDivider1;
    private ImageView ivDivider2;
    private ImageView ivDivider3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        setOnClickListener(R.id.iv_left, this);
        setOnClickListener(R.id.tv_right, this);

        String loginMode = getIntent().getStringExtra(Constants.LOGIN_MODE);
        if (Constants.LOGIN_MODE_VISITOR.equals(loginMode)) {
            setVisibility(R.id.ll_day_of_birth, View.VISIBLE);
            AutoLinkStyleTextView tvProtocolTip = findViewById(R.id.tv_protocol_tip);
            final String[] protocolName = getResources().getStringArray(R.array.protocol_name_array);
            tvProtocolTip.setClickSpanTextValues(new AutoLinkStyleTextView.ClickCallBack() {
                @Override
                public void onClick(int position) {
                    showToast(protocolName[position]);
                }
            }, protocolName);
            tvProtocolTip.setHighlightColor(getResources().getColor(R.color.color_transparent));
        } else {
            setVisibility(R.id.ll_day_of_birth, View.GONE);
            setVisibility(R.id.rl_register, View.VISIBLE);
            ivDivider1 = findViewById(R.id.divider_1);
            ivDivider2 = findViewById(R.id.divider_2);
            ivDivider3 = findViewById(R.id.divider_3);
            etPhone = findViewById(R.id.et_phone);
            etSecurityCode = findViewById(R.id.et_security_code);
            etUserName = findViewById(R.id.et_user_name);
            etPhone.setOnFocusChangeListener(this);
            etSecurityCode.setOnFocusChangeListener(this);
            etUserName.setOnFocusChangeListener(this);
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_phone:{
                ivDivider1.setImageResource(hasFocus ? R.drawable.bg_common_et_bottom_line_focused : R.drawable.bg_common_et_bottom_line_unfocused);
                break;
            }case R.id.et_security_code:{
                ivDivider2.setImageResource(hasFocus ? R.drawable.bg_common_et_bottom_line_focused : R.drawable.bg_common_et_bottom_line_unfocused);
                break;
            }
            case R.id.et_user_name:{
                ivDivider3.setImageResource(hasFocus ? R.drawable.bg_common_et_bottom_line_focused : R.drawable.bg_common_et_bottom_line_unfocused);
                break;
            }
        }
    }
}

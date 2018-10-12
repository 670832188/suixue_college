package com.suixue.edu.college.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.netRequest.Configs.Config;
import com.dev.kit.basemodule.netRequest.model.BaseController;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestCallback;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestSubscriber;
import com.dev.kit.basemodule.netRequest.util.BaseServiceUtil;
import com.dev.kit.basemodule.result.BaseResult;
import com.dev.kit.basemodule.util.LogUtil;
import com.dev.kit.basemodule.util.StringUtil;
import com.dev.kit.basemodule.view.AutoLinkStyleTextView;
import com.suixue.edu.college.R;
import com.suixue.edu.college.config.ApiService;
import com.suixue.edu.college.config.Constants;
import com.suixue.edu.college.entity.UserInfo;
import com.suixue.edu.college.util.PreferenceUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * 注册页面
 * Created by cuiyan on 2018/9/3.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final int defaultCountDownValue = 60 * 1000;
    private CountDownTimer countDownTimer;
    private EditText etYearOfBirth;
    private EditText etMobile;
    private EditText etSecurityCode;
    private EditText etUserName;
    private TextView tvSendSecurityCode;
    private ImageView ivDivider1;
    private ImageView ivDivider2;
    private ImageView ivDivider3;
    private String registerMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        setHideKeyBoardTouchOutside();
        setOnClickListener(R.id.iv_left, this);
        setOnClickListener(R.id.tv_right, this);

        registerMode = getIntent().getStringExtra(Constants.KEY_REGISTER_MODE);
        if (Constants.VALUE_REGISTER_MODE_VISITOR.equals(registerMode)) {  // 游客模式
            setVisibility(R.id.ll_visitor_register, View.VISIBLE);
            AutoLinkStyleTextView tvProtocolTip = findViewById(R.id.tv_protocol_tip);
            final String[] protocolName = getResources().getStringArray(R.array.protocol_name_array);
            tvProtocolTip.setClickSpanTextValues(new AutoLinkStyleTextView.ClickCallBack() {
                @Override
                public void onClick(int position) {
                    showToast(protocolName[position]);
                }
            }, protocolName);
            tvProtocolTip.setHighlightColor(getResources().getColor(R.color.color_transparent));
            etYearOfBirth = findViewById(R.id.et_year_of_birth);
        } else {  // 注册或登录
            setText(R.id.tv_right, R.string.action_complete);
            setVisibility(R.id.ll_visitor_register, View.GONE);
            setVisibility(R.id.rl_user_register, View.VISIBLE);
            ivDivider1 = findViewById(R.id.divider_1);
            ivDivider2 = findViewById(R.id.divider_2);
            ivDivider3 = findViewById(R.id.divider_3);
            etMobile = findViewById(R.id.et_mobile);
            etSecurityCode = findViewById(R.id.et_security_code);
            etUserName = findViewById(R.id.et_user_name);
            etMobile.setOnFocusChangeListener(this);
            etSecurityCode.setOnFocusChangeListener(this);
            etUserName.setOnFocusChangeListener(this);
            tvSendSecurityCode = findViewById(R.id.tv_get_security_code);
            tvSendSecurityCode.setOnClickListener(this);
            setOnClickListener(R.id.tv_login, this);
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
                if (Constants.VALUE_REGISTER_MODE_VISITOR.equals(registerMode)) {
                    registerByVisitorMode();
                } else {
                    register();
                }
                break;
            }
            case R.id.tv_get_security_code: {
                getSecurityCode();
                break;
            }
            case R.id.tv_login: {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            }
        }
    }

    // 游客模式注册
    private void registerByVisitorMode() {
        String yearOfBirth = etYearOfBirth.getText().toString().trim();
        if (TextUtils.isEmpty(yearOfBirth)) {
            showToast(R.string.tip_please_input_year_of_birth);
            return;
        }
        int year = StringUtil.stringToInteger(yearOfBirth);
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        if (year <= currentYear - 100 || year <= 0 || year >= currentYear) {
            showToast(R.string.tip_year_of_birth_error);
            return;
        }
        PreferenceUtil.setVisitorYearOfBirth(yearOfBirth);
        Intent intent = new Intent(this, InterestActivity.class);
        intent.putExtra(InterestActivity.KEY_CALLER_SOURCE, InterestActivity.VALUE_CALLER_SOURCE_REGISTER);
        startActivity(intent);
    }

    // 获取短信验证码
    private void getSecurityCode() {
        String mobileNumber = etMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobileNumber)) {
            showToast(R.string.tip_please_input_mobile_number);
            return;
        }
        NetRequestSubscriber<BaseResult> subscriber = new NetRequestSubscriber<>(new NetRequestCallback<BaseResult>() {
            @Override
            public void onSuccess(@NonNull BaseResult result) {
                if (Config.REQUEST_SUCCESS_CODE.equals(result.getCode())) {
                    startCountDown();
                }
            }

            @Override
            public void onResultNull() {
                showToast(R.string.tip_net_request_failed);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.error_net_request_failed);
                LogUtil.e(throwable);
            }
        }, this, true, null);
        Observable<BaseResult> observable = BaseServiceUtil.createService(ApiService.class).getSecurityCode(mobileNumber);
        BaseController.sendRequest(this, subscriber, observable);
    }


    private void startCountDown() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(defaultCountDownValue, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished == 0) {
                        tvSendSecurityCode.setText(R.string.action_send_security_code);
                        tvSendSecurityCode.setEnabled(true);
                    } else {
                        tvSendSecurityCode.setText(String.format(getString(R.string.time_count_down), millisUntilFinished / 1000));
                    }
                }

                @Override
                public void onFinish() {
                    tvSendSecurityCode.setText(R.string.action_send_security_code);
                    tvSendSecurityCode.setEnabled(true);
                }
            };
        }
        tvSendSecurityCode.setEnabled(false);
        countDownTimer.start();
    }

    private void register() {
        String mobileNumber = etMobile.getText().toString().trim();
        String securityCode = etSecurityCode.getText().toString().trim();
        String userName = etUserName.getText().toString().trim();
        if (TextUtils.isEmpty(mobileNumber)) {
            showToast(R.string.tip_please_input_mobile_number);
            return;
        }
        if (TextUtils.isEmpty(securityCode)) {
            showToast(R.string.tip_please_input_security_code);
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            showToast(R.string.tip_please_input_user_name);
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobileNumber);
        params.put("securityCode", securityCode);
        params.put("userName", userName);
        NetRequestSubscriber<BaseResult<UserInfo>> subscriber = new NetRequestSubscriber<>(new NetRequestCallback<BaseResult<UserInfo>>() {
            @Override
            public void onSuccess(@NonNull BaseResult<UserInfo> result) {
                if (Config.REQUEST_SUCCESS_CODE.equals(result.getCode())) {
                    if (result.getData() != null) {
                        PreferenceUtil.clearVisitorData();
                        PreferenceUtil.saveUserInfo(result.getData());
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    } else {
                        showToast(R.string.error_net_request_failed);
                    }
                }
            }

            @Override
            public void onResultNull() {
                showToast(R.string.error_net_request_failed);
            }

            @Override
            public void onError(Throwable throwable) {
                showToast(R.string.error_net_request_failed);
                LogUtil.e(throwable);
            }
        }, this, true, null);
        Observable<BaseResult<UserInfo>> observable = BaseServiceUtil.createService(ApiService.class).register(params);
        BaseController.sendRequest(this, subscriber, observable);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_mobile: {
                ivDivider1.setImageResource(hasFocus ? R.drawable.bg_common_et_bottom_line_focused : R.drawable.bg_common_et_bottom_line_unfocused);
                break;
            }
            case R.id.et_security_code: {
                ivDivider2.setImageResource(hasFocus ? R.drawable.bg_common_et_bottom_line_focused : R.drawable.bg_common_et_bottom_line_unfocused);
                break;
            }
            case R.id.et_user_name: {
                ivDivider3.setImageResource(hasFocus ? R.drawable.bg_common_et_bottom_line_focused : R.drawable.bg_common_et_bottom_line_unfocused);
                break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}

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
import com.suixue.edu.college.R;
import com.suixue.edu.college.config.ApiService;
import com.suixue.edu.college.entity.UserInfo;
import com.suixue.edu.college.util.PreferenceUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by cuiyan on 2018/9/5.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final int defaultCountDownValue = 60 * 1000;
    private CountDownTimer countDownTimer;
    private EditText etMobile;
    private EditText etSecurityCode;
    private TextView tvGetSecurityCode;
    private ImageView ivDivider1;
    private ImageView ivDivider2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        setHideKeyBoardTouchOutside();
        setOnClickListener(R.id.iv_left, this);
        setOnClickListener(R.id.tv_get_security_code, this);
        setOnClickListener(R.id.bt_login, this);
        etMobile = findViewById(R.id.et_mobile);
        etSecurityCode = findViewById(R.id.et_security_code);
        tvGetSecurityCode = findViewById(R.id.tv_get_security_code);
        ivDivider1 = findViewById(R.id.divider_1);
        ivDivider2 = findViewById(R.id.divider_2);
        etMobile.setOnFocusChangeListener(this);
        etSecurityCode.setOnFocusChangeListener(this);
    }

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
                LogUtil.e(throwable);
            }
        }, this, true, null);
        Observable<BaseResult<UserInfo>> observable = BaseServiceUtil.createService(ApiService.class).getSecurityCode(mobileNumber);
        BaseController.sendRequest(this, subscriber, observable);
    }


    private void startCountDown() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(defaultCountDownValue, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (millisUntilFinished == 0) {
                        tvGetSecurityCode.setText(R.string.action_send_security_code);
                        tvGetSecurityCode.setEnabled(true);
                    } else {
                        tvGetSecurityCode.setText(String.format(getString(R.string.time_count_down), millisUntilFinished / 1000));
                    }
                }

                @Override
                public void onFinish() {
                    tvGetSecurityCode.setText(R.string.action_send_security_code);
                    tvGetSecurityCode.setEnabled(true);
                }
            };
        }
        tvGetSecurityCode.setEnabled(false);
        countDownTimer.start();
    }

    private void login() {
        String mobileNumber = etMobile.getText().toString().trim();
        String securityCode = etSecurityCode.getText().toString().trim();
        if (TextUtils.isEmpty(mobileNumber)) {
            showToast(R.string.tip_please_input_mobile_number);
            return;
        }
        if (TextUtils.isEmpty(securityCode)) {
            showToast(R.string.tip_please_input_security_code);
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobileNumber);
        params.put("securityCode", securityCode);
        NetRequestSubscriber<BaseResult<UserInfo>> subscriber = new NetRequestSubscriber<>(new NetRequestCallback<BaseResult<UserInfo>>() {
            @Override
            public void onSuccess(@NonNull BaseResult<UserInfo> result) {
                if (Config.REQUEST_SUCCESS_CODE.equals(result.getCode())) {
                    PreferenceUtil.saveUserInfo(result.getData());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
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
        Observable<BaseResult<UserInfo>> observable = BaseServiceUtil.createService(ApiService.class).login(params);
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
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left: {
                finish();
                break;
            }
            case R.id.tv_get_security_code: {
                getSecurityCode();
                break;
            }
            case R.id.bt_login: {
                login();
                break;
            }
        }
    }
}

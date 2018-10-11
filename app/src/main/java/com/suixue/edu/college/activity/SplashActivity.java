package com.suixue.edu.college.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.surpport.CommonPagerAdapter;
import com.dev.kit.basemodule.view.CustomIndicator;
import com.suixue.edu.college.R;
import com.suixue.edu.college.config.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * Author: cuiyan
 * Date:   18/8/23 23:26
 * Desc:
 */
public class SplashActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        List<String> guideTipList = Arrays.asList(getResources().getStringArray(R.array.guide_tip_array));
        ViewPager viewPager = findViewById(R.id.vp_guide);
        viewPager.setAdapter(new CommonPagerAdapter<String>(guideTipList) {

            @Override
            public void renderItemView(@NonNull View itemView, int position) {
                TextView tvTip = itemView.findViewById(R.id.tv_guide_tip);
                tvTip.setText(getBindItemData(position));
            }

            @NonNull
            @Override
            public View getPageItemView(@NonNull ViewGroup container, int position) {
                return LayoutInflater.from(SplashActivity.this).inflate(R.layout.item_guide, container, false);
            }
        });
        CustomIndicator indicator = findViewById(R.id.indicator);
        indicator.bindViewPager(viewPager);
        setOnClickListener(R.id.bt_start_browse, this);
        setOnClickListener(R.id.bt_login, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login: {
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra(Constants.KEY_REGISTER_MODE, Constants.VALUE_REGISTER_MODE_USER);
                startActivity(intent);
                finish();
                break;
            }

            case R.id.bt_start_browse: {
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra(Constants.KEY_REGISTER_MODE, Constants.VALUE_REGISTER_MODE_VISITOR);
                startActivity(intent);
                finish();
                break;
            }
        }
    }
}

package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.surpport.CommonPagerAdapter;
import com.suixue.edu.college.R;

import java.util.Arrays;
import java.util.List;

/**
 * Author: cuiyan
 * Date:   18/8/23 23:26
 * Desc:
 */
public class SplashActivity extends BaseActivity {

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
    }
}

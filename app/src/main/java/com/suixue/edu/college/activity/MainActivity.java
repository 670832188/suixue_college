package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.surpport.FragmentAdapter;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.suixue.edu.college.R;
import com.suixue.edu.college.fragment.MainFragment;
import com.suixue.edu.college.fragment.MessageFragment;
import com.suixue.edu.college.fragment.PersonalFragment;
import com.suixue.edu.college.fragment.SearchFragment;

/**
 * Created by cuiyan on 2018/9/12.
 */
public class MainActivity extends BaseActivity implements FragmentAdapter.FragmentFactory {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        final RadioButton rbHome = findViewById(R.id.rb_home);
        final RadioButton rbSearch = findViewById(R.id.rb_search);
        final RadioButton rbMsg = findViewById(R.id.rb_msg);
        final RadioButton rbPersonal = findViewById(R.id.rb_me);
        final ViewPager vpFrg = findViewById(R.id.vp_frg);
        vpFrg.setOffscreenPageLimit(3);
        final RadioGroup rgNav = findViewById(R.id.rg_nav);
        vpFrg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: {
                        rbHome.setChecked(true);
                        break;
                    }
                    case 1: {
                        rbSearch.setChecked(true);
                        break;
                    }
                    case 2: {
                        rbMsg.setChecked(true);
                        break;
                    }
                    default: {
                        rbPersonal.setChecked(true);
                        break;
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), this);
        vpFrg.setAdapter(fragmentAdapter);
        rgNav.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home: {
                        vpFrg.setCurrentItem(0, false);
                        break;
                    }
                    case R.id.rb_search: {
                        vpFrg.setCurrentItem(1, false);
                        break;
                    }
                    case R.id.rb_msg: {
                        vpFrg.setCurrentItem(2, false);
                        break;
                    }
                    case R.id.rb_me: {
                        vpFrg.setCurrentItem(3, false);
                        break;
                    }
                }
            }
        });
        rbHome.setChecked(true);
    }

    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0: {
                fragment = new MainFragment();
                break;
            }
            case 1: {
                fragment = new SearchFragment();
                break;
            }
            case 2: {
                fragment = new MessageFragment();
                break;
            }
            default: {
                fragment = new PersonalFragment();
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getFragmentCount() {
        return 4;
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}

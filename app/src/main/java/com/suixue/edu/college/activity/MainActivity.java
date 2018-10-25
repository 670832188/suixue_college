package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.surpport.FragmentAdapter;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
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
    private ViewPager vpFrg;
    private FragmentAdapter fragmentAdapter;
    private OnBlogTagClickListener onBlogTagClickListener;
    private BottomBar navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        onBlogTagClickListener = new OnBlogTagClickListener() {
            @Override
            public void onTagClick(String blogTag) {
                vpFrg.setCurrentItem(1, false);
                SearchFragment searchFragment = (SearchFragment) fragmentAdapter.getItem(1);
                searchFragment.searchBlogList(blogTag);
            }
        };
        navBar = findViewById(R.id.nav_bar);
        vpFrg = findViewById(R.id.vp_frg);
        vpFrg.setOffscreenPageLimit(3);
        vpFrg.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: {
                        navBar.selectTabAtPosition(0, true);
                        break;
                    }
                    case 1: {
                        navBar.selectTabAtPosition(1, true);
                        break;
                    }
                    case 2: {
                        navBar.selectTabAtPosition(2, true);
                        break;
                    }
                    default: {
                        navBar.selectTabAtPosition(3, true);
                        break;
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), this);
        vpFrg.setAdapter(fragmentAdapter);
        navBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                switch (tabId) {
                    case R.id.tab_home: {
                        vpFrg.setCurrentItem(0, false);
                        break;
                    }
                    case R.id.tab_search: {
                        vpFrg.setCurrentItem(1, false);
                        break;
                    }
                    case R.id.tab_msg: {
                        vpFrg.setCurrentItem(2, false);
                        break;
                    }
                    case R.id.tab_me: {
                        vpFrg.setCurrentItem(3, false);
                        break;
                    }
                }
            }
        });
        navBar.selectTabAtPosition(0, true);
    }

    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0: {
                fragment = new MainFragment();
                ((MainFragment) fragment).setOnBlogTagClickListener(onBlogTagClickListener);
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
                Bundle arg = new Bundle();
                arg.putBoolean(PersonalFragment.IS_BLOGGER_SELF_BROWSE, true);
                fragment = new PersonalFragment();
                fragment.setArguments(arg);
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

    public interface OnBlogTagClickListener {
        void onTagClick(String blogTag);
    }
}

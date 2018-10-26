package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.surpport.FragmentAdapter;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.suixue.edu.college.R;
import com.suixue.edu.college.config.Constants;
import com.suixue.edu.college.fragment.MainFragment;
import com.suixue.edu.college.fragment.MessageFragment;
import com.suixue.edu.college.fragment.PersonalFragment;
import com.suixue.edu.college.fragment.SearchFragment;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;

/**
 * Created by cuiyan on 2018/9/12.
 */
public class MainActivity extends BaseActivity implements FragmentAdapter.FragmentFactory {
    private ViewPager vpFrg;
    private FragmentAdapter fragmentAdapter;
    private OnBlogTagClickListener onBlogTagClickListener;
    private NavigationController navigationController;
    private final int[] COLORS = {0xFF455A64, 0xFF25540e, 0xFF00796B, 0xFF795548, 0xFF5B4947};

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
        vpFrg = findViewById(R.id.vp_frg);
        vpFrg.setOffscreenPageLimit(3);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), this);
        vpFrg.setAdapter(fragmentAdapter);
        PageNavigationView navigationView = findViewById(R.id.nav_bar);
        navigationController = navigationView.material()
                .addItem(R.mipmap.ic_nav_home_selected, getString(R.string.tab_home), COLORS[0])
                .addItem(R.mipmap.ic_nav_search_selected, getString(R.string.tab_search), COLORS[1])
                .addItem(R.mipmap.ic_nav_msg_selected, getString(R.string.tab_msg), COLORS[2])
                .addItem(R.mipmap.ic_nav_me_selected, getString(R.string.tab_me), COLORS[3])
                .enableAnimateLayoutChanges()
                .setDefaultColor(getResources().getColor(R.color.color_common_gray))
                .build();
        navigationController.setMessageNumber(2, 5);
        navigationController.setupWithViewPager(vpFrg);
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
                arg.putString(Constants.KEY_BLOGGER_ID, "121212");
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

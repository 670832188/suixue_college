package com.suixue.edu.college.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.kit.basemodule.fragment.BaseFragment;
import com.dev.kit.basemodule.surpport.FragmentAdapter;
import com.suixue.edu.college.R;
import com.suixue.edu.college.view.GradualTitleView;

/**
 * Created by cuiyan on 2018/9/12.
 */
public class PersonalFragment extends BaseFragment implements FragmentAdapter.FragmentFactory {

    public static final String IS_BLOGGER_SELF_BROWSE = "isBloggerSelfBrowse";
    // 标识访问来源：访问者/博主本人
    private boolean isBloggerSelfBrowse;
    private View rootView;
    private ViewPager vpFrg;
    private String[] tabTitleArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frg_personal, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        Bundle arg = getArguments();
        if (arg != null) {
            isBloggerSelfBrowse = arg.getBoolean(IS_BLOGGER_SELF_BROWSE);
        }
        if (!isBloggerSelfBrowse) {
            rootView.findViewById(R.id.tool_bar).setVisibility(View.VISIBLE);
            final GradualTitleView titleView = rootView.findViewById(R.id.title_view);
            final int coverHeight = getResources().getDimensionPixelSize(R.dimen.personal_top_cover_height);
            AppBarLayout appBarLayout = rootView.findViewById(R.id.app_bar_layout);
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    float appBarLayoutOffsetRatio = Math.abs((float) verticalOffset / (coverHeight - titleView.getHeight()));
                    titleView.changeRatio((float) Math.pow(appBarLayoutOffsetRatio, 3));
                }
            });
            titleView.setOnLeftBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity  = getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                }
            });
        }
        TabLayout tabLayout = rootView.findViewById(R.id.sliding_tabs);
        tabTitleArray = getResources().getStringArray(R.array.personal_tab_title);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), this) {
            @Override
            public String getPageTitle(int position) {
                return tabTitleArray[position];
            }
        };
        vpFrg = rootView.findViewById(R.id.vp_frg);
        vpFrg.setOffscreenPageLimit(3);
        vpFrg.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(vpFrg);
        tabLayout.clearOnTabSelectedListeners();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpFrg.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0: {
                fragment = new PersonalCourseFragment();
                break;
            }
            case 1: {
                fragment = new PersonalCourseFragment();
                break;
            }
            case 2: {
                fragment = new PersonalCourseFragment();
                break;
            }
            default: {
                fragment = new PersonalCourseFragment();
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getFragmentCount() {
        return isBloggerSelfBrowse ? 4 : 3;
    }
}

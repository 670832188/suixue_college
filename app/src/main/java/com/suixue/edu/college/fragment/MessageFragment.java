package com.suixue.edu.college.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.kit.basemodule.fragment.BaseFragment;
import com.dev.kit.basemodule.surpport.FragmentAdapter;
import com.suixue.edu.college.R;

/**
 * Created by cuiyan on 2018/9/12.
 */
public class MessageFragment extends BaseFragment implements FragmentAdapter.FragmentFactory {
    private TabLayout tabLayout;
    private ViewPager vpFrg;
    private String[] tabTitleArray;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frg_message, container, false);
        tabLayout = rootView.findViewById(R.id.sliding_tabs);
        vpFrg = rootView.findViewById(R.id.vp_frg);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        tabTitleArray = getResources().getStringArray(R.array.msg_tab_title);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), this) {
            @Override
            public String getPageTitle(int position) {
                return tabTitleArray[position];
            }
        };
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
        if (position == 0) {
            fragment = new DynamicFragment();
        } else {
            fragment = new ChatFragment();
        }
        return fragment;
    }

    @Override
    public int getFragmentCount() {
        return 2;
    }
}

package com.dev.kit.basemodule.surpport;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuiyan on 16-10-19.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private FragmentFactory fragmentFactory;
    private SparseArray<Fragment> fragmentSparseArray = new SparseArray<>();

    public FragmentAdapter(FragmentManager fm, FragmentFactory fragmentFactory) {
        super(fm);
        this.fragmentFactory = fragmentFactory;
    }

    @Override
    public int getCount() {
        return fragmentFactory.getFragmentCount();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragmentSparseArray.get(position);
        if (fragment == null) {
            fragment = fragmentFactory.createFragment(position);
            fragmentSparseArray.put(position, fragment);
        }
        return fragment;
    }

    public interface FragmentFactory {
        Fragment createFragment(int position);

        int getFragmentCount();
    }
}

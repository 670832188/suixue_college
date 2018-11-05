package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.suixue.edu.college.R;
import com.suixue.edu.college.config.Constants;
import com.suixue.edu.college.fragment.PersonalFragment;

/**
 * Created by cuiyan on 2018/10/29.
 */
public class BloggerActivity extends BaseActivity {
    PersonalFragment personalFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blooger);
        init();
    }

    private void init() {
        String bloggerId = getIntent().getStringExtra(Constants.KEY_BLOGGER_ID);
        Bundle arg = new Bundle();
        arg.putString(Constants.KEY_BLOGGER_ID, bloggerId);
        personalFragment = new PersonalFragment();
        personalFragment.setArguments(arg);
        personalFragment.setUserVisibleHint(false);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();
        tc.add(R.id.fl_frg_container, personalFragment);
        tc.commitNow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!personalFragment.getUserVisibleHint()) {
            personalFragment.setUserVisibleHint(true);
        }
    }
}

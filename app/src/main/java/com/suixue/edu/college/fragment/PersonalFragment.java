package com.suixue.edu.college.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.kit.basemodule.fragment.BaseFragment;
import com.suixue.edu.college.R;

/**
 * Created by cuiyan on 2018/9/12.
 */
public class PersonalFragment extends BaseFragment {

    private View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frg_personal, container, false);
        return rootView;
    }
}

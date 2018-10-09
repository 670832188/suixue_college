package com.suixue.edu.college.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.dev.kit.basemodule.fragment.BaseStateFragment;
import com.suixue.edu.college.R;

/**
 * Created by cuiyan on 2018/10/9.
 */
public class ChatFragment extends BaseStateFragment {
    private View contentView;
    private RecyclerView rvChatMessage;
    @NonNull
    @Override
    public View createContentView(LayoutInflater inflater, FrameLayout flRootContainer) {
        contentView = inflater.inflate(R.layout.frg_chat, flRootContainer, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {}
}

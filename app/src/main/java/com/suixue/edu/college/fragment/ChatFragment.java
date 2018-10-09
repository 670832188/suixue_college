package com.suixue.edu.college.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.dev.kit.basemodule.fragment.BaseStateFragment;
import com.dev.kit.basemodule.surpport.RecyclerDividerDecoration;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.adapter.ChatMessageAdapter;
import com.suixue.edu.college.entity.ChatMessageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.dkzwm.widget.srl.SmoothRefreshLayout;

/**
 * Created by cuiyan on 2018/10/9.
 */
public class ChatFragment extends BaseStateFragment {
    private View contentView;
    private RecyclerView rvChatMessage;
    private SmoothRefreshLayout refreshLayout;
    private ChatMessageAdapter adapter;

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

    private void init() {
        refreshLayout = contentView.findViewById(R.id.refresh_layout);
        rvChatMessage = contentView.findViewById(R.id.rv_chat_msg);
        rvChatMessage.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChatMessage.addItemDecoration(new RecyclerDividerDecoration(RecyclerDividerDecoration.DIVIDER_TYPE_HORIZONTAL, getResources().getColor(R.color.color_transparent), DisplayUtil.dp2px(15)));
        adapter = new ChatMessageAdapter(getContext(), new ArrayList<ChatMessageInfo>());
        rvChatMessage.setAdapter(adapter);
        getChatMsg();
    }

    private void getChatMsg() {
        generateTestData();
    }

    private void generateTestData() {
        List<ChatMessageInfo> msgList = new ArrayList<>();
        int size = 15;
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            ChatMessageInfo info = new ChatMessageInfo();
            if (random.nextInt() % 2 == 0) {
                info.setType(1);
                info.setContent("这是我发给您的消息啊");
                info.setAvatarUrl("http://img19.3lian.com/d/file/201803/05/635adb96f8a4c0d41e4292ad01b52044.png");
            } else {
                info.setType(2);
                info.setContent("这是我回复您的消息啊");
                info.setAvatarUrl("http://img19.3lian.com/d/file/201803/05/fa6cf18ea93c86703344a2b95c437048.png");
            }
            msgList.add(info);
        }
        adapter.insertData(0, msgList);
    }
}

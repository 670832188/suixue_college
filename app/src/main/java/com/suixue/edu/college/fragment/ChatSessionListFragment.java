package com.suixue.edu.college.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.dev.kit.basemodule.fragment.BaseStateFragment;
import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerDividerDecoration;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.activity.ChatMessageListActivity;
import com.suixue.edu.college.adapter.ChatSessionListAdapter;
import com.suixue.edu.college.entity.ChatSessionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by cuiyan on 2018/10/9.
 */
public class ChatSessionListFragment extends BaseStateFragment {
    private ChatSessionListAdapter adapter;
    private RecyclerView rvSessions;

    @NonNull
    @Override
    public View createContentView(LayoutInflater inflater, FrameLayout flRootContainer) {
        View contentView = inflater.inflate(R.layout.frg_chat_session_list, flRootContainer, false);
        rvSessions = contentView.findViewById(R.id.rv_sessions);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        adapter = new ChatSessionListAdapter(getContext(), new ArrayList<ChatSessionInfo>());
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ChatSessionInfo info = adapter.getItem(position);
                Intent intent = new Intent(getContext(), ChatMessageListActivity.class);
                intent.putExtra(ChatMessageListActivity.COMMUNICATOR_NAME, info.getCommunicatorName());
                intent.putExtra(ChatMessageListActivity.SESSION_ID, info.getSessionId());
                startActivity(intent);
            }
        });
        rvSessions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSessions.addItemDecoration(new RecyclerDividerDecoration(RecyclerDividerDecoration.DIVIDER_TYPE_HORIZONTAL, getResources().getColor(R.color.color_common_light_gray), DisplayUtil.dp2px(0.5f)));
        rvSessions.setAdapter(adapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getChatSession();
        }
    }

    private void getChatSession() {
        generateTestData();
    }

    private void generateTestData() {
        List<ChatSessionInfo> msgList = new ArrayList<>();
        int size = 15;
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            ChatSessionInfo info = new ChatSessionInfo();
            if (random.nextInt() % 2 == 0) {
                info.setCommunicatorName("张三");
                info.setLatestMsgTime("晚上6:39");
                info.setHasUnreadMsg(true);
                info.setSessionId("10086");
                info.setLatestMsg("这是我发给您的消息啊");
                info.setCommunicatorAvatarUrl("http://img19.3lian.com/d/file/201803/05/635adb96f8a4c0d41e4292ad01b52044.png");
            } else {
                info.setCommunicatorName("李四");
                info.setLatestMsgTime("晚上6:39");
                info.setHasUnreadMsg(false);
                info.setSessionId("10086");
                info.setLatestMsg("这是我发给您的消息啊");
                info.setCommunicatorAvatarUrl("http://img19.3lian.com/d/file/201803/05/fa6cf18ea93c86703344a2b95c437048.png");
            }
            msgList.add(info);
        }
        adapter.insertData(0, msgList);
    }
}

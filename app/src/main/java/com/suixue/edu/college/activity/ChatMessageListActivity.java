package com.suixue.edu.college.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.kit.basemodule.activity.BaseStateViewActivity;
import com.dev.kit.basemodule.surpport.RecyclerDividerDecoration;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.adapter.ChatMessageAdapter;
import com.suixue.edu.college.entity.ChatMessageInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.extra.header.ClassicHeader;

/**
 * Created by cuiyan on 2018/10/9.
 */
public class ChatMessageListActivity extends BaseStateViewActivity implements View.OnClickListener {
    public static final String SESSION_ID = "sessionId";
    public static final String COMMUNICATOR_NAME = "communicatorName";
    private String sessionId;
    private RecyclerView rvChatMessage;
    private SmoothRefreshLayout refreshLayout;
    private ChatMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showContent(true);
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup contentRoot) {
        return inflater.inflate(R.layout.activity_chat_message_list, contentRoot, false);
    }

    private void init() {
        Intent intent = getIntent();
        sessionId = intent.getStringExtra(SESSION_ID);
        setText(R.id.tv_title, intent.getStringExtra(COMMUNICATOR_NAME));
        setOnClickListener(R.id.iv_left, this);
        refreshLayout = findViewById(R.id.refresh_layout);
        rvChatMessage = findViewById(R.id.rv_chat_msg);
        rvChatMessage.setLayoutManager(new LinearLayoutManager(this));
        rvChatMessage.addItemDecoration(new RecyclerDividerDecoration(RecyclerDividerDecoration.DIVIDER_TYPE_HORIZONTAL, getResources().getColor(R.color.color_transparent), DisplayUtil.dp2px(15)));
        adapter = new ChatMessageAdapter(this, new ArrayList<ChatMessageInfo>());
        rvChatMessage.setAdapter(adapter);
        refreshLayout.setHeaderView(new ClassicHeader(this));
        refreshLayout.setOnRefreshListener(new SmoothRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefreshBegin(boolean isRefresh) {
                getChatMsg();
            }

            @Override
            public void onRefreshComplete(boolean isSuccessful) {

            }
        });
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
        refreshLayout.refreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left: {
                finish();
                break;
            }
        }
    }
}

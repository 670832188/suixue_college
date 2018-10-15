package com.suixue.edu.college.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dev.kit.basemodule.activity.BaseStateViewActivity;
import com.dev.kit.basemodule.result.BaseResult;
import com.dev.kit.basemodule.surpport.RecyclerDividerDecoration;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.dev.kit.basemodule.util.LogUtil;
import com.dev.kit.basemodule.util.StringUtil;
import com.google.gson.Gson;
import com.suixue.edu.college.R;
import com.suixue.edu.college.adapter.ChatMessageAdapter;
import com.suixue.edu.college.entity.BaseListResult;
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
    private LinearLayout llEditContainer;
    private EditText etContent;
    private TextView tvSend;
    private int rootViewBottom;
    private View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showContent(true);
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup contentRoot) {
        contentView = inflater.inflate(R.layout.activity_chat_message_list, contentRoot, false);
        return contentView;
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
        rvChatMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyBoard();
                etContent.clearFocus();
                return false;
            }
        });
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (rootViewBottom == 0 && contentView.getHeight() > 0) {
                    rootViewBottom = contentView.getBottom();
                }
                Rect rect = new Rect();
                rvChatMessage.getGlobalVisibleRect(rect);
                if (rootViewBottom - rect.bottom > 400) { // 认为软键盘弹起
                    if (adapter.getItemCount() > 1) {
                        rvChatMessage.smoothScrollToPosition(adapter.getItemCount() - 1);
                    }
                }
            }
        });
        ClassicHeader header = new ClassicHeader(this);
        header.setReleaseToRefreshRes(R.string.tip_release_to_load_more);
        header.setRefreshingRes(R.string.tip_loading);
        header.setRefreshSuccessfulRes(R.string.tip_loading_complete);
        refreshLayout.setHeaderView(header);
        refreshLayout.setOnRefreshListener(new SmoothRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefreshBegin(boolean isRefresh) {
                getChatMsg();
            }

            @Override
            public void onRefreshComplete(boolean isSuccessful) {

            }
        });
        llEditContainer = findViewById(R.id.ll_edit_container);
        etContent = findViewById(R.id.et_content);
        tvSend = findViewById(R.id.tv_send);
        llEditContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = llEditContainer.getHeight();
                if (height > 0) {
                    tvSend.layout(tvSend.getLeft(), height - tvSend.getHeight() - llEditContainer.getPaddingBottom(), tvSend.getRight(), height - llEditContainer.getPaddingBottom());
                }
            }
        });
        tvSend.setOnClickListener(this);
        getChatMsg();
    }

    private void getChatMsg() {
        generateTestData();
    }

    private void generateTestData() {
        List<ChatMessageInfo> msgList = new ArrayList<>();
        int size = 5;
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
        if (!refreshLayout.isRefreshing()) {
            rvChatMessage.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
        refreshLayout.refreshComplete();
        BaseResult<BaseListResult<ChatMessageInfo>> baseResult = new BaseResult<>();
        BaseListResult<ChatMessageInfo> baseListResult = new BaseListResult<>();
        baseListResult.setDataList(msgList);
        baseListResult.setCurrentPageIndex(11);
        baseListResult.setHasMoreData(true);
        baseResult.setData(baseListResult);
        baseResult.setMsg("success");
        baseResult.setCode("1");
        LogUtil.e("mytag", new Gson().toJson(baseResult));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left: {
                finish();
                break;
            }
            case R.id.tv_send: {
                senMsg();
                break;
            }
        }
    }

    private void senMsg() {
        if (!StringUtil.isEmpty(etContent.getText().toString())) {
            ChatMessageInfo info = new ChatMessageInfo();
            info.setAvatarUrl("http://img19.3lian.com/d/file/201803/05/fa6cf18ea93c86703344a2b95c437048.png");
            info.setType(2);
            info.setContent(etContent.getText().toString());
            info.setCreatorName("我");
            etContent.setText("");
            adapter.appendItem(info, false);
            rvChatMessage.smoothScrollToPosition(adapter.getItemCount() - 1);
        }
    }
}

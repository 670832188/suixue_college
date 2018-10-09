package com.suixue.edu.college.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.GlideUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.entity.ChatSessionInfo;

import java.util.List;

/**
 * Created by cuiyan on 2018/10/9.
 */
public class ChatSessionListAdapter extends BaseRecyclerAdapter<ChatSessionInfo> {
    public ChatSessionListAdapter(Context context, List<ChatSessionInfo> dataList) {
        super(context, dataList, R.layout.item_chat_session);
    }

    @Override
    public void fillData(RecyclerViewHolder holder, int position) {
        ChatSessionInfo info = getItem(position);
        ImageView ivCommunicatorAvatar = holder.getView(R.id.iv_communicator_avatar);
        GlideUtil.loadImage(context, info.getCommunicatorAvatarUrl(), R.mipmap.ic_launcher, R.mipmap.ic_launcher, ivCommunicatorAvatar, 1);
        holder.setVisibility(R.id.iv_unread_msg_tip, info.isHasUnreadMsg());
        holder.setText(R.id.tv_communicator_name, info.getCommunicatorName());
        holder.setText(R.id.tv_latest_msg_time, info.getLatestMsgTime());
        holder.setText(R.id.tv_latest_msg, info.getLatestMsg());
    }
}

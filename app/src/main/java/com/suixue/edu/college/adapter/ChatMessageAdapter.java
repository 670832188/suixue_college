package com.suixue.edu.college.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.GlideUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.entity.ChatMessageInfo;

import java.util.List;

/**
 * Created by cuiyan on 2018/10/9.
 */
public class ChatMessageAdapter extends BaseRecyclerAdapter<ChatMessageInfo> {
    private static final int TYPE_RECEIVE = 1;
    private static final int TYPE_SEND = 2;

    public ChatMessageAdapter(Context context, List<ChatMessageInfo> dataList) {
        super(context, dataList);
    }

    @NonNull
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RecyclerViewHolder.getViewHolder(context, parent, viewType == TYPE_RECEIVE ? R.layout.item_chat_receive : R.layout.item_chat_send);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getType();
    }

    @Override
    public void fillData(RecyclerViewHolder holder, int position) {
        ChatMessageInfo info = getItem(position);
        ImageView ivAvatar = holder.getView(R.id.iv_creator_avatar);
        GlideUtil.loadImage(context, info.getAvatarUrl(), R.mipmap.ic_launcher, R.mipmap.ic_launcher, ivAvatar, 1);
        holder.setText(R.id.tv_content, info.getContent());
    }
}

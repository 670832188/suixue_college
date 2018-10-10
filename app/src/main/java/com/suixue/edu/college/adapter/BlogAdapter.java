package com.suixue.edu.college.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.GlideUtil;
import com.dev.kit.basemodule.util.StringUtil;
import com.dev.kit.basemodule.util.ToastUtil;
import com.dev.kit.basemodule.view.AutoLinkStyleTextView;
import com.suixue.edu.college.R;
import com.suixue.edu.college.activity.MainActivity;
import com.suixue.edu.college.entity.BlogInfo;
import com.suixue.edu.college.entity.RecommendedBloggerResult;

import java.util.List;

/**
 * Created by cuiyan on 2018/9/10.
 */
public class BlogAdapter extends BaseRecyclerAdapter<Object> {
    private static final int VIEW_TYPE_BLOG = 1;
    private static final int VIEW_TYPE_RECOMMENDED_BLOGGER = 2;
    private MainActivity.OnBlogTagClickListener onBlogTagClickListener;

    public BlogAdapter(Context context, List<Object> dataList) {
        super(context, dataList);
    }

    public BlogAdapter(Context context, List<Object> dataList, int itemViewLayoutId) {
        super(context, dataList, itemViewLayoutId);
    }

    public void setOnBlogTagClickListener(MainActivity.OnBlogTagClickListener listener) {
        onBlogTagClickListener = listener;
    }

    @NonNull
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RecyclerViewHolder.getViewHolder(context, parent, viewType == VIEW_TYPE_BLOG ? R.layout.item_bolog_info : R.layout.item_recommended_blogger);
    }

    @Override
    public int getItemViewType(int position) {
        Object info = getItem(position);
        return info instanceof BlogInfo ? VIEW_TYPE_BLOG : VIEW_TYPE_RECOMMENDED_BLOGGER;
    }

    @Override
    public void fillData(RecyclerViewHolder holder, int position) {
        Object info = getItem(position);
        if (info instanceof BlogInfo) {
            fillBlogData(holder, (BlogInfo) info);
        } else if (info instanceof RecommendedBloggerResult) {
            fillRecommendedBloggerData(holder, (RecommendedBloggerResult) info);
        }
    }

    private void fillBlogData(RecyclerViewHolder holder, final BlogInfo info) {
        ImageView ivBloggerAvatar = holder.getView(R.id.iv_blogger_avatar);
        GlideUtil.loadImage(context, info.getBloggerAvatarUrl(), R.mipmap.ic_launcher, R.mipmap.ic_launcher, ivBloggerAvatar, 1);
        holder.setText(R.id.tv_blogger_name, info.getBloggerName());
        holder.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(info, true);
            }
        });

        holder.setOnClickListener(R.id.iv_transfer, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(context, "您转发了这个帖子");
            }
        });
        holder.setOnClickListener(R.id.iv_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(context, "您分享了这个帖子");
            }
        });
        holder.setOnClickListener(R.id.iv_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(context, "您喜欢了这个帖子");
            }
        });

        RecyclerView rvContent = holder.getView(R.id.rv_blog_content);
        rvContent.setLayoutManager(new LinearLayoutManager(context));
        rvContent.setAdapter(new BlogContentAdapter(context, info.getBlogContentList()));
        AutoLinkStyleTextView tvSourceAndTags = holder.getView(R.id.tv_source_and_tags);
        String blogSource = info.getSource();
        if (StringUtil.isEmpty(blogSource)) {
            blogSource = "";
        } else {
            blogSource = context.getString(R.string.blog_source) + blogSource;
        }
        StringBuilder sb = new StringBuilder(blogSource);
        final String[] tags = info.getTags();
        if (tags != null && tags.length > 0) {
            for (int i = 0; i < tags.length; i++) {
                if (!tags[i].startsWith("#")) {
                    tags[i] = "#" + tags[i];
                }
                sb.append("  ").append(tags[i]);
            }
        }
        if (!StringUtil.isEmpty(sb.toString())) {
            tvSourceAndTags.setVisibility(View.VISIBLE);
            tvSourceAndTags.setText(sb.toString());
            if (tags != null && tags.length > 0) {
                tvSourceAndTags.setClickSpanTextValues(new AutoLinkStyleTextView.ClickCallBack() {
                    @Override
                    public void onClick(int position) {
                        if (onBlogTagClickListener != null) {
                            onBlogTagClickListener.onTagClick(tags[position].substring(1));
                        }
                    }
                }, tags);
            }
        } else {
            tvSourceAndTags.setVisibility(View.GONE);
        }
        holder.setText(R.id.tv_attention_level, "热度" + info.getAttentionLevel());
    }

    private void fillRecommendedBloggerData(RecyclerViewHolder holder, RecommendedBloggerResult recommendedBloggerResult) {
        RecyclerView rvRecommendBlogger = holder.getView(R.id.rv_recommended_blogger);
        rvRecommendBlogger.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvRecommendBlogger.setAdapter(new RecommendedBloggerAdapter(context, recommendedBloggerResult.getRecommendedBloggerInfoList()));
    }
}

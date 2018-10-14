package com.suixue.edu.college.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.dev.kit.basemodule.util.GlideUtil;
import com.dev.kit.basemodule.util.StringUtil;
import com.dev.kit.basemodule.util.ToastUtil;
import com.dev.kit.basemodule.view.AutoLinkStyleTextView;
import com.plattysoft.leonids.ParticleSystem;
import com.plattysoft.leonids.modifiers.ScaleModifier;
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
        final ImageView ivPraiseTrigger = holder.getView(R.id.iv_praise);
        ivPraiseTrigger.setImageResource(info.isPraised() ? R.mipmap.ic_praised : R.mipmap.ic_unpraised);
        ivPraiseTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                praiseBlog(info, ivPraiseTrigger);
            }
        });
        RecyclerView rvContent = holder.getView(R.id.rv_blog_content);
        rvContent.setLayoutManager(new LinearLayoutManager(context));
        rvContent.setAdapter(new BlogContentAdapter(context, info.getBlogContentList()));
        AutoLinkStyleTextView tvSourceAndTags = holder.getView(R.id.tv_source_and_tags);
        String blogSourceType = info.getSourceType();
        String blogSource;
        if (StringUtil.isEmpty(blogSourceType) || blogSourceType.trim().equals(BlogInfo.SOURCE_TYPE_RECOMMENDED)) {
            tvSourceAndTags.setCompoundDrawables(null, null, null, null);
            blogSource = context.getString(R.string.blog_source_recommended);
        } else {
            Drawable drawable = context.getDrawable(R.mipmap.ic_search_source);
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                tvSourceAndTags.setCompoundDrawables(drawable, null, null, null);
                tvSourceAndTags.setCompoundDrawablePadding(DisplayUtil.dp2px(5));
            }

            blogSource = context.getString(R.string.blog_source_search);
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
        tvSourceAndTags.setText(StringUtil.getSpannableString(sb.toString(), 0, blogSource.length(), Color.parseColor("#54B87C")));
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
        holder.setText(R.id.tv_attention_level, "热度" + info.getAttentionLevel());
    }

    private void fillRecommendedBloggerData(RecyclerViewHolder holder, RecommendedBloggerResult recommendedBloggerResult) {
        RecyclerView rvRecommendBlogger = holder.getView(R.id.rv_recommended_blogger);
        rvRecommendBlogger.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvRecommendBlogger.setAdapter(new RecommendedBloggerAdapter(context, recommendedBloggerResult.getRecommendedBloggerInfoList()));
    }

    private void praiseBlog(BlogInfo info, ImageView ivTrigger) {
        if (!info.isPraised()) {
            ivTrigger.setImageResource(R.mipmap.ic_praised);
            new ParticleSystem((Activity) context, 10, R.mipmap.ic_praised, 3000)
                    .setSpeedByComponentsRange(-0.1f, 0.1f, -0.1f, 0.02f)
                    .setAcceleration(0.000003f, 90)
                    .setInitialRotationRange(0, 360)
                    .setRotationSpeed(120)
                    .setFadeOut(2000)
                    .addModifier(new ScaleModifier(0f, 1.5f, 0, 1500))
                    .oneShot(ivTrigger, 10);
        } else {
            ivTrigger.setImageResource(R.mipmap.ic_unpraised);
        }
        info.setPraised(!info.isPraised());
        // ToDo 点赞或取消点赞
    }
}

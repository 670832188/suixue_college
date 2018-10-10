package com.suixue.edu.college.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.dev.kit.basemodule.util.GlideUtil;
import com.dev.kit.basemodule.util.ToastUtil;
import com.joooonho.SelectableRoundedImageView;
import com.suixue.edu.college.R;
import com.suixue.edu.college.entity.RecommendedBloggerInfo;

import java.util.List;

/**
 * Created by cuiyan on 2018/9/11.
 */
public class RecommendedBloggerAdapter extends BaseRecyclerAdapter<RecommendedBloggerInfo> {
    private static final int[] bgColor = {R.color.color_common_red, R.color.color_common_blue, R.color.color_common_orange, R.color.color_common_purple, R.color.color_common_green};

    public RecommendedBloggerAdapter(Context context, List<RecommendedBloggerInfo> dataList) {
        super(context, dataList, R.layout.item_recommended_blogger_info);
    }

    @Override
    public void fillData(RecyclerViewHolder holder, int position) {
        final RecommendedBloggerInfo info = getItem(position);
        ImageView ivBloggerAvatar = holder.getView(R.id.iv_blogger_avatar);
        Glide.with(context).load(info.getBloggerAvatarUrl()).into(ivBloggerAvatar);
        ImageView ivBlogCover = holder.getView(R.id.iv_blogger_cover);
        Glide.with(context).load(info.getBloggerCoverUrl()).into(ivBlogCover);

        holder.setText(R.id.tv_blogger_name, info.getBloggerName());
        holder.setText(R.id.tv_blogger_desc, info.getBloggerDesc());
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(context.getResources().getColor(bgColor[position % 5]));
        drawable.setCornerRadius(DisplayUtil.dp2px(5));
        holder.getView(R.id.tv_concern).setBackground(drawable);
        holder.setOnClickListener(R.id.tv_concern, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToConcernBlogger(info.getBloggerId());
            }
        });
        SelectableRoundedImageView ivPic1 = holder.getView(R.id.iv_pic1);
        SelectableRoundedImageView ivPic2 = holder.getView(R.id.iv_pic2);
        SelectableRoundedImageView ivPic3 = holder.getView(R.id.iv_pic3);

        List<String> latestPictures = info.getLatestPictures();
        if (latestPictures != null && latestPictures.size() > 0) {
            int size = latestPictures.size();
            if (size >= 3) {
                GlideUtil.loadImage(context, latestPictures.get(0), R.drawable.bg_default_pic_thumb, R.drawable.bg_default_pic_thumb, ivPic1, 1);
                GlideUtil.loadImage(context, latestPictures.get(1), R.drawable.bg_default_pic_thumb, R.drawable.bg_default_pic_thumb, ivPic2, 1);
                GlideUtil.loadImage(context, latestPictures.get(2), R.drawable.bg_default_pic_thumb, R.drawable.bg_default_pic_thumb, ivPic3, 1);
            } else if (size == 2) {
                GlideUtil.loadImage(context, latestPictures.get(0), R.drawable.bg_default_pic_thumb, R.drawable.bg_default_pic_thumb, ivPic1, 1);
                GlideUtil.loadImage(context, latestPictures.get(1), R.drawable.bg_default_pic_thumb, R.drawable.bg_default_pic_thumb, ivPic2, 1);
                ivPic3.setImageResource(R.drawable.bg_default_pic_thumb);
            } else {
                GlideUtil.loadImage(context, latestPictures.get(0), R.drawable.bg_default_pic_thumb, R.drawable.bg_default_pic_thumb, ivPic1, 1);
                ivPic2.setImageResource(R.drawable.bg_default_pic_thumb);
                ivPic3.setImageResource(R.drawable.bg_default_pic_thumb);
            }
        } else {
            ivPic1.setImageResource(R.drawable.bg_default_pic_thumb);
            ivPic2.setImageResource(R.drawable.bg_default_pic_thumb);
            ivPic3.setImageResource(R.drawable.bg_default_pic_thumb);
        }
    }

    private void tryToConcernBlogger(String bloggerId) {
        ToastUtil.showToast(context, "尝试关注该博主");
    }
}

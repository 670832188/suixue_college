package com.suixue.edu.college.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dev.kit.basemodule.netRequest.Configs.Config;
import com.dev.kit.basemodule.netRequest.model.BaseController;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestCallback;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestSubscriber;
import com.dev.kit.basemodule.netRequest.util.BaseServiceUtil;
import com.dev.kit.basemodule.result.BaseResult;
import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.dev.kit.basemodule.util.GlideUtil;
import com.joooonho.SelectableRoundedImageView;
import com.suixue.edu.college.R;
import com.suixue.edu.college.config.ApiService;
import com.suixue.edu.college.entity.RecommendedBloggerInfo;
import com.suixue.edu.college.util.ViewClickUtil;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by cuiyan on 2018/9/11.
 */
public class RecommendedBloggerAdapter extends BaseRecyclerAdapter<RecommendedBloggerInfo> {
    private Handler handler = new Handler(Looper.getMainLooper());
    private static final int[] bgColor = {R.color.color_common_red, R.color.color_common_blue, R.color.color_common_orange, R.color.color_common_purple, R.color.color_common_green};

    public RecommendedBloggerAdapter(Context context, List<RecommendedBloggerInfo> dataList) {
        super(context, dataList, R.layout.item_recommended_blogger_info);
    }

    @Override
    public void fillData(RecyclerViewHolder holder, final int position) {
        RecommendedBloggerInfo info = getItem(position);
        ImageView ivBloggerAvatar = holder.getView(R.id.iv_blogger_avatar);
        Glide.with(context).load(info.getBloggerAvatarUrl()).into(ivBloggerAvatar);
        ImageView ivBlogCover = holder.getView(R.id.iv_blogger_cover);
        Glide.with(context).load(info.getBloggerCoverUrl()).into(ivBlogCover);

        holder.setText(R.id.tv_blogger_name, info.getBloggerName());
        holder.setText(R.id.tv_blogger_desc, info.getBloggerDesc());
        GradientDrawable drawable = new GradientDrawable();
        if (info.isConcerned()) {
            drawable.setColor(context.getResources().getColor(bgColor[position % 5]));
        } else {
            drawable.setColor(context.getResources().getColor(R.color.color_common_light_gray));
        }
        drawable.setCornerRadius(DisplayUtil.dp2px(5));
        holder.getView(R.id.tv_concern).setBackground(drawable);
        final View concernTrigger = holder.getView(R.id.tv_concern);
        ViewClickUtil.onViewClick(concernTrigger, 1500, new ViewClickUtil.OnClickCallBack() {
            @Override
            public void onClick(View view) {
                tryToConcernBlogger(position, concernTrigger);
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


    // 关注或取消关注
    private void tryToConcernBlogger(int position, final View triggerView) {
        final RecommendedBloggerInfo info = getItem(position);
        String concernFlag = info.isConcerned() ? "0" : "1";
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(DisplayUtil.dp2px(5));
        if (!info.isConcerned()) {
            drawable.setColor(context.getResources().getColor(bgColor[position % 5]));
        } else {
            drawable.setColor(context.getResources().getColor(R.color.color_common_light_gray));
        }
        triggerView.setBackground(drawable);
        final ValueAnimator animator = ValueAnimator.ofFloat(1, 1.2f, 1).setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                triggerView.setScaleX(value);
                triggerView.setScaleY(value);
            }
        });
        animator.start();
        NetRequestSubscriber<BaseResult<String>> subscriber
                = new NetRequestSubscriber<>(new NetRequestCallback<BaseResult<String>>() {
            @Override
            public void onSuccess(@NonNull BaseResult<String> result) {
                if (Config.REQUEST_SUCCESS_CODE.endsWith(result.getCode())) {
                    info.setConcerned("1".equals(result.getData()));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }

            @Override
            public void onFinish() {
                long delayTime = (long) (animator.getDuration() * (1 - animator.getAnimatedFraction()));
                if (animator.isRunning()) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    }, delayTime);
                } else {
                    notifyDataSetChanged();
                }
            }
        }, context, true, "");
        Observable<BaseResult<String>> observable = BaseServiceUtil.createService(ApiService.class).concernBlogger(info.getBloggerId(), concernFlag);
        BaseController.sendRequest(subscriber, observable);
    }
}

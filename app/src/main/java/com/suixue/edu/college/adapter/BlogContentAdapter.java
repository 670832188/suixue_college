package com.suixue.edu.college.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.GlideUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.suixue.edu.college.R;
import com.suixue.edu.college.entity.BlogContentInfo;
import com.suixue.edu.college.video.SampleCoverVideo;

import java.security.MessageDigest;
import java.util.List;

/**
 * Created by cuiyan on 2018/9/10.
 */
public class BlogContentAdapter extends BaseRecyclerAdapter<BlogContentInfo> {
    public BlogContentAdapter(Context context, List<BlogContentInfo> dataList) {
        super(context, dataList, R.layout.item_blog_content);
    }

    @Override
    public void fillData(RecyclerViewHolder holder, final int position) {
        final BlogContentInfo info = getItem(position);
        holder.setVisibility(R.id.tv_text_item, View.GONE);
        holder.setVisibility(R.id.iv_img_item, View.GONE);
        holder.setVisibility(R.id.video_player, View.GONE);

        switch (info.getContentType()) {
            case BlogContentInfo.CONTENT_TYPE_TEXT: {
                holder.setVisibility(R.id.tv_text_item, View.VISIBLE);
                TextView tvTextItem = holder.getView(R.id.tv_text_item);
                tvTextItem.setText(Html.fromHtml(info.getContent()));
                break;
            }
            case BlogContentInfo.CONTENT_TYPE_PICTURE: {
                holder.setVisibility(R.id.iv_img_item, View.VISIBLE);
                final ImageView ivImgItem = holder.getView(R.id.iv_img_item);
                if (info.getWidth() > 0 && info.getHeight() > 0) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivImgItem.getLayoutParams();
                    if (layoutParams != null) {
                        ivImgItem.getLayoutParams().width = info.getWidth();
                        ivImgItem.getLayoutParams().height = info.getHeight();
                    } else {
                        layoutParams = new FrameLayout.LayoutParams(info.getWidth(), info.getHeight());
                        ivImgItem.setLayoutParams(layoutParams);
                    }
                }
                GlideUtil.loadImage(context, info.getContent(), R.mipmap.ic_launcher, R.mipmap.ic_launcher, ivImgItem, 1, new Transformation<Bitmap>() {
                    @NonNull
                    @Override
                    public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
                        if (info.getWidth() == 0 && info.getHeight() == 0) {
                            int imgWidth = resource.get().getWidth();
                            int imgHeight = resource.get().getHeight();
                            int ivWidth = ivImgItem.getWidth();

                            if (ivWidth == 0) {
                                ivImgItem.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.UNSPECIFIED);
                                ivWidth = ivImgItem.getMeasuredWidth();
                            }

                            if (imgWidth > ivWidth) {
                                imgWidth = ivWidth;
                                imgHeight = (int) ((float) imgHeight * ivWidth / imgWidth);
                            }
                            info.setWidth(imgWidth);
                            info.setHeight(imgHeight);
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivImgItem.getLayoutParams();
                            if (layoutParams != null) {
                                ivImgItem.getLayoutParams().width = info.getWidth();
                                ivImgItem.getLayoutParams().height = info.getHeight();
                            } else {
                                layoutParams = new FrameLayout.LayoutParams(info.getWidth(), info.getHeight());
                                ivImgItem.setLayoutParams(layoutParams);
                            }
                        }
                        return resource;
                    }

                    @Override
                    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

                    }
                });
                break;
            }
            case BlogContentInfo.CONTENT_TYPE_VIDEO: {
                setVideoPlay(holder, position);
                break;
            }
        }
    }

    private void setVideoPlay(RecyclerViewHolder holder, int position) {
        holder.setVisibility(R.id.video_player, View.VISIBLE);
        final BlogContentInfo info = getItem(position);
        final SampleCoverVideo gsyVideoPlayer = holder.getView(R.id.video_player);
        if (info.getWidth() > 0 && info.getHeight() > 0) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) gsyVideoPlayer.getLayoutParams();
            if (layoutParams != null) {
                gsyVideoPlayer.getLayoutParams().width = info.getWidth();
                gsyVideoPlayer.getLayoutParams().height = info.getHeight();
            } else {
                layoutParams = new FrameLayout.LayoutParams(info.getWidth(), info.getHeight());
                gsyVideoPlayer.setLayoutParams(layoutParams);
            }
        }
        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
//        gsyVideoPlayer.loadCoverImage(info.getContent(), R.mipmap.ic_launcher);
        final ImageView coverImage = gsyVideoPlayer.getCoverImage();
        GlideUtil.loadImage(context, info.getContent(), R.mipmap.ic_launcher, R.mipmap.ic_launcher, coverImage, 1, new Transformation<Bitmap>() {
            @NonNull
            @Override
            public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
                if (info.getWidth() == 0 && info.getHeight() == 0) {
                    int imgWidth = resource.get().getWidth();
                    int imgHeight = resource.get().getHeight();
                    int ivWidth = gsyVideoPlayer.getWidth();

                    if (ivWidth == 0) {
                        gsyVideoPlayer.measure(View.MeasureSpec.EXACTLY, View.MeasureSpec.UNSPECIFIED);
                        ivWidth = gsyVideoPlayer.getMeasuredWidth();
                    }

                    if (imgWidth > ivWidth) {
                        imgWidth = ivWidth;
                        imgHeight = (int) ((float) imgHeight * ivWidth / imgWidth);
                    }
                    info.setWidth(imgWidth);
                    info.setHeight(imgHeight);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) gsyVideoPlayer.getLayoutParams();
                    if (layoutParams != null) {
                        gsyVideoPlayer.getLayoutParams().width = info.getWidth();
                        gsyVideoPlayer.getLayoutParams().height = info.getHeight();
                    } else {
                        layoutParams = new FrameLayout.LayoutParams(info.getWidth(), info.getHeight());
                        gsyVideoPlayer.setLayoutParams(layoutParams);
                    }
                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) coverImage.getLayoutParams();
                    if (layoutParams1 != null) {
                        coverImage.getLayoutParams().width = info.getWidth();
                        coverImage.getLayoutParams().height = info.getHeight();
                    } else {
                        layoutParams1 = new RelativeLayout.LayoutParams(info.getWidth(), info.getHeight());
                        coverImage.setLayoutParams(layoutParams1);
                    }
                }
                return resource;
            }

            @Override
            public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

            }
        });
        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
//                .setThumbImageView(ivCover)
                .setUrl(info.getContent())
                .setSetUpLazy(true)//lazy可以防止滑动卡顿
                .setVideoTitle("")
                .setCacheWithPlay(true)
                .setRotateViewAuto(true)
                .setLockLand(false)
                .setPlayTag(info.getContent() + position)
                .setShowFullAnimation(true)
                .setNeedLockFull(true)
                .setPlayPosition(position)
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                            //静音
                            GSYVideoManager.instance().setNeedMute(true);
                        }

                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        //全屏不静音
                        GSYVideoManager.instance().setNeedMute(true);
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        GSYVideoManager.instance().setNeedMute(false);
                        gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
                    }
                }).build(gsyVideoPlayer);


        //增加title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);

        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);

        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(gsyVideoPlayer);
            }
        });
    }

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        standardGSYVideoPlayer.startWindowFullscreen(context, true, true);
    }
}

package com.suixue.edu.college.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.dev.kit.basemodule.util.GlideUtil;
import com.dev.kit.basemodule.util.LogUtil;
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
public class PublishBlogAdapter extends BaseRecyclerAdapter<BlogContentInfo> {
    private static final int VIEW_TYPE_TEXT = 1;
    private static final int VIEW_TYPE_PICTURE = 2;
    private static final int VIEW_TYPE_VIDEO = 3;

    public PublishBlogAdapter(Context context, List<BlogContentInfo> dataList) {
        super(context, dataList);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_TEXT: {
                return RecyclerViewHolder.getViewHolder(context, parent, R.layout.item_text);
            }
            case VIEW_TYPE_PICTURE: {
                return RecyclerViewHolder.getViewHolder(context, parent, R.layout.item_picture);
            }
            default: {
                return RecyclerViewHolder.getViewHolder(context, parent, R.layout.item_video);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        BlogContentInfo info = getItem(position);
        switch (info.getContentType()) {
            case BlogContentInfo.CONTENT_TYPE_TEXT: {
                type = VIEW_TYPE_TEXT;
                break;
            }
            case BlogContentInfo.CONTENT_TYPE_PICTURE: {
                type = VIEW_TYPE_PICTURE;
                break;
            }
            case BlogContentInfo.CONTENT_TYPE_VIDEO: {
                type = VIEW_TYPE_VIDEO;
                break;
            }
            default: {
                throw new RuntimeException("content type error");
            }
        }
        return type;
    }

    @Override
    public void fillData(RecyclerViewHolder holder, final int position) {
        final BlogContentInfo info = getItem(position);
        switch (info.getContentType()) {
            case BlogContentInfo.CONTENT_TYPE_TEXT: {
                TextView tvTextItem = holder.getView(R.id.tv_text_item);
                tvTextItem.setText(Html.fromHtml(info.getContent()));
                break;
            }
            case BlogContentInfo.CONTENT_TYPE_PICTURE: {
                setPicture(holder, position);
                break;
            }
            case BlogContentInfo.CONTENT_TYPE_VIDEO: {
                setVideoPlay(holder, position);
                break;
            }
        }
        holder.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(info, true);
            }
        });
    }

    private void setPicture(RecyclerViewHolder holder, int position) {
        final BlogContentInfo info = getItem(position);
        final ImageView ivImgItem = holder.getView(R.id.iv_picture_item);
        if (info.getWidth() > 0 && info.getHeight() > 0) {
            int ivWidth = getBlogWidth();
            int ivHeight = (int) ((float) ivWidth / info.getWidth() * info.getHeight());
            ViewGroup.LayoutParams layoutParams = ivImgItem.getLayoutParams();
            if (layoutParams != null) {
                ivImgItem.getLayoutParams().width = ivWidth;
                ivImgItem.getLayoutParams().height = ivHeight;
            } else {
                layoutParams = new ViewGroup.LayoutParams(ivWidth, ivHeight);
                ivImgItem.setLayoutParams(layoutParams);
            }
        }
        GlideUtil.loadImageWithDisableDiskCache(context, info.getContent(), R.mipmap.ic_launcher, R.mipmap.ic_launcher, ivImgItem, 1, new Transformation<Bitmap>() {
            @NonNull
            @Override
            public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
                if (info.getWidth() == 0 && info.getHeight() == 0) {
                    info.setWidth(resource.get().getWidth());
                    info.setHeight(resource.get().getHeight());
                    int ivWidth = getBlogWidth();
                    int ivHeight = (int) ((float) ivWidth / resource.get().getWidth() * resource.get().getHeight());
                    ViewGroup.LayoutParams layoutParams = ivImgItem.getLayoutParams();
                    if (layoutParams != null) {
                        ivImgItem.getLayoutParams().width = ivWidth;
                        ivImgItem.getLayoutParams().height = ivHeight;
                    } else {
                        layoutParams = new ViewGroup.LayoutParams(ivWidth, ivHeight);
                        ivImgItem.setLayoutParams(layoutParams);
                    }
                }
                return resource;
            }

            @Override
            public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
            }
        });
    }

    private void setVideoPlay(RecyclerViewHolder holder, int position) {
        final BlogContentInfo info = getItem(position);
        final SampleCoverVideo gsyVideoPlayer = holder.getView(R.id.video_item);
        if (info.getWidth() > 0 && info.getHeight() > 0) {
            int videoWidth = getBlogWidth(); // 横向满屏
            int videoHeight = (int) ((float) videoWidth / info.getWidth() * info.getHeight());
            ViewGroup.LayoutParams layoutParams = gsyVideoPlayer.getLayoutParams();
            if (layoutParams != null) {
                gsyVideoPlayer.getLayoutParams().width = videoWidth;
                gsyVideoPlayer.getLayoutParams().height = videoHeight;
            } else {
                layoutParams = new ViewGroup.LayoutParams(videoWidth, videoHeight);
                gsyVideoPlayer.setLayoutParams(layoutParams);
            }
        }
        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        final ImageView coverImage = gsyVideoPlayer.getCoverImage();
        GlideUtil.loadImageWithDisableDiskCache(context, info.getContent(), R.mipmap.ic_launcher, R.mipmap.ic_launcher, coverImage, 1, new Transformation<Bitmap>() {
            @NonNull
            @Override
            public Resource<Bitmap> transform(@NonNull Context context, @NonNull Resource<Bitmap> resource, int outWidth, int outHeight) {
                if (info.getWidth() == 0 && info.getHeight() == 0) {
                    info.setWidth(resource.get().getWidth());
                    info.setHeight(resource.get().getHeight());
                    int videoWidth = getBlogWidth();
                    int videoHeight = (int) ((float) videoWidth / info.getWidth() * info.getHeight());
                    ViewGroup.LayoutParams layoutParams = gsyVideoPlayer.getLayoutParams();
                    if (layoutParams != null) {
                        gsyVideoPlayer.getLayoutParams().width = videoWidth;
                        gsyVideoPlayer.getLayoutParams().height = videoHeight;
                    } else {
                        layoutParams = new ViewGroup.LayoutParams(videoWidth, videoHeight);
                        gsyVideoPlayer.setLayoutParams(layoutParams);
                    }
                    RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) coverImage.getLayoutParams();
                    if (layoutParams1 != null) {
                        coverImage.getLayoutParams().width = videoWidth;
                        coverImage.getLayoutParams().height = videoHeight;
                    } else {
                        layoutParams1 = new RelativeLayout.LayoutParams(videoWidth, videoHeight);
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

    public int getVideoSize() {
        int videoSize = 0;
        for (BlogContentInfo info : dataList) {
            if (BlogContentInfo.CONTENT_TYPE_VIDEO.equals(info.getContentType())) {
                videoSize++;
            }
        }
        return videoSize;
    }

    private int getBlogWidth() {
        return DisplayUtil.getScreenWidth();
    }
}

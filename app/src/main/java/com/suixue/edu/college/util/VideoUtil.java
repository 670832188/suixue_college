package com.suixue.edu.college.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.suixue.edu.college.R;

/**
 * Created by cuiyan on 2018/9/10.
 */
public class VideoUtil {

    // 根据视频url加载封面
    private void loadCover(Context context, ImageView imageView, String videoUrl) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.ic_launcher);

        Glide.with(context)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(3000000)
                                .centerCrop()
                                .error(R.mipmap.ic_launcher)
                                .placeholder(R.mipmap.ic_launcher))
                .load(videoUrl)
                .into(imageView);
    }
}

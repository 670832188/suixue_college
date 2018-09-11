package com.dev.kit.basemodule.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;

import java.security.MessageDigest;

/**
 * Created by cuiyan on 2018/9/11.
 */
public class GlideUtil {

    public static synchronized void loadImage(Context context, String imgUri, @DrawableRes int defaultSrcId, @DrawableRes int errorSrcId, ImageView target, float sizeMultiplier) {
        if (TextUtils.isEmpty(imgUri)) {
            return;
        }
        Glide.with(context)
                .load(imgUri)
                .apply(new RequestOptions()
                        .placeholder(defaultSrcId)
                        .error(errorSrcId))
                .thumbnail((sizeMultiplier > 0 && sizeMultiplier < 1) ? sizeMultiplier : 1.0f)
                .into(target);
    }

    public static synchronized void loadImage(Context context, String imgUri, @DrawableRes int defaultSrcId, @DrawableRes int errorSrcId, ImageView target, float sizeMultiplier, Transformation<Bitmap> transformation) {
        if (TextUtils.isEmpty(imgUri)) {
            return;
        }
        RequestOptions options = new RequestOptions().placeholder(defaultSrcId).error(defaultSrcId);
        if (transformation != null) {
            options.transform(transformation);
        }
        Glide.with(context)
                .load(imgUri)
                .apply(options)
                .thumbnail((sizeMultiplier > 0 && sizeMultiplier < 1) ? sizeMultiplier : 1.0f)
                .into(target);
    }

    public static synchronized void loadImage(Context context, String imgUri, SimpleTarget<Drawable> target, @DrawableRes int defaultSrcId, @DrawableRes int errorSrcId, float sizeMultiplier) {
        if (TextUtils.isEmpty(imgUri)) {
            return;
        }
        Glide.with(context).load(imgUri)
                .apply(new RequestOptions()
                        .placeholder(defaultSrcId)
                        .error(errorSrcId))
                .thumbnail((sizeMultiplier > 0 && sizeMultiplier < 1) ? 1.0f : sizeMultiplier)
                .into(target);
    }

    public static synchronized void loadImage(Context context, String imgUri, @DrawableRes int defaultSrcId, @DrawableRes int errorSrcId, SimpleTarget<Bitmap> target, float sizeMultiplier) {
        if (TextUtils.isEmpty(imgUri)) {
            return;
        }
        Glide.with(context).asBitmap().load(imgUri)
                .apply(new RequestOptions()
                        .placeholder(defaultSrcId)
                        .error(errorSrcId))
                .thumbnail((sizeMultiplier > 0 && sizeMultiplier < 1) ? 1.0f : sizeMultiplier)
                .into(target);
    }

    public static synchronized void loadCircleImage(Context context, String imgUri, @DrawableRes int defaultSrcId, ImageView target, float sizeMultiplier) {
        if (TextUtils.isEmpty(imgUri)) {
            return;
        }
        Glide.with(context)
                .load(imgUri)
                .apply(new RequestOptions()
                        .optionalCircleCrop()
                        .placeholder(defaultSrcId)
                        .error(defaultSrcId))
                .thumbnail((sizeMultiplier > 0 && sizeMultiplier < 1) ? sizeMultiplier : 1.0f)
                .into(target);
    }
}

package com.suixue.edu.college.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.activity.VideoEditActivity;

import java.util.List;

/**
 * Author: cuiyan
 * Date:   18/10/7 18:56
 * Desc:
 */
public class VideoFrameAdapter extends BaseRecyclerAdapter<Bitmap> {
    private int itemWidth;

    public VideoFrameAdapter(Context context, int maxEnabledTime, List<Bitmap> dataList) {
        super(context, dataList, R.layout.item_video_frame);
        itemWidth = (int) ((DisplayUtil.getScreenWidth() - DisplayUtil.dp2px(70)) / (float) VideoEditActivity.FRAME_SHOW_COUNT);
    }

    @Override
    public void fillData(RecyclerViewHolder holder, int position) {
        ImageView ivFrame = holder.getView(R.id.iv_frame);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivFrame.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new FrameLayout.LayoutParams(itemWidth, FrameLayout.LayoutParams.MATCH_PARENT);
            ivFrame.setLayoutParams(layoutParams);
        } else {
            layoutParams.width = itemWidth;
        }
        if (position == 0) {
            layoutParams.setMargins(DisplayUtil.dp2px(35), 0, 0, 0);
        } else {
            layoutParams.setMargins(0, 0, 0, 0);
        }
        ivFrame.setImageBitmap(getItem(position));
    }
}

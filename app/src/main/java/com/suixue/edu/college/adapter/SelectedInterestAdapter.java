package com.suixue.edu.college.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.entity.InterestInfo;

import java.util.List;

/**
 * Created by cuiyan on 2018/10/12.
 */
public class SelectedInterestAdapter extends BaseRecyclerAdapter<InterestInfo> {
    private static final int[] itemBgColors = {Color.parseColor("#f04e2e"), Color.parseColor("#af62e3"), Color.parseColor("#E64fc6be"), Color.parseColor("#6d9eeb"), Color.parseColor("#ff9900")};

    public SelectedInterestAdapter(Context context, List<InterestInfo> dataList) {
        super(context, dataList, R.layout.item_selected_interest);
    }

    @Override
    public void fillData(RecyclerViewHolder holder, int position) {
        InterestInfo info = getItem(position);
        holder.setText(R.id.tv_name, info.getCategoryName());
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(itemBgColors[position % itemBgColors.length]);
        drawable.setCornerRadius(DisplayUtil.dp2px(3));
        holder.getItemView().setBackground(drawable);
    }
}

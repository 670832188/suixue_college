package com.suixue.edu.college.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.entity.InterestInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cuiyan on 2018/9/6.
 */
public class InterestAdapter extends BaseRecyclerAdapter<InterestInfo> {

    public InterestAdapter(Context context, List<InterestInfo> dataList) {
        super(context, dataList, R.layout.item_interest);
    }

    @Override
    public void fillData(RecyclerViewHolder holder, final int position) {
        final TextView itemView = holder.getView(R.id.tv_category_name);
        final InterestInfo info = getItem(position);
        holder.setText(R.id.tv_category_name, info.getCategoryName());
        if (info.isLocalCategoryTitle()) {
            itemView.setTextColor(context.getResources().getColor(R.color.color_common_white));
            itemView.setBackgroundResource(R.color.color_transparent);
        } else {
            if (!info.isChecked()) {
                itemView.setTextColor(context.getResources().getColor(R.color.color_common_white));
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(info.getBgColor());
                drawable.setCornerRadius(DisplayUtil.dp2px(3));
                itemView.setBackground(drawable);
            } else {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(Color.parseColor("#293954"));
                drawable.setCornerRadius(DisplayUtil.dp2px(3));
                drawable.setStroke(DisplayUtil.dp2px(1), info.getBgColor());
                itemView.setBackground(drawable);
                itemView.setTextColor(info.getBgColor());
            }
        }
    }

    public int getSelectedItemCount() {
        int count = 0;
        for (InterestInfo info : getDataList()) {
            if (info.isChecked()) {
                count++;
            }
        }
        return count;
    }

    public List<InterestInfo> getSelectedItemList() {
        List<InterestInfo> selectedItemList = new ArrayList<>();
        for (InterestInfo info : getDataList()) {
            if (info.isChecked()) {
                selectedItemList.add(info.createRealInterest());
            }
        }
        return selectedItemList;
    }
}

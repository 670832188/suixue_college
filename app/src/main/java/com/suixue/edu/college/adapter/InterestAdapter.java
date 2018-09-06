package com.suixue.edu.college.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.dev.kit.basemodule.util.LogUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.entity.InterestInfo;

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
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!info.isLocalCategoryTitle()) {
                    info.setChecked(!info.isChecked());
                    notifyItemChanged(position);
                    if (!info.isChildCategory() && !info.isChildAdded()) {
                        if (info.getSubCategoryList() != null && info.getSubCategoryList().size() > 0) {
                            info.setChildAdded(true);
                            getDataList().addAll(position + 1, info.getSubCategoryList());
                            notifyItemRangeInserted(position + 1, info.getSubCategoryList().size());
                            notifyItemRangeChanged(position + 1, dataList.size());
                        }
                    }
                    notifyItemChanged(position);
                }
            }
        });
        if (info.isLocalCategoryTitle()) {
            itemView.setBackgroundResource(R.color.transparent);
        } else {
            if (!info.isChecked()) {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(info.getBgColor());
                drawable.setCornerRadius(DisplayUtil.dp2px(3));
                itemView.setBackground(drawable);
            } else {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(Color.parseColor("#293954"));
                drawable.setCornerRadius(DisplayUtil.dp2px(3));
                drawable.setStroke(DisplayUtil.dp2px(1), Color.parseColor("#F57923"));
                itemView.setBackground(drawable);
            }
        }
    }
}

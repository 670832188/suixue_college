package com.suixue.edu.college.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.TextView;

import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.surpport.RecyclerViewHolder;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.entity.BaseCourseInfo;

import java.util.List;

/**
 * Created by cuiyan on 2018/10/31.
 */
public class CourseNameAdapter extends BaseRecyclerAdapter<BaseCourseInfo.CourseInfo> {

    public CourseNameAdapter(Context context, List<BaseCourseInfo.CourseInfo> dataList) {
        super(context, dataList, R.layout.item_course_name);
    }

    @Override
    public void fillData(RecyclerViewHolder holder, int position) {
        TextView tvCourseName = holder.getView(R.id.tv_course_name);
        tvCourseName.setText(getItem(position).getName());
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(DisplayUtil.dp2px(45));
        if (position == 0) {
            tvCourseName.setTextColor(context.getResources().getColor(R.color.color_common_white));
            drawable.setColor(context.getResources().getColor(R.color.color_main_bg));
        } else {
            tvCourseName.setTextColor(context.getResources().getColor(R.color.color_common_text));
            drawable.setColor(Color.parseColor("#f7f0f0"));
        }
        tvCourseName.setBackground(drawable);
    }
}

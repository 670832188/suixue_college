package com.suixue.edu.college.util;

import android.app.Activity;
import android.support.annotation.Size;

import com.dev.kit.basemodule.util.DisplayUtil;

import cn.qqtheme.framework.picker.DatePicker;

/**
 * Created by cuiyan on 2018/11/5.
 */
public class PickerUtil {
    public static void showDatePicker(@Size(3) int[] startDate, @Size(3) int[] endDate, @Size(3) int[] selectedDate, Activity activity, DatePicker.OnYearMonthDayPickListener listener) {
        final DatePicker picker = new DatePicker(activity);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(DisplayUtil.dp2px(10));
        picker.setRangeStart(startDate[0], startDate[1], startDate[2]);
        picker.setRangeEnd(endDate[0], endDate[1], endDate[2]);
        picker.setSelectedItem(selectedDate[0], selectedDate[1], selectedDate[2]);
        picker.setResetWhileWheel(false);
        picker.setLineSpaceMultiplier(3);
        picker.setOnDatePickListener(listener);
        picker.show();
    }
}

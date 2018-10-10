package com.suixue.edu.college.util;

import android.content.Context;

import com.dev.kit.basemodule.util.DisplayUtil;
import com.suixue.edu.college.R;

import me.dkzwm.widget.srl.SmoothRefreshLayout;
import me.dkzwm.widget.srl.extra.header.MaterialHeader;

/**
 * Created by cuiyan on 2018/10/10.
 */
public class RefreshUtil {

    public static synchronized void initMaterialRefreshLayout(SmoothRefreshLayout refreshLayout) {
        Context context = refreshLayout.getContext();
        MaterialHeader header = new MaterialHeader(context);
        header.setColorSchemeColors(new int[]{context.getResources().getColor(R.color.color_main_bg)});
        header.setPadding(0, DisplayUtil.dp2px(20), 0, DisplayUtil.dp2px(20));
        refreshLayout.setEnablePinContentView(true);
        refreshLayout.setHeaderView(header);
        refreshLayout.setDurationToClose(500);
    }
}

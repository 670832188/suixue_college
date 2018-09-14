package com.dev.kit.basemodule.view;

import android.content.Context;
import android.util.AttributeSet;

import me.dkzwm.widget.srl.SmoothRefreshLayout;

/**
 * Wave smooth refresh layout
 *
 * @author dkzwm
 */
public class WaveSmoothRefreshLayout extends SmoothRefreshLayout {
    private WaveHeader mWaveHeader;

    public WaveSmoothRefreshLayout(Context context) {
        this(context, null);
    }

    public WaveSmoothRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveSmoothRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mWaveHeader = new WaveHeader(context);
        setHeaderView(mWaveHeader);
        setEnableHeaderDrawerStyle(true);
        setEnableKeepRefreshView(true);
        setMaxMoveRatioOfHeader(.4f);
        setRatioOfHeaderToRefresh(.22f);
        setRatioToKeepHeader(.22f);
        setDurationToCloseHeader(380);
        setDurationOfBackToKeepHeader(850);
    }

    public WaveHeader getDefaultHeader() {
        return mWaveHeader;
    }

}

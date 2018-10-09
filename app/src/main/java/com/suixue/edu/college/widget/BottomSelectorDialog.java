package com.suixue.edu.college.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.dev.kit.basemodule.util.DisplayUtil;
import com.suixue.edu.college.R;

/**
 * Created by cuiyan on 2018/10/9.
 */
public abstract class BottomSelectorDialog extends BottomSheetDialog {

    public BottomSelectorDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            int dialogHeight = DisplayUtil.getScreenHeight();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_bottom_selector);
        FrameLayout rootContainer = findViewById(R.id.fl_root_container);
        rootContainer.addView(createRealContentView(rootContainer));
    }

    @NonNull
    public abstract View createRealContentView(FrameLayout rootContainer);
}

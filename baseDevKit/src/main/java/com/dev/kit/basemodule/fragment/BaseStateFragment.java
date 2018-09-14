package com.dev.kit.basemodule.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dev.kit.basemodule.R;
import com.dev.kit.basemodule.activity.BaseStateViewActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cuiyan on 2018/9/14.
 */
public abstract class BaseStateFragment extends BaseFragment {
    private int contentState = STATE_INVALID;
    private View lastContentView;
    private FrameLayout flRootContainer;
    // 网络请求状态视图
    private View progressView;
    // 异常视图
    private View errorView;
    // 空数据视图
    private View emptyView;
    // 数据视图
    private View dataContentView;

    // 以下为状态视图对应的状态值
    public static final int STATE_INVALID = -1;
    // 网络请求视图
    public static final int STATE_PROGRESS = 0;
    // 网络请求异常视图
    public static final int STATE_ERROR = 1;
    // 空数据视图
    public static final int STATE_EMPTY = 2;
    // 数据视图
    public static final int STATE_DATA_CONTENT = 3;

    private OnStateChangeListener onStateChangeListener;

    @IntDef({STATE_PROGRESS, STATE_ERROR, STATE_EMPTY, STATE_DATA_CONTENT, STATE_INVALID})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ContentState {
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        flRootContainer = (FrameLayout) inflater.inflate(R.layout.frg_base_state_view, container, false);
        initStateView(inflater);
        return flRootContainer;
    }

    @NonNull
    public abstract View createContentView(LayoutInflater inflater, FrameLayout flRootContainer);

    public View createProgressView(LayoutInflater inflater, FrameLayout flRootContainer) {
        return null;
    }

    public View createDataEmptyView(LayoutInflater inflater, FrameLayout flRootContainer) {
        return null;
    }

    public View createErrorView(LayoutInflater inflater, FrameLayout flRootContainer) {
        return null;
    }

    private void initStateView(LayoutInflater inflater) {
        dataContentView = createContentView(inflater, flRootContainer);
        progressView = createProgressView(inflater, flRootContainer);
        emptyView = createDataEmptyView(inflater, flRootContainer);
        errorView = createErrorView(inflater, flRootContainer);
        if (progressView == null) {
            progressView = inflater.inflate(R.layout.layout_net_loading, flRootContainer, false);
        }
        if (emptyView == null) {
            emptyView = inflater.inflate(R.layout.layout_data_empty, flRootContainer, false);
        }
        if (errorView == null) {
            errorView = inflater.inflate(R.layout.layout_net_error, flRootContainer, false);
        }
        if (dataContentView == null) {
            throw new RuntimeException("no dataContentView");
        }
        progressView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        flRootContainer.addView(dataContentView);
        flRootContainer.addView(progressView);
        flRootContainer.addView(emptyView);
        flRootContainer.addView(errorView);
        lastContentView = dataContentView;
    }

    public void setOnStateChangeListener(@NonNull OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public void setContentState(@ContentState int contentState) {
        if (contentState == this.contentState) {
            return;
        }
        this.contentState = contentState;
        View currentContentView = null;
        switch (contentState) {
            case STATE_INVALID: {
                break;
            }
            case STATE_PROGRESS: {
                currentContentView = progressView;
                break;
            }
            case STATE_EMPTY: {
                currentContentView = emptyView;
                break;
            }
            case STATE_ERROR: {
                currentContentView = errorView;
                break;
            }
            case STATE_DATA_CONTENT: {
                currentContentView = dataContentView;
                break;
            }
        }
        if (null != currentContentView) {
            if (lastContentView != null) {
                lastContentView.setVisibility(View.GONE);
            }
            lastContentView = currentContentView;
            currentContentView.setVisibility(View.VISIBLE);
            if (currentContentView != progressView) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(currentContentView, "alpha", 0, 1).setDuration(500);
                animator.start();
            }
        }
        if (onStateChangeListener != null) {
            onStateChangeListener.onStateChanged(contentState);
        }
    }

    public void setOnEmptyViewClickListener(final View.OnClickListener listener) {
        if (emptyView != null) {
            emptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v);
                }
            });
        }
    }

    public void setOnErrorViewClickListener(final View.OnClickListener listener) {
        if (errorView != null) {
            errorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v);
                }
            });
        }
    }

    public interface OnStateChangeListener {
        void onStateChanged(@BaseStateViewActivity.ContentState int contentState);
    }
}

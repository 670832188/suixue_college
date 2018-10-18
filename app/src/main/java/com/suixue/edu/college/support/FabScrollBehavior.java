package com.suixue.edu.college.support;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * FloatingActionButton.Behavior
 * 与RecyclerView配合使用，滑动时隐藏FloatingActionButton，停止滑动时显示FloatingActionButton
 * Created by cuiyan on 2018/10/17.
 */
public class FabScrollBehavior extends FloatingActionButton.Behavior {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    public FabScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        if (recyclerView == null) {
            recyclerView = findRecyclerView(coordinatorLayout);
            if (recyclerView == null) {
                throw new RuntimeException("not found recyclerView");
            }
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        animateIn();
                    }
                }
            });
        }
        if (fab == null) {
            fab = child;
        }
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, final @NonNull FloatingActionButton child, @NonNull View target, int type) {
//        if (type == ViewCompat.TYPE_NON_TOUCH) {
//            handler.postDelayed(runnable, 500);
//        } else {
//            if (RecyclerView.SCROLL_STATE_IDLE == recyclerView.getScrollState()) {
//                handler.postDelayed(runnable, 500);
//            }
//        }
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        animateOut();
    }

    private boolean isOutRunning;
    private boolean isInRunning;

    private void animateOut() {
        if (isOutRunning || isInRunning) {
            return;
        }
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        int bottomMargin = layoutParams.bottomMargin;
        int translationY = fab.getHeight() + bottomMargin;
        if (fab.getTranslationY() >= translationY) {
            return;
        }
        isOutRunning = true;
        ObjectAnimator outAnimator = ObjectAnimator.ofFloat(fab, "translationY", 0, translationY);
        outAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isOutRunning = false;
            }
        });
        outAnimator.start();
    }

    private void animateIn() {
        if (isInRunning || fab.getTranslationY() <= 0) {
            return;
        }
        isInRunning = true;
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        int bottomMargin = layoutParams.bottomMargin;
        ObjectAnimator inAnimator = ObjectAnimator.ofFloat(fab, "translationY", fab.getHeight() + bottomMargin, 0);
        inAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isInRunning = false;
            }
        });
        inAnimator.start();
    }

    private RecyclerView findRecyclerView(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof RecyclerView) {
                return (RecyclerView) child;
            } else if (child instanceof ViewGroup) {
                return findRecyclerView((ViewGroup) child);
            }
        }
        return null;
    }
}

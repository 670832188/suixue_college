package com.suixue.edu.college.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.kit.basemodule.activity.BaseStateViewActivity;
import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.dev.kit.basemodule.util.LogUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.adapter.InterestAdapter;
import com.suixue.edu.college.entity.InterestInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 游客模式用户在此页面选择感兴趣的消息分类
 * Created by cuiyan on 2018/9/5.
 */
public class InterestActivity extends BaseStateViewActivity {
    private InterestAdapter interestAdapter;
    private static final int[] itemBgColors = {Color.parseColor("#f04e2e"), Color.parseColor("#af62e3"), Color.parseColor("#E64fc6be"), Color.parseColor("#6d9eeb"), Color.parseColor("#ff9900")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showContent(true);
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public View createContentView(LayoutInflater inflater, ViewGroup contentRoot) {
        return inflater.inflate(R.layout.activity_interest, contentRoot, false);
    }

    private void init() {
        generateTestData();
        RecyclerView rvInterest = findViewById(R.id.rv_interest);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 12);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int size = 3;
                if (interestAdapter.getItem(position).isLocalCategoryTitle()) {
                    size = 12;
                } else if (interestAdapter.getItem(position).getCategoryName().length() > 4) {
                    size = 4;
                }
                return size;
            }
        });
        rvInterest.setLayoutManager(layoutManager);
        interestAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                InterestInfo info = interestAdapter.getItem(position);
                if (!info.isLocalCategoryTitle()) {
                    if (info.isChecked()) {
                        info.setChecked(false);
                        interestAdapter.removeSelectedItem(info);
                        interestAdapter.notifyItemChanged(position);
                    } else {
                        if (interestAdapter.getSelectedItemCount() < 5) {
                            info.setChecked(true);
                            interestAdapter.addSelectedItem(info);
                            interestAdapter.notifyItemChanged(position);
                        } else {
                            showToast(R.string.tip_selected_interest_reach_limit);
                        }
                    }

                    if (!info.isChildCategory() && !info.isChildAdded()) {
                        if (info.getSubCategoryList() != null && info.getSubCategoryList().size() > 0) {
                            info.setChildAdded(true);
                            interestAdapter.getDataList().addAll(position + 1, info.getSubCategoryList());
                            interestAdapter.notifyItemRangeInserted(position + 1, info.getSubCategoryList().size());
                            interestAdapter.notifyItemRangeChanged(position + 1, interestAdapter.getDataList().size());
                        }
                    }
                }
            }
        });
        rvInterest.setAdapter(interestAdapter);
    }

    private void generateTestData() {
        List<InterestInfo> dataList = new ArrayList<>();
        InterestInfo titleInfo = new InterestInfo();
        titleInfo.setCategoryName("专业类");
        titleInfo.setLocalCategoryTitle(true);
        dataList.add(titleInfo);
        Random random = new Random();
        int size = random.nextInt(10) + 5;
        int k = 0;
        for (int i = 0; i < size; i++) {
            k++;
            InterestInfo info = new InterestInfo();
            info.setCategoryId(String.valueOf(k));
            info.setCategoryName("专业类" + k);
            info.setBgColor(itemBgColors[i % 5]);
            if (random.nextInt() % 2 == 0) {
                List<InterestInfo> childList = new ArrayList<>();
                int childSize = random.nextInt(5) + 5;
                for (int j = 0; j < childSize; j++) {
                    k++;
                    InterestInfo child = new InterestInfo();
                    child.setCategoryName("专业类" + k);
                    child.setCategoryId(String.valueOf(k));
                    child.setBgColor(itemBgColors[i % 5]);
                    childList.add(child);
                }
                info.setSubCategoryList(childList);
            }
            dataList.add(info);
        }

        titleInfo = new InterestInfo();
        titleInfo.setCategoryName("生活类");
        titleInfo.setLocalCategoryTitle(true);
        dataList.add(titleInfo);
        size = random.nextInt(10) + 5;
        for (int i = 0; i < size; i++) {
            k++;
            InterestInfo info = new InterestInfo();
            info.setCategoryId(String.valueOf(k));
            info.setCategoryName("生活类" + k);
            info.setBgColor(itemBgColors[i % 5]);
            if (random.nextInt() % 2 == 0) {
                List<InterestInfo> childList = new ArrayList<>();
                int childSize = random.nextInt(5) + 5;
                for (int j = 0; j < childSize; j++) {
                    k++;
                    InterestInfo child = new InterestInfo();
                    child.setCategoryName("生活类" + k);
                    child.setCategoryId(String.valueOf(k));
                    child.setBgColor(itemBgColors[i % 5]);
                    childList.add(child);
                }
                info.setSubCategoryList(childList);
            }
            dataList.add(info);
        }
        interestAdapter = new InterestAdapter(this, dataList);
    }
}

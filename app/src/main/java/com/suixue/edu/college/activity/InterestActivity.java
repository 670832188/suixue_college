package com.suixue.edu.college.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.kit.basemodule.activity.BaseStateViewActivity;
import com.dev.kit.basemodule.netRequest.Configs.Config;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestCallback;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestSubscriber;
import com.dev.kit.basemodule.result.BaseResult;
import com.dev.kit.basemodule.surpport.BaseRecyclerAdapter;
import com.suixue.edu.college.R;
import com.suixue.edu.college.adapter.InterestAdapter;
import com.suixue.edu.college.entity.InterestInfo;
import com.suixue.edu.college.entity.InterestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 兴趣爱好
 * Created by cuiyan on 2018/9/5.
 */
public class InterestActivity extends BaseStateViewActivity {
    private static final int maxSelectCount = 5;
    private InterestAdapter interestAdapter;
    private static final int[] itemBgColors = {Color.parseColor("#f04e2e"), Color.parseColor("#af62e3"), Color.parseColor("#E64fc6be"), Color.parseColor("#6d9eeb"), Color.parseColor("#ff9900")};
    private List<InterestInfo> currentSelectedInterestList;
    private List<InterestInfo> previousSelectedInterestList;

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
        setVisibility(R.id.tv_right, View.VISIBLE);
        setText(R.id.tv_right, R.string.action_next_step);
        setOnClickListener(R.id.tv_right, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InterestActivity.this, MainActivity.class));
            }
        });
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
        interestAdapter = new InterestAdapter(this, new ArrayList<InterestInfo>());
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
                        if (interestAdapter.getSelectedItemCount() < maxSelectCount) {
                            info.setChecked(true);
                            interestAdapter.addSelectedItem(info);
                            interestAdapter.notifyItemChanged(position);
                        } else {
                            showToast(String.format(getString(R.string.tip_selected_interest_reach_limit), maxSelectCount));
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
                    setText(R.id.tv_right, String.format(getString(R.string.interest_next_step), interestAdapter.getSelectedItemCount(), maxSelectCount));
                }
            }
        });
        rvInterest.setAdapter(interestAdapter);
        generateTestData();
    }

    private void getInterestList() {
        NetRequestSubscriber<BaseResult<InterestResult>> subscriber = new NetRequestSubscriber<>(new NetRequestCallback<BaseResult<InterestResult>>() {
            @Override
            public void onSuccess(@NonNull BaseResult<InterestResult> result) {
                if (Config.REQUEST_SUCCESS_CODE.equals(result.getCode())) {
                    handleInterestList(result.getData());
                }
            }

            @Override
            public void onResultNull() {
                super.onResultNull();
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        }, this, true, "");
    }

    private void handleInterestList(InterestResult result) {
        if (result == null || (isListEmpty(result.getLifeInterestList()) && isListEmpty(result.getMajorInterestList()))) {
            showToast(R.string.data_empty);
            return;
        }
        List<InterestInfo> dataList = new ArrayList<>();
        List<InterestInfo> majorInterestList = result.getMajorInterestList();
        List<InterestInfo> lifeInterestList = result.getLifeInterestList();
        int bgIndex = 0;
        if (!isListEmpty(majorInterestList)) {
            InterestInfo titleInfo = new InterestInfo();
            titleInfo.setCategoryName(getString(R.string.interest_major));
            titleInfo.setLocalCategoryTitle(true); // 标题
            dataList.add(titleInfo);
            for (InterestInfo info : majorInterestList) {
                info.setBgColor(itemBgColors[bgIndex % 5]);
                if (!isListEmpty(info.getSubCategoryList())) {
                    for (InterestInfo childInfo : info.getSubCategoryList()) {
                        childInfo.setChildCategory(true); // 标记为子类
                        childInfo.setBgColor(itemBgColors[bgIndex % 5]);
                    }
                }
                bgIndex++;
            }
            dataList.addAll(majorInterestList);
        }
        if (!isListEmpty(lifeInterestList)) {
            InterestInfo titleInfo = new InterestInfo();
            titleInfo.setCategoryName(getString(R.string.interest_life));
            titleInfo.setLocalCategoryTitle(true);
            dataList.add(titleInfo);
            for (InterestInfo info : lifeInterestList) {
                info.setBgColor(itemBgColors[bgIndex % 5]);
                if (!isListEmpty(info.getSubCategoryList())) {
                    for (InterestInfo childInfo : info.getSubCategoryList()) {
                        childInfo.setChildCategory(true);
                        childInfo.setBgColor(itemBgColors[bgIndex % 5]);
                    }
                }
                bgIndex++;
            }
            dataList.addAll(lifeInterestList);
        }
        interestAdapter.updateDataList(dataList);

    }


    private void generateTestData() {
        List<InterestInfo> majorInterestList = new ArrayList<>();
        List<InterestInfo> lifeInterestList = new ArrayList<>();
        Random random = new Random();
        int size = random.nextInt(10) + 5;
        int k = 0;
        for (int i = 0; i < size; i++) {
            k++;
            InterestInfo info = new InterestInfo();
            info.setCategoryId(String.valueOf(k));
            info.setCategoryName("专业类" + k);
            if (random.nextInt() % 2 == 0) {
                List<InterestInfo> childList = new ArrayList<>();
                int childSize = random.nextInt(5) + 5;
                for (int j = 0; j < childSize; j++) {
                    k++;
                    InterestInfo child = new InterestInfo();
                    child.setCategoryName("专业类" + k);
                    child.setCategoryId(String.valueOf(k));
                    childList.add(child);
                }
                info.setSubCategoryList(childList);
            }
            majorInterestList.add(info);
        }
        size = random.nextInt(10) + 5;
        for (int i = 0; i < size; i++) {
            k++;
            InterestInfo info = new InterestInfo();
            info.setCategoryId(String.valueOf(k));
            info.setCategoryName("生活类" + k);
            if (random.nextInt() % 2 == 0) {
                List<InterestInfo> childList = new ArrayList<>();
                int childSize = random.nextInt(5) + 5;
                for (int j = 0; j < childSize; j++) {
                    k++;
                    InterestInfo child = new InterestInfo();
                    child.setCategoryName("生活类" + k);
                    child.setCategoryId(String.valueOf(k));
                    childList.add(child);
                }
                info.setSubCategoryList(childList);
            }
            lifeInterestList.add(info);
        }
        InterestResult interestResult = new InterestResult();
        interestResult.setMajorInterestList(majorInterestList);
        interestResult.setLifeInterestList(lifeInterestList);
        handleInterestList(interestResult);
    }
}

package com.suixue.edu.college.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.fragment.BaseFragment;
import com.dev.kit.basemodule.netRequest.model.BaseController;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestCallback;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestSubscriber;
import com.dev.kit.basemodule.netRequest.util.BaseServiceUtil;
import com.dev.kit.basemodule.result.BaseResult;
import com.dev.kit.basemodule.surpport.RecyclerDividerDecoration;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.dev.kit.basemodule.util.StringUtil;
import com.suixue.edu.college.R;
import com.suixue.edu.college.adapter.BlogAdapter;
import com.suixue.edu.college.config.ApiService;
import com.suixue.edu.college.entity.BaseListResult;
import com.suixue.edu.college.entity.BlogContentInfo;
import com.suixue.edu.college.entity.BlogInfo;
import com.suixue.edu.college.entity.RecommendedBloggerInfo;
import com.suixue.edu.college.entity.RecommendedBloggerResult;
import com.suixue.edu.college.util.RefreshUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import me.dkzwm.widget.srl.SmoothRefreshLayout;

import static com.suixue.edu.college.fragment.MainFragment.avatarUrl;
import static com.suixue.edu.college.fragment.MainFragment.thumbList;
import static com.suixue.edu.college.fragment.MainFragment.videoUrls;

/**
 * Created by cuiyan on 2018/9/12.
 */
public class SearchFragment extends BaseFragment {
    private String keyWord;
    private int pageIndex;
    private View rootView;
    private SmoothRefreshLayout refreshLayout;
    private BlogAdapter blogAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frg_search, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        refreshLayout = rootView.findViewById(R.id.refresh_layout);
        RefreshUtil.initMaterialRefreshLayout(refreshLayout);
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new SmoothRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefreshBegin(boolean isRefresh) {
                if (isRefresh) {
                    pageIndex = 1;
                } else {
                    pageIndex++;
                }
                searchBlogList(keyWord);
            }

            @Override
            public void onRefreshComplete(boolean isSuccessful) {

            }
        });
        RecyclerView rvBlog = rootView.findViewById(R.id.rv_blog);
        rvBlog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((BaseActivity) getActivity()).hideKeyBoard();
                return false;
            }
        });
        rvBlog.addItemDecoration(new RecyclerDividerDecoration(RecyclerDividerDecoration.DIVIDER_TYPE_HORIZONTAL, getResources().getColor(R.color.color_main_bg), DisplayUtil.dp2px(5)));
        rvBlog.setLayoutManager(new LinearLayoutManager(getContext()));
        generateTestData();
        final EditText etSearch = rootView.findViewById(R.id.et_search);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String keyWord = etSearch.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !StringUtil.isEmpty(keyWord)) {
                    searchBlogList(keyWord);
                    return true;
                }
                return false;
            }
        });
    }

    private void generateTestData() {
        List<Object> dataList = new ArrayList<>();
        Random random = new Random();
        int size = random.nextInt(10) + 10;
        for (int i = 0; i < size; i++) {
            if (i % 5 == 0) {
                RecommendedBloggerResult recommendedBloggerResult = new RecommendedBloggerResult();
                List<RecommendedBloggerInfo> recommendedBloggerInfoList = new ArrayList<>();
                for (int j = 0; j < 5; j++) {
                    RecommendedBloggerInfo info = new RecommendedBloggerInfo();
                    info.setBloggerAvatarUrl(avatarUrl[Math.abs(random.nextInt() % avatarUrl.length)]);
                    info.setBloggerCoverUrl(thumbList[Math.abs(random.nextInt() % thumbList.length)]);
                    info.setBloggerDesc("风拂二月柳~");
                    info.setBloggerId(String.valueOf(i * 10 + j));
                    info.setBloggerName("李四" + String.valueOf(i * 10 + j));
                    recommendedBloggerInfoList.add(info);
                }
                recommendedBloggerResult.setRecommendedBloggerInfoList(recommendedBloggerInfoList);
                dataList.add(recommendedBloggerResult);
            } else {
                BlogInfo info = new BlogInfo();
                info.setBloggerId(String.valueOf(i + 1));
                info.setBloggerName("张三" + (i + 1));
                info.setAttentionLevel(String.valueOf(random.nextInt(100) + 10));
                info.setBloggerAvatarUrl(thumbList[Math.abs(random.nextInt() % thumbList.length)]);
                int contentItemSize = random.nextInt(5) + 3;
                List<BlogContentInfo> contentInfoList = new ArrayList<>();
                boolean isVideoAdded = false;
                for (int j = 0; j < contentItemSize; j++) {
                    BlogContentInfo contentInfo = new BlogContentInfo();
                    if (random.nextInt() % 3 == 0 && !isVideoAdded) {
                        contentInfo.setContentType(BlogContentInfo.CONTENT_TYPE_VIDEO);
                        contentInfo.setContent(videoUrls[Math.abs(random.nextInt() % videoUrls.length)]);
                        isVideoAdded = true;
                    } else if (Math.abs(random.nextInt() % 2) == 0) {
                        contentInfo.setContent(thumbList[Math.abs(random.nextInt() % thumbList.length)]);
                        contentInfo.setContentType(BlogContentInfo.CONTENT_TYPE_PICTURE);
                    } else {
                        contentInfo.setContent("这是一段文本");
                        contentInfo.setContentType(BlogContentInfo.CONTENT_TYPE_TEXT);
                    }
                    contentInfoList.add(contentInfo);
                }
                info.setBlogContentList(contentInfoList);
                dataList.add(info);
            }
        }
        blogAdapter = new BlogAdapter(getContext(), dataList);
        RecyclerView rvBlog = rootView.findViewById(R.id.rv_blog);
        rvBlog.setAdapter(blogAdapter);
    }

    public void searchBlogList(String keyWord) {
        if (StringUtil.isEmpty(keyWord) || !keyWord.equals(this.keyWord)) {
            pageIndex = 1;
        }
        if (pageIndex == 1) {
            refreshLayout.setEnableNoMoreData(false);
        }
        this.keyWord = keyWord;
        NetRequestSubscriber<BaseResult<BaseListResult<Object>>> subscriber = new NetRequestSubscriber<>(new NetRequestCallback<BaseResult<BaseListResult<Object>>>() {
            @Override
            public void onSuccess(@NonNull BaseResult<BaseListResult<Object>> result) {
                refreshLayout.refreshComplete();
                if (result.getData() == null) {
                    showToast(R.string.data_empty);
                    return;
                }
                if (result.getData().getDataList() != null) {
                    List<Object> dataList = result.getData().getDataList();
                    if (dataList.size() > 0) {
                        if (pageIndex == 1) {
                            blogAdapter.updateDataList(dataList);
                        } else {
                            blogAdapter.appendDataAndRefreshLocal(dataList);
                        }
                    } else {
                        showToast(R.string.data_empty);
                    }
                } else {
                    showToast(R.string.data_empty);
                }
                refreshLayout.setEnableNoMoreData(!result.getData().isHasMoreData());
            }

            @Override
            public void onResultNull() {
                refreshLayout.refreshComplete();
                showToast(R.string.data_empty);
            }

            @Override
            public void onError(Throwable throwable) {
                refreshLayout.refreshComplete();
                showToast(R.string.error_net_request_failed);
            }
        }, getContext());
        Observable<BaseResult<BaseListResult<Object>>> observable = BaseServiceUtil.createService(ApiService.class).searchBlogList(keyWord, pageIndex);
        BaseController.sendRequest(this, subscriber, observable);
    }
}

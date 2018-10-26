package com.suixue.edu.college.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.dev.kit.basemodule.fragment.BaseStateFragment;
import com.dev.kit.basemodule.netRequest.Configs.Config;
import com.dev.kit.basemodule.netRequest.model.BaseController;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestCallback;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestSubscriber;
import com.dev.kit.basemodule.netRequest.util.BaseServiceUtil;
import com.dev.kit.basemodule.result.BaseResult;
import com.dev.kit.basemodule.surpport.RecyclerDividerDecoration;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.dev.kit.basemodule.util.LogUtil;
import com.dev.kit.basemodule.util.StringUtil;
import com.google.gson.Gson;
import com.suixue.edu.college.BuildConfig;
import com.suixue.edu.college.R;
import com.suixue.edu.college.adapter.BlogAdapter;
import com.suixue.edu.college.config.ApiService;
import com.suixue.edu.college.config.Constants;
import com.suixue.edu.college.entity.BaseListResult;
import com.suixue.edu.college.entity.BlogContentInfo;
import com.suixue.edu.college.entity.BlogInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import me.dkzwm.widget.srl.SmoothRefreshLayout;

import static com.suixue.edu.college.fragment.MainFragment.thumbList;
import static com.suixue.edu.college.fragment.MainFragment.videoUrls;

/**
 * Created by cuiyan on 2018/10/25.
 */
public class BloggerBlogFragment extends BaseStateFragment {
    private View rootView;
    private SmoothRefreshLayout refreshLayout;
    private BlogAdapter adapter;
    private int pageIndex;
    private String bloggerId;
    public static final String BLOG_TYPE = "blogType";
    private String blogType;

    @NonNull
    @Override
    public View createContentView(LayoutInflater inflater, FrameLayout flRootContainer) {
        rootView = inflater.inflate(R.layout.frg_blogger_blog, flRootContainer, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentState(STATE_PROGRESS);
        initArguments();
        initView();
        getCourseList();
    }

    private void initArguments() {
        Bundle arg = getArguments();
        if (arg == null || StringUtil.isEmpty(arg.getString(Constants.KEY_BLOGGER_ID))) {
            throw new RuntimeException("missing bloggerId argument");
        }
        bloggerId = arg.getString(Constants.KEY_BLOGGER_ID);
        blogType = arg.getString(BLOG_TYPE);
        if (StringUtil.isEmpty(blogType)) {
            throw new RuntimeException("missing blogType argument");
        }
    }

    private void initView() {
        setOnEmptyViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCourseList();
            }
        });
        adapter = new BlogAdapter(getContext(), new ArrayList<>(), true);
        RecyclerView rvBlog = rootView.findViewById(R.id.rv_blog);
        rvBlog.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBlog.addItemDecoration(new RecyclerDividerDecoration(RecyclerDividerDecoration.DIVIDER_TYPE_HORIZONTAL, getResources().getColor(R.color.color_main_bg), DisplayUtil.dp2px(5)));
        rvBlog.setAdapter(adapter);
        refreshLayout = rootView.findViewById(R.id.refresh_layout);
        refreshLayout.setDurationToClose(500);
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new SmoothRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefreshBegin(boolean isRefresh) {
                getCourseList();
            }

            @Override
            public void onRefreshComplete(boolean isSuccessful) {

            }
        });
    }

    @SuppressWarnings("unchecked")
    private void getCourseList() {
        NetRequestSubscriber<BaseResult<BaseListResult<BlogInfo>>> subscriber = new NetRequestSubscriber<>(new NetRequestCallback<BaseResult<BaseListResult<BlogInfo>>>() {
            @Override
            public void onSuccess(@NonNull BaseResult<BaseListResult<BlogInfo>> result) {
                refreshLayout.refreshComplete();
                if (!Config.REQUEST_SUCCESS_CODE.equals(result.getCode())) {
                    return;
                }
                if (result.getData() == null) {
                    showToast(R.string.data_empty);
                    return;
                }
                if (result.getData().getDataList() != null && result.getData().getDataList().size() > 0) {
                    adapter.appendData((List<Object>) (List<?>) result.getData().getDataList());
                    if (result.getData().isHasMoreData()) {
                        pageIndex++;
                    }
                    refreshLayout.setEnableNoMoreData(!result.getData().isHasMoreData());
                }
            }

            @Override
            public void onResultNull() {
                refreshLayout.refreshComplete();
                showToast(R.string.error_net_request_failed);
            }

            @Override
            public void onError(Throwable throwable) {
                refreshLayout.refreshComplete();
                if (BuildConfig.DEBUG) {
                    generateTestData();
                    setContentState(STATE_DATA_CONTENT);
                    return;
                }
                showToast(R.string.error_net_request_failed);
            }
        }, getContext());
        Observable<BaseResult<BaseListResult<BlogInfo>>> observable = BaseServiceUtil.createService(ApiService.class).getBloggerBlogList(pageIndex, blogType, bloggerId);
        BaseController.sendRequest(this, subscriber, observable);
    }

    @SuppressWarnings("unchecked")
    private void generateTestData() {
        int size = 3;
        List<BlogInfo> list = new ArrayList<>();
        Random random = new Random();
        BaseResult<BaseListResult<BlogInfo>> baseResult = new BaseResult<>();
        BaseListResult<BlogInfo> baseListResult = new BaseListResult<>();
        baseResult.setData(baseListResult);
        for (int i = 0; i < size; i++) {
            BlogInfo info = new BlogInfo();
            info.setBloggerId(String.valueOf(i + 1));
            info.setBloggerName("张三" + (i + 1));
            info.setAttentionLevel(String.valueOf(random.nextInt(100) + 10));
            info.setBloggerAvatarUrl(thumbList[Math.abs(random.nextInt() % thumbList.length)]);
            int contentItemSize = Math.abs(random.nextInt()) % 4 + 1;
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
            int tagSize = Math.abs(random.nextInt() % 6);
            String[] tags = new String[tagSize];
            for (int k = 0; k < tagSize; k++) {
                tags[k] = "标签" + k;
            }
            info.setTags(tags);
            if (Math.abs(random.nextInt()) % 2 == 0) {
                info.setSourceType(BlogInfo.SOURCE_TYPE_SEARCH);
            } else {
                info.setSourceType(BlogInfo.SOURCE_TYPE_RECOMMENDED);
            }
            list.add(info);
        }
        baseListResult.setDataList(list);
        baseListResult.setHasMoreData(true);
        baseListResult.setCurrentPageIndex(1);
        Gson gson = new Gson();
        LogUtil.e("mytag", "blogResult: " + gson.toJson(baseResult));
        adapter.appendDataAndRefreshLocal((List<Object>) (List<?>) list);
    }
}

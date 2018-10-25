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
import com.google.gson.Gson;
import com.suixue.edu.college.BuildConfig;
import com.suixue.edu.college.R;
import com.suixue.edu.college.adapter.BlogAdapter;
import com.suixue.edu.college.config.ApiService;
import com.suixue.edu.college.entity.BaseListResult;
import com.suixue.edu.college.entity.BlogContentInfo;
import com.suixue.edu.college.entity.CourseInfo;

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
public class PersonalCourseFragment extends BaseStateFragment {
    private View rootView;
    private SmoothRefreshLayout refreshLayout;
    private BlogAdapter adapter;
    private int pageIndex;

    @NonNull
    @Override
    public View createContentView(LayoutInflater inflater, FrameLayout flRootContainer) {
        rootView = inflater.inflate(R.layout.frg_personal_course, flRootContainer, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentState(STATE_PROGRESS);
        initView();
        getCourseList();
    }

    private void initView() {
        setOnEmptyViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCourseList();
            }
        });
        adapter = new BlogAdapter(getContext(), new ArrayList<>(), true);
        RecyclerView rvCourse = rootView.findViewById(R.id.rv_course);
        rvCourse.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCourse.addItemDecoration(new RecyclerDividerDecoration(RecyclerDividerDecoration.DIVIDER_TYPE_HORIZONTAL, getResources().getColor(R.color.color_main_bg), DisplayUtil.dp2px(5)));
        rvCourse.setAdapter(adapter);
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
        NetRequestSubscriber<BaseResult<BaseListResult<CourseInfo>>> subscriber = new NetRequestSubscriber<>(new NetRequestCallback<BaseResult<BaseListResult<CourseInfo>>>() {
            @Override
            public void onSuccess(@NonNull BaseResult<BaseListResult<CourseInfo>> result) {
                refreshLayout.refreshComplete();
                if (Config.REQUEST_SUCCESS_CODE.equals(result.getCode()) && result.getData() != null && result.getData().getDataList() != null && result.getData().getDataList().size() > 0) {
                    adapter.appendData((List<Object>) (List<?>) result.getData().getDataList());
                    if (!result.getData().isHasMoreData()) {
                        refreshLayout.setDisableLoadMore(true);
                        refreshLayout.setEnableAutoLoadMore(false);
                    } else {
                        pageIndex++;
                    }
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
        Observable<BaseResult<BaseListResult<CourseInfo>>> observable = BaseServiceUtil.createService(ApiService.class).getCourseList(pageIndex);
        BaseController.sendRequest(this, subscriber, observable);
    }

    @SuppressWarnings("unchecked")
    private void generateTestData() {
        int size = 3;
        List<CourseInfo> list = new ArrayList<>();
        Random random = new Random();
        BaseResult<BaseListResult<CourseInfo>> baseResult = new BaseResult<>();
        BaseListResult<CourseInfo> baseListResult = new BaseListResult<>();
        baseResult.setData(baseListResult);
        for (int i = 0; i < size; i++) {
            CourseInfo info = new CourseInfo();
            info.setBloggerId("12306");
            info.setCourseId("110");
            info.setCommentCount(10086);
            info.setPraisedCount(10011);
            info.setSubscribed(true);
            info.setCourseTitle("大学物理");
            String tags[] = {"吴式枢", "唐敖庆"};
            info.setTags(tags);
            info.setPublishTime("2018 10.24 9:03");
            info.setTransferred(false);
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
            info.setCourseContentList(contentInfoList);
            list.add(info);
        }
        baseListResult.setDataList(list);
        baseListResult.setHasMoreData(true);
        baseListResult.setCurrentPageIndex(1);
        Gson gson = new Gson();
        LogUtil.e("mytag", "courseResult: " + gson.toJson(baseResult));
        adapter.appendDataAndRefreshLocal((List<Object>) (List<?>) list);
    }
}

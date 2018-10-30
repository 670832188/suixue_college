package com.suixue.edu.college.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.kit.basemodule.fragment.BaseFragment;
import com.dev.kit.basemodule.netRequest.Configs.Config;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestCallback;
import com.dev.kit.basemodule.netRequest.subscribers.NetRequestSubscriber;
import com.dev.kit.basemodule.result.BaseResult;
import com.dev.kit.basemodule.surpport.FragmentAdapter;
import com.dev.kit.basemodule.util.GlideUtil;
import com.dev.kit.basemodule.util.StringUtil;
import com.suixue.edu.college.BuildConfig;
import com.suixue.edu.college.R;
import com.suixue.edu.college.activity.MainActivity;
import com.suixue.edu.college.activity.RegisterActivity;
import com.suixue.edu.college.config.Constants;
import com.suixue.edu.college.entity.BloggerInfo;
import com.suixue.edu.college.entity.UserInfo;
import com.suixue.edu.college.util.PreferenceUtil;
import com.suixue.edu.college.view.GradualTitleView;

/**
 * Created by cuiyan on 2018/9/12.
 */
public class PersonalFragment extends BaseFragment implements FragmentAdapter.FragmentFactory {

    public static final String IS_BLOGGER_SELF_BROWSE = "isBloggerSelfBrowse";
    // 标识访问来源：访问者/博主本人
    private boolean isBloggerSelfBrowse;
    private View rootView;
    private TextView tvSubscribe;
    private TextView tvConcern;
    private TextView tvEdit;
    private ViewPager vpFrg;
    private String[] tabTitleArray;
    private String bloggerId;
    private boolean isInitialized;
    private BloggerInfo bloggerInfo;
    private BloggerCourseFragment bloggerCourseFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frg_personal, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initArguments(false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isInitialized) {
            init();
        }
    }

    private void init() {
        if (initArguments(true)) {
            initView();
            isInitialized = true;
            bloggerInfo = generateTestData();
            renderBloggerInfo();
        }
    }

    private boolean initArguments(boolean startLogin) {
        Activity activity = getActivity();
        isBloggerSelfBrowse = activity instanceof MainActivity;
        if (isBloggerSelfBrowse) {
            UserInfo userInfo = PreferenceUtil.getUserInfo();
            if (userInfo == null || StringUtil.isEmpty(userInfo.getUserId())) {
                if (startLogin) {
                    Intent intent = new Intent(getContext(), RegisterActivity.class);
                    intent.putExtra(RegisterActivity.IS_NEED_REGISTER_RESULT, true);
                    intent.putExtra(Constants.KEY_REGISTER_MODE, Constants.VALUE_REGISTER_MODE_USER);
                    startActivityForResult(intent, Constants.REQUEST_CODE_REGISTER_FROM_ME);
                }
                rootView.findViewById(R.id.app_bar_layout).setVisibility(View.GONE);
                rootView.findViewById(R.id.ll_content_container).setVisibility(View.GONE);
                rootView.findViewById(R.id.rl_login_tip).setVisibility(View.VISIBLE);
                rootView.findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), RegisterActivity.class);
                        intent.putExtra(RegisterActivity.IS_NEED_REGISTER_RESULT, true);
                        intent.putExtra(Constants.KEY_REGISTER_MODE, Constants.VALUE_REGISTER_MODE_USER);
                        startActivityForResult(intent, Constants.REQUEST_CODE_REGISTER_FROM_ME);
                    }
                });
                return false;
            }
            bloggerId = userInfo.getUserId();
        } else {
            Bundle arg = getArguments();
            if (arg == null || StringUtil.isEmpty(arg.getString(Constants.KEY_BLOGGER_ID))) {
                throw new RuntimeException("missing bloggerId argument");
            }
            bloggerId = arg.getString(Constants.KEY_BLOGGER_ID);
        }
        return true;
    }

    private void initView() {
        rootView.findViewById(R.id.app_bar_layout).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.ll_content_container).setVisibility(View.VISIBLE);
        tvSubscribe = rootView.findViewById(R.id.tv_subscribe);
        tvConcern = rootView.findViewById(R.id.tv_concern);
        tvEdit = rootView.findViewById(R.id.tv_edit);
        if (!isBloggerSelfBrowse) {
            rootView.findViewById(R.id.tool_bar).setVisibility(View.VISIBLE);
            final GradualTitleView titleView = rootView.findViewById(R.id.title_view);
            final int coverHeight = getResources().getDimensionPixelSize(R.dimen.personal_top_cover_height);
            AppBarLayout appBarLayout = rootView.findViewById(R.id.app_bar_layout);
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    float appBarLayoutOffsetRatio = Math.abs((float) verticalOffset / (coverHeight - titleView.getHeight()));
                    titleView.changeRatio((float) Math.pow(appBarLayoutOffsetRatio, 3));
                }
            });
            titleView.setOnLeftBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.finish();
                    }
                }
            });
            tvEdit.setVisibility(View.GONE);
            tvSubscribe.setVisibility(View.VISIBLE);
            tvConcern.setVisibility(View.VISIBLE);
        } else {
            tvEdit.setVisibility(View.VISIBLE);
            tvSubscribe.setVisibility(View.GONE);
            tvConcern.setVisibility(View.GONE);
        }
        TabLayout tabLayout = rootView.findViewById(R.id.sliding_tabs);
        tabTitleArray = getResources().getStringArray(R.array.personal_tab_title);
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getChildFragmentManager(), this) {
            @Override
            public String getPageTitle(int position) {
                return tabTitleArray[position];
            }
        };
        vpFrg = rootView.findViewById(R.id.vp_frg);
        vpFrg.setOffscreenPageLimit(3);
        vpFrg.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(vpFrg);
        tabLayout.clearOnTabSelectedListeners();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpFrg.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void getBloggerInfo() {
        NetRequestSubscriber<BaseResult<BloggerInfo>> subscriber = new NetRequestSubscriber<>(new NetRequestCallback<BaseResult<BloggerInfo>>() {
            @Override
            public void onSuccess(@NonNull BaseResult<BloggerInfo> result) {
                if (Config.REQUEST_SUCCESS_CODE.equals(result.getCode())) {
                    bloggerInfo = result.getData();
                    renderBloggerInfo();
                }
            }

            @Override
            public void onResultNull() {
                if (bloggerInfo == null) {
                    showToast(R.string.error_net_request_failed);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                if (BuildConfig.DEBUG) {
                    bloggerInfo = generateTestData();
                    renderBloggerInfo();
                }
                if (bloggerInfo == null) {
                    showToast(R.string.error_net_request_failed);
                }
            }
        }, getContext());
    }

    private BloggerInfo generateTestData() {
        BloggerInfo bloggerInfo = new BloggerInfo();
        bloggerInfo.setAvatarUrl("http://img19.3lian.com/d/file/201803/05/fa6cf18ea93c86703344a2b95c437048.png");
        bloggerInfo.setConcernedNum(3600);
        bloggerInfo.setSubscribedNum(396);
        bloggerInfo.setCoverImgUrl("http://img19.3lian.com/d/file/201803/10/292711fbc1fb75bb7d0d87717ddfed7c.jpg");
        bloggerInfo.setPersonalBrief("清泉石上流");
        bloggerInfo.setSubscribeFee(99);
        bloggerInfo.setName("太虚真人");
        return bloggerInfo;
    }

    private void renderBloggerInfo() {
        if (bloggerInfo == null) {
            return;
        }
        ImageView ivAvatar = rootView.findViewById(R.id.iv_avatar);
        GlideUtil.loadImage(getContext(), bloggerInfo.getAvatarUrl(), R.mipmap.ic_launcher, R.mipmap.ic_launcher, ivAvatar, 1);
        ImageView ivCover = rootView.findViewById(R.id.iv_cover);
        GlideUtil.loadImage(getContext(), bloggerInfo.getCoverImgUrl(), R.mipmap.ic_launcher, R.mipmap.ic_launcher, ivCover, 1);
        TextView tvSubscribeNum = rootView.findViewById(R.id.tv_subscribe_num);
        tvSubscribeNum.setText(String.valueOf(bloggerInfo.getSubscribedNum()));
        TextView tvConcernNum = rootView.findViewById(R.id.tv_concern_num);
        tvConcernNum.setText(String.valueOf(bloggerInfo.getConcernedNum()));
        if (!isBloggerSelfBrowse) {
            TextView tvSubscribe = rootView.findViewById(R.id.tv_subscribe);
            tvSubscribe.setText(String.format(getString(R.string.action_subscribe), String.valueOf(bloggerInfo.getSubscribeFee())));
        }
        TextView tvBloggerName = rootView.findViewById(R.id.tv_blogger_name);
        tvBloggerName.setText(bloggerInfo.getName());
        TextView tvPersonalBrief = rootView.findViewById(R.id.tv_personal_brief);
        tvPersonalBrief.setText(bloggerInfo.getPersonalBrief());
    }

    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        Bundle arg = new Bundle();
        switch (position) {
            case 0: {
                arg.putString(Constants.KEY_BLOGGER_ID, bloggerId);
                bloggerCourseFragment = new BloggerCourseFragment();
                bloggerCourseFragment.setArguments(arg);
                return bloggerCourseFragment;
            }
            case 1: {
                arg.putString(Constants.KEY_BLOGGER_ID, bloggerId);
                arg.putString(Constants.KEY_BLOG_TYPE, Constants.BLOG_TYPE_SELF_OR_TRANSFERRED);
                fragment = new BloggerBlogFragment();
                break;
            }
            case 2: {
                arg.putString(Constants.KEY_BLOGGER_ID, bloggerId);
                arg.putString(Constants.KEY_BLOG_TYPE, Constants.BLOG_TYPE_PRAISED);
                fragment = new BloggerBlogFragment();
                break;
            }
            default: {
                arg.putString(Constants.KEY_BLOGGER_ID, bloggerId);
                fragment = new BloggerCourseFragment();
                break;
            }
        }
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public int getFragmentCount() {
        return isBloggerSelfBrowse ? 4 : 3;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_REGISTER_FROM_ME && resultCode == Activity.RESULT_OK) {
            rootView.findViewById(R.id.app_bar_layout).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.ll_content_container).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.rl_login_tip).setVisibility(View.GONE);
            init();
        }
    }
}

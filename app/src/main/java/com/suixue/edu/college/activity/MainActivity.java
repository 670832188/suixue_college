package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.surpport.RecyclerDividerDecoration;
import com.dev.kit.basemodule.util.DisplayUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.suixue.edu.college.R;
import com.suixue.edu.college.adapter.BlogAdapter;
import com.suixue.edu.college.entity.BlogContentInfo;
import com.suixue.edu.college.entity.BlogInfo;
import com.suixue.edu.college.entity.RecommendedBloggerInfo;
import com.suixue.edu.college.entity.RecommendedBloggerResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: cuiyan
 * Date:   18/8/26 23:01
 * Desc:
 */
public class MainActivity extends BaseActivity {
    private BlogAdapter adapter;
    private static final String[] thumbList =
            {
                    "http://img19.3lian.com/d/file/201803/09/b701b8995fccf7d7664636765a519008.jpg",
                    "http://img19.3lian.com/d/file/201803/09/27b8e420b535310d6420eb42c000956a.jpg",
                    "http://img19.3lian.com/d/file/201803/09/d53b7b6804298cbfe30e379505dba03c.jpg",
                    "http://img19.3lian.com/d/file/201803/09/b69a01203075c52a28844a7d52e32d8b.jpg",
                    "http://img19.3lian.com/d/file/201803/09/a8cb38251d3d2244eae41923b19e2de3.jpg",
                    "http://img19.3lian.com/d/file/201803/09/ec8c139d6760cdd2274bb0362df73877.jpg",
                    "http://img18.3lian.com/d/file/201709/28/a8835327b8e05f48b85426e3dd121091.jpg",
                    "http://img18.3lian.com/d/file/201709/28/fe795f27efc072a5bff0645a0ba6ba1e.jpg",
                    "http://img18.3lian.com/d/file/201709/28/12a4e4bac045735da67f10c807aadbde.jpg",
            };

    private static final String[] avatarUrl = {
            "http://img19.3lian.com/d/file/201803/05/635adb96f8a4c0d41e4292ad01b52044.png",
            "http://img19.3lian.com/d/file/201803/05/fa6cf18ea93c86703344a2b95c437048.png",
            "http://img19.3lian.com/d/file/201803/05/3d59d002675cfeac149550c49f9189a4.png",
            "http://img19.3lian.com/d/file/201804/17/d1e2ff112d89de99362ab1ec161ac89a.jpg",
            "http://img19.3lian.com/d/file/201804/17/564553543ae3fb6e54dc96b0073ae1f7.jpg",
            "http://img19.3lian.com/d/file/201804/17/917cea7c38dfaf09c15a84a3ecc380f4.jpg",
            "http://img19.3lian.com/d/file/201804/17/cdb99115a97aa9b8a99f52f200c6172d.jpg",
            "http://img19.3lian.com/d/file/201804/17/e9ef0fdf40a70df14821aa6be5a69685.jpg",
            "http://img19.3lian.com/d/file/201804/17/514f5c6966876fd43eccb432fa6113cb.jpg",
    };

    private static final String[] videoUrls = {
            "http://ksy.fffffive.com/mda-hinp1ik37b0rt1mj/mda-hinp1ik37b0rt1mj.mp4",
            "http://ksy.fffffive.com/mda-himtqzs2un1u8x2v/mda-himtqzs2un1u8x2v.mp4",
            "http://ksy.fffffive.com/mda-hiw5zixc1ghpgrhn/mda-hiw5zixc1ghpgrhn.mp4",
            "http://ksy.fffffive.com/mda-hiw61ic7i4qkcvmx/mda-hiw61ic7i4qkcvmx.mp4",
            "http://ksy.fffffive.com/mda-hihvysind8etz7ga/mda-hihvysind8etz7ga.mp4",
            "http://ksy.fffffive.com/mda-hiw60i3kczgum0av/mda-hiw60i3kczgum0av.mp4",
            "http://ksy.fffffive.com/mda-hidnzn5r61qwhxp4/mda-hidnzn5r61qwhxp4.mp4",
            "http://ksy.fffffive.com/mda-he1zy3rky0rwrf60/mda-he1zy3rky0rwrf60.mp4",
            "http://ksy.fffffive.com/mda-hh6cxd0dqjqcszcj/mda-hh6cxd0dqjqcszcj.mp4",
            "http://ksy.fffffive.com/mda-hifsrhtqjn8jxeha/mda-hifsrhtqjn8jxeha.mp4",
            "http://ksy.fffffive.com/mda-hics799vjrg0w5az/mda-hics799vjrg0w5az.mp4",
            "http://ksy.fffffive.com/mda-hfshah045smezhtf/mda-hfshah045smezhtf.mp4",
            "http://ksy.fffffive.com/mda-hh4mbturm902j7wi/mda-hh4mbturm902j7wi.mp4",
            "http://ksy.fffffive.com/mda-hiwxzficjivwmsch/mda-hiwxzficjivwmsch.mp4",
            "http://ksy.fffffive.com/mda-hhug2p7hfbhnv40r/mda-hhug2p7hfbhnv40r.mp4",
            "http://ksy.fffffive.com/mda-hieuuaei6cufye2c/mda-hieuuaei6cufye2c.mp4",
            "http://ksy.fffffive.com/mda-hibhufepe5m1tfw1/mda-hibhufepe5m1tfw1.mp4",
            "http://ksy.fffffive.com/mda-hhzeh4c05ivmtiv7/mda-hhzeh4c05ivmtiv7.mp4",
            "http://ksy.fffffive.com/mda-hfrigfn2y9jvzm72/mda-hfrigfn2y9jvzm72.mp4",
            "http://ksy.fffffive.com/mda-himek207gvvqg3wq/mda-himek207gvvqg3wq.mp4"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        setContentView(R.layout.layout_blog_list);
        init();
    }

    private void init() {
        RecyclerView rvBlog = findViewById(R.id.rv_blog);
        rvBlog.addItemDecoration(new RecyclerDividerDecoration(RecyclerDividerDecoration.DIVIDER_TYPE_HORIZONTAL, getResources().getColor(R.color.color_main_bg), DisplayUtil.dp2px(5)));
        rvBlog.setLayoutManager(new LinearLayoutManager(this));
        generateTestData();
        rvBlog.setAdapter(adapter);
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
                    if (random.nextInt() % 2 == 0) {
                        info.setBloggerCoverDesc("李四" + (j + 1) + "的博客");
                    }
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
        adapter = new BlogAdapter(this, dataList);
    }

    @Override
    public void onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}

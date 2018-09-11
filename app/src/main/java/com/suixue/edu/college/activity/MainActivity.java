package com.suixue.edu.college.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dev.kit.basemodule.activity.BaseActivity;
import com.dev.kit.basemodule.surpport.RecyclerDividerDecoration;
import com.dev.kit.basemodule.util.DisplayUtil;
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
                    "http://img19.3lian.com/d/file/201803/13/580bae0442a95837bd6f2724a22c9a3b.jpg",
                    "http://img19.3lian.com/d/file/201803/10/292711fbc1fb75bb7d0d87717ddfed7c.jpg",
                    "http://img19.3lian.com/d/file/201803/13/b8d6f128d9363249060cebb5be7c5a53.jpg",
                    "http://img19.3lian.com/d/file/201803/10/9148e66ead8afa3751c971f0c49882f0.jpg",
                    "http://img19.3lian.com/d/file/201803/13/e5f77d6b93426a53cb37e1d1a103ecaf.jpg",
                    "http://img19.3lian.com/d/file/201803/10/161a0853bb9eff46b8bb2ce165ca7bfb.jpg"
            };

    private static final String[] avatarUrl = {
            "http://img19.3lian.com/d/file/201804/17/d1e2ff112d89de99362ab1ec161ac89a.jpg",
            "http://img19.3lian.com/d/file/201804/17/564553543ae3fb6e54dc96b0073ae1f7.jpg",
            "http://img19.3lian.com/d/file/201804/17/917cea7c38dfaf09c15a84a3ecc380f4.jpg",
            "http://img19.3lian.com/d/file/201804/17/cdb99115a97aa9b8a99f52f200c6172d.jpg",
            "http://img19.3lian.com/d/file/201804/17/e9ef0fdf40a70df14821aa6be5a69685.jpg",
            "http://img19.3lian.com/d/file/201804/17/514f5c6966876fd43eccb432fa6113cb.jpg"
    };

    private static final String[] videoUrls = {
            "http://jzvd.nathen.cn/6ea7357bc3fa4658b29b7933ba575008/fbbba953374248eb913cb1408dc61d85-5287d2089db37e62345123a1be272f8b.mp4",
            "http://jzvd.nathen.cn/35b3dc97fbc240219961bd1fccc6400b/8d9b76ab5a584bce84a8afce012b72d3-5287d2089db37e62345123a1be272f8b.mp4",
            "http://jzvd.nathen.cn/df6096e7878541cbbea3f7298683fbed/ef76450342914427beafe9368a4e0397-5287d2089db37e62345123a1be272f8b.mp4",
            "http://jzvd.nathen.cn/384d341e000145fb82295bdc54ecef88/103eab5afca34baebc970378dd484942-5287d2089db37e62345123a1be272f8b.mp4",
            "http://jzvd.nathen.cn/f55530ba8a59403da0621cbf4faef15e/adae4f2e3ecf4ea780beb057e7bce84c-5287d2089db37e62345123a1be272f8b.mp4",
            "http://jzvd.nathen.cn/6340efd1962946ad80eeffd19b3be89c/65b499c0f16e4dd8900497e51ffa0949-5287d2089db37e62345123a1be272f8b.mp4",
            "http://jzvd.nathen.cn/f07fa9fddd1e45a6ae1570c7fe7967c1/c6db82685b894e25b523b1cb28d79f2e-5287d2089db37e62345123a1be272f8b.mp4",
            "http://jzvd.nathen.cn/d2e969f2ec734520b46ab0965d2b68bd/f124edfef6c24be8b1a7b7f996ccc5e0-5287d2089db37e62345123a1be272f8b.mp4",
            "http://jzvd.nathen.cn/4f965ad507ef4194a60a943a34cfe147/32af151ea132471f92c9ced2cff785ea-5287d2089db37e62345123a1be272f8b.mp4",
            "http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
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
                    info.setBloggerAvatarUrl(avatarUrl[Math.abs(random.nextInt() % 6)]);
                    info.setBloggerCoverUrl(thumbList[Math.abs(random.nextInt() % 6)]);
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
                info.setBloggerAvatarUrl(thumbList[Math.abs(random.nextInt() % 6)]);
                int contentItemSize = random.nextInt(5) + 3;
                List<BlogContentInfo> contentInfoList = new ArrayList<>();
                boolean isVideoAdded = false;
                for (int j = 0; j < contentItemSize; j++) {
                    BlogContentInfo contentInfo = new BlogContentInfo();
                    if (random.nextInt() % 3 == 0 && !isVideoAdded) {
                        contentInfo.setContentType(BlogContentInfo.CONTENT_TYPE_VIDEO);
                        contentInfo.setContent(videoUrls[Math.abs(random.nextInt() % 10)]);
                        isVideoAdded = true;
                    } else if (Math.abs(random.nextInt() % 2) == 0) {
                        contentInfo.setContent(thumbList[Math.abs(random.nextInt() % 6)]);
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
}

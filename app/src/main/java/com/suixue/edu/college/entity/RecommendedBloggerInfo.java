package com.suixue.edu.college.entity;

import java.util.List;

/**
 * Created by cuiyan on 2018/9/10.
 */
public class RecommendedBloggerInfo {
    private String bloggerId;
    private String bloggerAvatarUrl;
    private String bloggerName;
    // 博主博客封面图片url
    private String bloggerCoverUrl;
    // 博主博客描述
    private String bloggerDesc;
    // 是否被用户关注
    private boolean isConcerned;
    // 最近发布的图片url，取三张
    private List<String> latestPictures;

    public String getBloggerId() {
        return bloggerId;
    }

    public String getBloggerAvatarUrl() {
        return bloggerAvatarUrl;
    }

    public String getBloggerName() {
        return bloggerName;
    }

    public boolean isConcerned() {
        return isConcerned;
    }

    public String getBloggerCoverUrl() {
        return bloggerCoverUrl;
    }


    public String getBloggerDesc() {
        return bloggerDesc;
    }

    public List<String> getLatestPictures() {
        return latestPictures;
    }

    //////////////////////////////////////////////

    public void setBloggerId(String bloggerId) {
        this.bloggerId = bloggerId;
    }

    public void setBloggerAvatarUrl(String bloggerAvatarUrl) {
        this.bloggerAvatarUrl = bloggerAvatarUrl;
    }

    public void setBloggerName(String bloggerName) {
        this.bloggerName = bloggerName;
    }

    public void setConcerned(boolean concerned) {
        isConcerned = concerned;
    }

    public void setBloggerCoverUrl(String bloggerCoverUrl) {
        this.bloggerCoverUrl = bloggerCoverUrl;
    }

    public void setBloggerDesc(String bloggerDesc) {
        this.bloggerDesc = bloggerDesc;
    }

    public void setLatestPictures(List<String> latestPictures) {
        this.latestPictures = latestPictures;
    }
}

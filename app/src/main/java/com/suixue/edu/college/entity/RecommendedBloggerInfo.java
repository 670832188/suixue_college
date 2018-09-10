package com.suixue.edu.college.entity;

/**
 * Created by cuiyan on 2018/9/10.
 */
public class RecommendedBloggerInfo {
    private String bloggerId;
    private String bloggerAvatarUrl;
    private String bloggerName;
    // 是否被用户关注
    private boolean isConcerned;

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
}

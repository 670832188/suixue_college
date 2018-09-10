package com.suixue.edu.college.entity;

import java.util.List;

/**
 * 帖子信息
 * Created by cuiyan on 2018/8/30.
 */
public class BlogInfo {
    private String bloggerName;
    private String bloggerId;
    private String bloggerAvatarUrl;
    // 是否被用户关注
    private boolean isConcerned;
    // 帖子内容数据列表
    private List<BlogContentInfo> blogContentList;
    // 帖子来源：推荐/搜索 等
    private String source;
    // 关注度、热度
    private String attentionLevel;

    public String getBloggerName() {
        return bloggerName;
    }

    public String getBloggerId() {
        return bloggerId;
    }

    public String getBloggerAvatarUrl() {
        return bloggerAvatarUrl;
    }

    public boolean isConcerned() {
        return isConcerned;
    }

    public List<BlogContentInfo> getBlogContentList() {
        return blogContentList;
    }

    public String getSource() {
        return source;
    }

    public String getAttentionLevel() {
        return attentionLevel;
    }

    public void setBloggerName(String bloggerName) {
        this.bloggerName = bloggerName;
    }

    public void setBloggerId(String bloggerId) {
        this.bloggerId = bloggerId;
    }

    public void setBloggerAvatarUrl(String bloggerAvatarUrl) {
        this.bloggerAvatarUrl = bloggerAvatarUrl;
    }

    public void setConcerned(boolean concerned) {
        isConcerned = concerned;
    }

    public void setBlogContentList(List<BlogContentInfo> blogContentList) {
        this.blogContentList = blogContentList;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setAttentionLevel(String attentionLevel) {
        this.attentionLevel = attentionLevel;
    }
}

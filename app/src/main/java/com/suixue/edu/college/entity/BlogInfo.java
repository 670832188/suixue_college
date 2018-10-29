package com.suixue.edu.college.entity;

import java.util.List;

/**
 * 帖子信息
 * Created by cuiyan on 2018/8/30.
 */
public class BlogInfo {
    public static final String SOURCE_TYPE_SEARCH = "1";
    public static final String SOURCE_TYPE_RECOMMENDED = "2";
    private String bloggerName;
    private String bloggerId;
    private String blogId;
    private String bloggerAvatarUrl;
    // 是否被用户点赞/喜欢
    private boolean isPraised;
    // 帖子内容数据列表
    private List<BlogContentInfo> blogContentList;
    // 帖子来源：1：搜索；2：推荐
    private String sourceType;
    // 关注度、热度
    private String attentionLevel;
    // 博客标签
    private String[] tags;

    public String getBloggerName() {
        return bloggerName;
    }

    public String getBloggerId() {
        return bloggerId;
    }

    public String getBlogId() {
        return blogId;
    }

    public String getBloggerAvatarUrl() {
        return bloggerAvatarUrl;
    }

    public boolean isPraised() {
        return isPraised;
    }

    public List<BlogContentInfo> getBlogContentList() {
        return blogContentList;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getAttentionLevel() {
        return attentionLevel;
    }

    public String[] getTags() {
        return tags;
    }

    //////////////////////////////////////
    public void setBloggerName(String bloggerName) {
        this.bloggerName = bloggerName;
    }

    public void setBloggerId(String bloggerId) {
        this.bloggerId = bloggerId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public void setBloggerAvatarUrl(String bloggerAvatarUrl) {
        this.bloggerAvatarUrl = bloggerAvatarUrl;
    }

    public void setPraised(boolean isPraised) {
        this.isPraised = isPraised;
    }

    public void setBlogContentList(List<BlogContentInfo> blogContentList) {
        this.blogContentList = blogContentList;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public void setAttentionLevel(String attentionLevel) {
        this.attentionLevel = attentionLevel;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}

package com.suixue.edu.college.entity;

import java.util.List;

/**
 * Created by cuiyan on 2018/10/24.
 */
public class CourseInfo {
    private String bloggerId;
    private String courseId;
    private String courseTitle;
    private String[] tags;
    // 是否为转发他人课程
    private boolean isTransferred;
    // 是否已订阅(访问者使用，若访问者已订阅被访问者，则为true)
    private boolean isSubscribed;
    private long commentCount;
    // 被喜欢(点赞)数量
    private long praisedCount;
    private String publishTime;
    private List<BlogContentInfo> courseContentList;

    public String getBloggerId() {
        return bloggerId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String[] getTags() {
        return tags;
    }

    public boolean isTransferred() {
        return isTransferred;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public long getPraisedCount() {
        return praisedCount;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public List<BlogContentInfo> getCourseContentList() {
        return courseContentList;
    }

    /////////////////////////////////////


    public void setBloggerId(String bloggerId) {
        this.bloggerId = bloggerId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setTransferred(boolean isTransferred) {
        this.isTransferred = isTransferred;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public void setPraisedCount(long praisedCount) {
        this.praisedCount = praisedCount;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public void setCourseContentList(List<BlogContentInfo> courseContentList) {
        this.courseContentList = courseContentList;
    }
}

package com.suixue.edu.college.entity;

import java.util.List;

/**
 * Created by cuiyan on 2018/10/24.
 */
public class CourseInfo {
    private String bloggerId;
    private String courseId;
    private String courseName;
    private String[] tags;
    // 是否为转发他人课程
    private boolean isTransferred;
    // 是否已订阅(访问者使用，若访问者已订阅被访问者，则为true)
    private boolean isSubscribed;
    private long commentsCount;
    // 被喜欢(点赞)数量
    private long praisedCount;
    private List<BlogContentInfo> courseContentList;

    public String getBloggerId() {
        return bloggerId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
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

    public void setCommentsCount(long commentsCount) {
        this.commentsCount = commentsCount;
    }

    public void setPraisedCount(long praisedCount) {
        this.praisedCount = praisedCount;
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

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public void setTransferred(boolean transferred) {
        isTransferred = transferred;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public long getCommentsCount() {
        return commentsCount;
    }

    public long getPraisedCount() {
        return praisedCount;
    }

    public void setCourseContentList(List<BlogContentInfo> courseContentList) {
        this.courseContentList = courseContentList;
    }
}

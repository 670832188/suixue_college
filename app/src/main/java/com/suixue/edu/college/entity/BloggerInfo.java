package com.suixue.edu.college.entity;

/**
 * 我的页面/博主个人页面数据
 * Created by cuiyan on 2018/10/29.
 */
public class BloggerInfo {
    private String name;
    private String coverImgUrl;
    private String personalBrief;
    private String avatarUrl;
    // 订阅数
    private int subscribedNum;
    // 关注数
    private int concernedNum;
    // 订阅博主所需费用
    private int subscribeFee;
    // 是否被关注
    private boolean isConcerned;

    public String getName() {
        return name;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public String getPersonalBrief() {
        return personalBrief;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public int getSubscribedNum() {
        return subscribedNum;
    }

    public int getConcernedNum() {
        return concernedNum;
    }

    public int getSubscribeFee() {
        return subscribeFee;
    }

    public boolean isConcerned() {
        return isConcerned;
    }

    /////////////////////////


    public void setName(String name) {
        this.name = name;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public void setPersonalBrief(String personalBrief) {
        this.personalBrief = personalBrief;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setSubscribedNum(int subscribedNum) {
        this.subscribedNum = subscribedNum;
    }

    public void setConcernedNum(int concernedNum) {
        this.concernedNum = concernedNum;
    }

    public void setSubscribeFee(int subscribeFee) {
        this.subscribeFee = subscribeFee;
    }

    public void setConcerned(boolean concerned) {
        isConcerned = concerned;
    }
}

package com.suixue.edu.college.entity;

/**
 * Author: cuiyan
 * Date:   18/8/26 22:42
 * Desc:
 */
public class UserInfo {

    private String userName;
    private String userId;
    private String mobile;
    private String userAvatarUrl;
    private String authToken;

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getMobile() {
        return mobile;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public String getAuthToken() {
        return authToken;
    }
    // testCode


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

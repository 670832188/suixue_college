package com.suixue.edu.college.entity;

/**
 * Created by cuiyan on 2018/10/9.
 */
public class ChatSessionInfo {
    private String sessionId;
    private String latestMsg;
    private String latestMsgTime;
    // 交流人名称
    private String communicatorName;
    private String communicatorAvatarUrl;
    private boolean hasUnreadMsg;

    public String getSessionId() {
        return sessionId;
    }

    public String getLatestMsg() {
        return latestMsg;
    }

    public String getLatestMsgTime() {
        return latestMsgTime;
    }

    public String getCommunicatorName() {
        return communicatorName;
    }

    public String getCommunicatorAvatarUrl() {
        return communicatorAvatarUrl;
    }

    public boolean isHasUnreadMsg() {
        return hasUnreadMsg;
    }

    /////


    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setLatestMsg(String latestMsg) {
        this.latestMsg = latestMsg;
    }

    public void setLatestMsgTime(String latestMsgTime) {
        this.latestMsgTime = latestMsgTime;
    }

    public void setCommunicatorName(String communicatorName) {
        this.communicatorName = communicatorName;
    }

    public void setCommunicatorAvatarUrl(String communicatorAvatarUrl) {
        this.communicatorAvatarUrl = communicatorAvatarUrl;
    }

    public void setHasUnreadMsg(boolean hasUnreadMsg) {
        this.hasUnreadMsg = hasUnreadMsg;
    }
}

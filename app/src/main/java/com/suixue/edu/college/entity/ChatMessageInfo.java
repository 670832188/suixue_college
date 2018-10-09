package com.suixue.edu.college.entity;

/**
 * Created by cuiyan on 2018/10/9.
 */
public class ChatMessageInfo {
    // 对应博主id
    private String creatorId;
    private String creatorName;
    private String avatarUrl;
    // 1: 接收；2：发送
    private int type;
    private String content;

    public String getCreatorId() {
        return creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    ///////////////////////////////////////

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

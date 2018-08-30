package com.suixue.edu.college.entity;

/**
 * 帖子信息
 * Created by cuiyan on 2018/8/30.
 */
public class PostInfo {
    private String creatorName;
    private String creatorId;
    private String creatorAvatarUrl;
    // 是否被用户关注
    private boolean isConcerned;
    // 帖子内容：数据列表json字符串
    private String postContent;
    // 帖子来源：推荐/搜索 等
    private String source;
    // 关注度、热度
    private String attentionLevel;
}

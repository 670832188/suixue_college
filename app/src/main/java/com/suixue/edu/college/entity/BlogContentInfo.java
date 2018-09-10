package com.suixue.edu.college.entity;

/**
 * 帖子内容
 * Created by cuiyan on 2018/8/30.
 */
public class BlogContentInfo {
    public static final String CONTENT_TYPE_PICTURE = "picture";
    public static final String CONTENT_TYPE_VIDEO = "video";
    public static final String CONTENT_TYPE_TEXT = "text";
    // 数据类型：picture 图片；video 视频； text 文本
    private String contentType;
    // picture或video类型时为url地址；text类型时为文本内容
    private String content;
    // 视频类型使用：视频封面缩略图
    private String videoThumbnail;
    // 视频及图片类型使用：视频宽度
    private int width;
    // 视频及图片类型使用：视频高度
    private int height;

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
/////////////////////////////////////////////////////////////////
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

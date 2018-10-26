package com.suixue.edu.college.config;

/**
 * Author: cuiyan
 * Date:   18/8/26 10:33
 * Desc:
 */
public class Constants {
    public static final int REQUEST_CODE_CONVERT_GIF = 10086;
    public static final int REQUEST_CODE_SELECT_INTEREST = 10087;
    public static final int REQUEST_CODE_REGISTER_FROM_MSG = 10088;
    public static final int REQUEST_CODE_REGISTER_FROM_ME = 10089;

    // 访客出生年份
    public static final String KEY_VISITOR_YEAR_OF_BIRTH = "visitorYearOfBirth";
    // 访客兴趣爱好
    public static final String KEY_VISITOR_INTEREST = "visitorInterest";

    public static final String KEY_USER_INFO = "userInfo";
    public static final String KEY_REGISTER_MODE = "registerMode";
    public static final String KEY_BLOGGER_ID = "bloggerId";
    public static final String KEY_BLOG_TYPE = "blogType";
    // 博主个人原创或转发
    public static final String BLOG_TYPE_SELF_OR_TRANSFERRED = "1";
    // 博主喜欢(点赞)
    public static final String BLOG_TYPE_PRAISED = "2";
    ////////////////////////////////////////////////////////////////////////////////

    // 游客模式
    public static final String VALUE_REGISTER_MODE_VISITOR = "1";
    // 用户模式
    public static final String VALUE_REGISTER_MODE_USER = "2";

    /////////////////////////////////////////////////////////////////////////////////
    public static final String INTEREST_ID_SEPARATOR = "#";
}

package com.suixue.edu.college.config;

import com.dev.kit.basemodule.netRequest.Configs.ApiConstants;
import com.dev.kit.basemodule.result.BaseResult;
import com.suixue.edu.college.entity.BaseListResult;
import com.suixue.edu.college.entity.BlogInfo;
import com.suixue.edu.college.entity.BloggerInfo;
import com.suixue.edu.college.entity.ChatMessageInfo;
import com.suixue.edu.college.entity.ChatSessionInfo;
import com.suixue.edu.college.entity.BaseCourseInfo;
import com.suixue.edu.college.entity.CourseInfo;
import com.suixue.edu.college.entity.InterestInfo;
import com.suixue.edu.college.entity.InterestResult;
import com.suixue.edu.college.entity.UserInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by cuiyan on 2018/9/4.
 */
public interface ApiService {

    // 获取短信验证码
    @FormUrlEncoded
    @POST(ApiConstants.GET_SECURITY_CODE_API)
    Observable<BaseResult> getSecurityCode(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST(ApiConstants.REGISTER_API)
    Observable<BaseResult<UserInfo>> register(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstants.LOGIN_API)
    Observable<BaseResult<UserInfo>> login(@FieldMap Map<String, String> params);

    /**
     * 获取全部兴趣列表
     *
     * @param params 传递空map即可
     */
    @FormUrlEncoded
    @POST(ApiConstants.GET_ALL_INTEREST_API)
    Observable<BaseResult<InterestResult>> getInterestList(@FieldMap Map<String, String> params);

    /**
     * 获取用户已选兴趣列表
     *
     * @param params 传递空map即可
     */
    @FormUrlEncoded
    @POST(ApiConstants.GET_USER_SELECTED_INTEREST_API)
    Observable<BaseResult<List<InterestInfo>>> getUserInterestList(@FieldMap Map<String, String> params);

    // 发送用户兴趣列表至服务器
    @FormUrlEncoded
    @POST(ApiConstants.SAVE_USER_INTEREST_API)
    Observable<BaseResult> sendUserInterestsToServer(@Field("interestIds") String interestIds);

    // 获取博客列表
    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOG_LIST_API)
    Observable<BaseResult<BaseListResult<Object>>> getBlogList(@Field("pageIndex") int pageIndex);

    // 搜索博客列表
    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOG_LIST_BY_SEARCH_API)
    Observable<BaseResult<BaseListResult<Object>>> searchBlogList(@Field("keyWord") String keyWord, @Field("pageIndex") int pageIndex);

    // 关注或取消关注博主
    @FormUrlEncoded
    @POST(ApiConstants.CONCERN_BLOGGER_API)
    Observable<BaseResult<String>> concernBlogger(@Field("bloggerId") String bloggerId, @Field("concernFlag") String concernFlag);

    // 赞或取消赞博客
    @FormUrlEncoded
    @POST(ApiConstants.PRAISE_BLOG_API)
    Observable<BaseResult<String>> praiseBlog(@Field("blogId") String blogId, @Field("praiseFlag") String praiseFlag);

    // 消息session列表
    @FormUrlEncoded
    @POST(ApiConstants.GET_CHAT_SESSION_LIST_API)
    Observable<BaseResult<List<ChatSessionInfo>>> getSessionList();

    // 消息列表
    @FormUrlEncoded
    @POST(ApiConstants.GET_CHAT_MSG_LIST_API)
    Observable<BaseResult<BaseListResult<ChatMessageInfo>>> getMsgList(@Field("sessionId") String sessionId, @Field("pageIndex") int pageIndex);

    // 获取博主信息
    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOGGER_INFO_API)
    Observable<BaseResult<BloggerInfo>> getBloggerInfo(@Field("bloggerId") String bloggerId);

    // 获取博主课程基础信息(学年、课程名称等)
    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOGGER_COURSE_BASE_INFO_API)
    Observable<BaseResult<List<BaseCourseInfo>>> getBloggerBaseCourseInfo(@Field("bloggerId") String bloggerId);

    // 获取课程列表
    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOGGER_COURSE_LIST_API)
    Observable<BaseResult<BaseListResult<CourseInfo>>> getBloggerCourseList(@Field("pageIndex") int pageIndex, @Field("bloggerId") String bloggerId);

    /**
     * 获取博主个人博客列表
     *
     * @param pageIndex 分页
     * @param blogType  博客类型：1 ：博主原创或转发；2：喜欢(点赞)的博客
     */
    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOGGER_BLOG_LIST_API)
    Observable<BaseResult<BaseListResult<BlogInfo>>> getBloggerBlogList(@Field("pageIndex") int pageIndex, @Field("blogType") String blogType, @Field("bloggerId") String bloggerId);
}

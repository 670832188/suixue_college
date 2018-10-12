package com.suixue.edu.college.config;

import com.dev.kit.basemodule.result.BaseResult;
import com.suixue.edu.college.entity.BaseListResult;
import com.suixue.edu.college.entity.ChatMessageInfo;
import com.suixue.edu.college.entity.ChatSessionInfo;
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
     * @param params 传递空map即可
     */
    @FormUrlEncoded
    @POST(ApiConstants.LOGIN_API)
    Observable<BaseResult<InterestResult>> getInterestList(@FieldMap Map<String, String> params);

    /**
     * 获取用户已选兴趣列表
     * @param params 传递空map即可
     */
    @FormUrlEncoded
    @POST(ApiConstants.LOGIN_API)
    Observable<BaseResult<List<InterestInfo>>> getUserInterestList(@FieldMap Map<String, String> params);

    // 发送用户兴趣列表至服务器
    @FormUrlEncoded
    @POST(ApiConstants.LOGIN_API)
    Observable<BaseResult> sendUserInterestsToServer(@Field("interestIds") String interestIds);

    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOG_LIST_API)
    Observable<BaseResult<BaseListResult<Object>>> getBlogList(@Field("pageIndex") String pageIndex);

    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOG_LIST_BY_SEARCH_API)
    Observable<BaseResult<BaseListResult<Object>>> searchBlogList(@Field("keyWord") String keyWord, @Field("pageIndex") int pageIndex);

    // 关注或取消关注博主
    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOG_LIST_BY_SEARCH_API)
    Observable<BaseResult<String>> concernBlogger(@Field("bloggerId") String bloggerId, @Field("concernFlag") String concernFlag);

    // 赞或取消赞博客
    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOG_LIST_BY_SEARCH_API)
    Observable<BaseResult<String>> praiseBlog(@Field("blogId") String blogId, @Field("praiseFlag") String praiseFlag);

    // 消息session列表
    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOG_LIST_BY_SEARCH_API)
    Observable<BaseResult<List<ChatSessionInfo>>> getSessionList();

    // 消息列表
    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOG_LIST_BY_SEARCH_API)
    Observable<BaseResult<BaseListResult<ChatMessageInfo>>> getMsgList(@Field("sessionId") String sessionId, @Field("pageIndex") int pageIndex);
}

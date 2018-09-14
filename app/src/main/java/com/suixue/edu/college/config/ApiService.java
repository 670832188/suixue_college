package com.suixue.edu.college.config;

import com.dev.kit.basemodule.result.BaseResult;
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

    @FormUrlEncoded
    @POST(ApiConstants.GET_SECURITY_CODE_API)
    Observable<BaseResult<UserInfo>> getSecurityCode(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST(ApiConstants.REGISTER_API)
    Observable<BaseResult<UserInfo>> register(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstants.LOGIN_API)
    Observable<BaseResult<UserInfo>> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOG_LIST_API)
    Observable<BaseResult<List<Object>>> getBlogList(@Field("pageIndex") String pageIndex);

    @FormUrlEncoded
    @POST(ApiConstants.GET_BLOG_LIST_BY_SEARCH_API)
    Observable<BaseResult<List<Object>>> getBlogListBySearch(@Field("pageIndex") String pageIndex, @Field("keyWord") String keyWord);
}

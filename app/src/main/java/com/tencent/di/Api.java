package com.tencent.di;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface Api {

    @POST
    Observable<ResponseBody> doPostMap(@Url String url, @Body Map<String, String> map);

    @POST
    Observable<ResponseBody> doPostsMap(@Url String url, @Body Map<String, Object> map);


    @POST
    Observable<ResponseBody> doPostsMaps(@Url String url, @Body Map<String, Object> map);

    @POST
    Observable<ResponseBody> doPostsScan(@Url String url, @Body Map<String, Object> map);


}

package com.tencent.http;

import android.os.Environment;
import android.util.Log;

import com.tencent.activity.LoginActivity;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrifitUtils {
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    private static RetrifitUtils utils;
    private String userId;
    private String sessionId;

    private RetrifitUtils(){

        //2、添加Http日志拦截
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLog());
        //3、设置日志拦截等级
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //4、sd卡缓存路径 名称

        File file=new File(Environment.getExternalStorageDirectory(),"interceptor");
        okHttpClient = new OkHttpClient.Builder()
                //6、日志拦截
                .addInterceptor(interceptor)
                //5、设置缓存文件和大小
                .cache(new Cache(file, 1024*10))
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request build = chain.request().newBuilder()
                                .addHeader("userId", userId)
                                .addHeader("sessionId",sessionId)
                                .build();
                        Log.e("intercept: ",build.url()+"" );
                        Response proceed = chain.proceed(build);
                        return proceed;
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Url.WAI_URL)
                .client(okHttpClient)
                .build();
    }

    public static RetrifitUtils getInstance(){
        if (utils==null){
            utils = new RetrifitUtils();
        }
        return utils;
    }


    //1、新建日志拦截
    public class HttpLog implements HttpLoggingInterceptor.Logger{
        @Override
        public void log(String message) {
            Log.e("log: ",message );
        }
    }
    public <T>T create(Class<T> classes){
        userId = LoginActivity.sp.getString("userId", "");
        sessionId = LoginActivity.sp.getString("sessionId", "");
        Log.e("create: ",userId+"--"+sessionId);
        return retrofit.create(classes);
    }

}

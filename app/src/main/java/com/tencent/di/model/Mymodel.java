package com.tencent.di.model;

import android.util.Log;

import com.google.gson.Gson;
import com.tencent.di.Api;
import com.tencent.http.RetrifitUtils;


import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class Mymodel {
//    MyCallback myCallback;

    public void postRequest(String url, Map<String, String> map, final Class aClass,final MyCallback myCallback) {

        RetrifitUtils.getInstance()
                .create(Api.class)
                .doPostMap(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = responseBody.string();
                            Gson gson = new Gson();
                            Object suc = gson.fromJson(string, aClass);
                            myCallback.Success(suc);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void postRequests(String url, Map<String, Object> map, final Class aClass,final MyCallback myCallback) {

        RetrifitUtils.getInstance()
                .create(Api.class)
                .doPostsMap(url, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String string = responseBody.string();
                            Log.i("egavxa", string);
                            Gson gson = new Gson();
                            Object suc = gson.fromJson(string, aClass);
                            myCallback.Success(suc);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
        public void postRequestss(String url, Map<String, Object> map, final Class aClass,final MyCallback myCallback) {

            RetrifitUtils.getInstance()
                    .create(Api.class)
                    .doPostsMaps(url, map)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<ResponseBody>() {
                        @Override
                        public void onNext(ResponseBody responseBody) {
                            try {
                                String string = responseBody.string();
                                Log.i("hahhahaha", string);
                                Gson gson = new Gson();
                                Object suc = gson.fromJson(string, aClass);
                                myCallback.Success(suc);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                        }
                    });
        }
            public void postRequestScan(String url, Map<String, Object> map, final Class aClass,final MyCallback myCallback) {

                RetrifitUtils.getInstance()
                        .create(Api.class)
                        .doPostsMaps(url, map)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableObserver<ResponseBody>() {
                            @Override
                            public void onNext(ResponseBody responseBody) {
                                try {
                                    String string = responseBody.string();
                                    Log.i("ScanPay",string);
                                    Gson gson = new Gson();
                                    Object suc = gson.fromJson(string, aClass);
                                    myCallback.Success(suc);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                            }
                        });


            }

    public interface MyCallback{
        public void Success(Object o);
    }
}


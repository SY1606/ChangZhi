package com.tencent.di.presenter;

import java.util.Map;

/**
 * @ProjectName: movie
 * @ClassName: BaseInterface
 * @Description: java类作用描述
 * @Author: 刘继超
 * @CreateDate: 2019/5/13 14:31:44
 */
public class BaseInterface {
    public interface PInterface{
        public void Login(String url, Map<String, String> map, Class aClass);
        public void Autoinfo(String url, Map<String,Object> map,Class aClass);

        public void Pay(String url, Map<String,Object> map,Class aClass);
        public void ScanPay(String url, Map<String,Object> map,Class aClass);
        public void OnDestory();
    }



    public interface LoginInterface{
        public  void Success(Object o);
    }
    public interface AutoinfoInterface{
        public  void Success(Object o);
    }

    public interface PayInterface{
        public  void SuccessFul(Object o);
    }
    public interface ScanInterface{
        public  void ScanSuccess(Object o);
        void showPayLoading();
       void dismissPayLoading();
    }
}

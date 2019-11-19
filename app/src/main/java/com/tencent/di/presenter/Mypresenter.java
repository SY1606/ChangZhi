package com.tencent.di.presenter;

import com.tencent.di.model.Mymodel;

import java.util.Map;

public class Mypresenter<T>  implements BaseInterface.PInterface {
    T tt;
    Mymodel mymodel;
    public Mypresenter(T tt){
        this.tt=tt;
        mymodel=new Mymodel();
    }
    @Override
    public void Login(String url, Map<String,String> map, Class aClass) {
        mymodel.postRequest(url,map,aClass,new Mymodel.MyCallback(){
            @Override
            public void Success(Object o) {
                ((BaseInterface.LoginInterface)tt).Success(o);
            }
        });
    }

    @Override
    public void Autoinfo(String url, Map<String, Object> map, Class aClass) {
        mymodel.postRequests(url, map, aClass, new Mymodel.MyCallback() {
            @Override
            public void Success(Object o) {
                ((BaseInterface.AutoinfoInterface)tt).Success(o);
            }
        });
    }

    @Override
    public void Pay(String url, Map<String, Object> map, Class aClass) {
        mymodel.postRequestss(url, map, aClass, new Mymodel.MyCallback() {
            @Override
            public void Success(Object o) {
                ((BaseInterface.PayInterface)tt).SuccessFul(o);
            }
        });
    }

    @Override
    public void ScanPay(String url, Map<String, Object> map, Class aClass) {
        ((BaseInterface.ScanInterface)tt).showPayLoading();
        mymodel.postRequestScan(url, map, aClass, new Mymodel.MyCallback() {
            @Override
            public void Success(Object o) {
                ((BaseInterface.ScanInterface)tt).ScanSuccess(o);
                ((BaseInterface.ScanInterface)tt).dismissPayLoading();
            }
        });
    }
    @Override
    public void OnDestory() {
        BaseInterface.PInterface pInterface=null;
    }
}

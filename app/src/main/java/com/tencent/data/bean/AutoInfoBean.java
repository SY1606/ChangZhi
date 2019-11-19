package com.tencent.data.bean;

import java.util.List;

public class AutoInfoBean {
    /**
     * "appid": "1308791035",
     * 	"method": "wx_scan",
     * 	"data": {
     * 		"total": 100,
     * 		"store_id": "11",
     * 		"nonce_str": "5ad1d0db143a2"
     *        },
     * 	"sign": "1802BF462E9DCD422BCF42D181068655"
     */

    private String appid;
    private String method;
    private String sign;
    private List<Datas> data;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public List<Datas> getData() {
        return data;
    }

    public void setData(List<Datas> data) {
        this.data = data;
    }

    public class Datas{
        /**
         * store_id
         * rawdata
         * nonce_str
         */

        private int store_id;
        private String rawdata;
        private String nonce_str;

        public int getStore_id() {
            return store_id;
        }

        public void setStore_id(int store_id) {
            this.store_id = store_id;
        }

        public String getRawdata() {
            return rawdata;
        }

        public void setRawdata(String rawdata) {
            this.rawdata = rawdata;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }
    }
}

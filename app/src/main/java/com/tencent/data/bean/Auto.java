package com.tencent.data.bean;

public class Auto {

    /**
     * code : 100
     * msg : 成功
     * data : {"appid":"wx42bc02eb7a18a5e4","mch_id":"1536575351","sub_mch_id":"1552384761","store_id":"4290","store_name":"同心烟酒店","device_id":"D5906781603","auth_info":"O3ofy4PN8/KGiCawupR/xtPKlCSWYN+VZWQdugjDZKmRaqx8cypVLvj+VX9eqgX4xeWoFWFkO7Pu6XFnicGEZn1wJ1Y3n+kgdyDXsjuAppcBZdAEgcUfPP0ilyrG8MjAxPV9nvxirFmGQ9oWAYw8qXdwmFKQYtx0YDa688TnOAlwOyq7xjmNzSRfMMSpUBaVt0VM2AsjG7DaLIZ1nBrRblEemIDGGBKy97P+a96PQX0Bqlw+1UykRb/td/bq2yLf0hoIBfeNrXm168rkaTI7bxn8uu3nuTYnPtSIUAYo4MPVZbFP0zHmKfGhDZmLY/+j6GpcbWRXwQvkIQWFhQd1fqj2Swe8WOAeHDw989L2mf7lzu4/g79Uh4TQeuQOEBkc/hBBy3pO9iXiJdZz8JWenE6d1XoyClbvsHMrxT32J/jck2fBcVX9r3MnkHr1QFXf7FqvFpdJh+OIJwBV3USJ0nYzmjLJOGOUD/+0vNHgaaVkE04w3H1BbXb+06I1bJNEn12sv2MBlpZzRo6kpQgxuEoo60DKlC78Q08RGkkXz3SGN6wvwlByZK9GmZ4uuvpF/QeYpimK3rdIiD6yQJSOresmACvmF0ksX+2gNt0QA6jb+q3GamuT0fX/JA+nfXKjTkp0PxTp9oI6YQ==","out_trade_no":"4290281517361572247056076946218","nonce_str":"Face5db6961012cf0"}
     * sign : 40C96A0CD5B0FC61F8FFC5EC9D974221
     */

    private int code;
    private String msg;
    private DataBean data;
    private String sign;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static class DataBean {
        /**
         * appid : wx42bc02eb7a18a5e4
         * mch_id : 1536575351
         * sub_mch_id : 1552384761
         * store_id : 4290
         * store_name : 同心烟酒店
         * device_id : D5906781603
         * auth_info : O3ofy4PN8/KGiCawupR/xtPKlCSWYN+VZWQdugjDZKmRaqx8cypVLvj+VX9eqgX4xeWoFWFkO7Pu6XFnicGEZn1wJ1Y3n+kgdyDXsjuAppcBZdAEgcUfPP0ilyrG8MjAxPV9nvxirFmGQ9oWAYw8qXdwmFKQYtx0YDa688TnOAlwOyq7xjmNzSRfMMSpUBaVt0VM2AsjG7DaLIZ1nBrRblEemIDGGBKy97P+a96PQX0Bqlw+1UykRb/td/bq2yLf0hoIBfeNrXm168rkaTI7bxn8uu3nuTYnPtSIUAYo4MPVZbFP0zHmKfGhDZmLY/+j6GpcbWRXwQvkIQWFhQd1fqj2Swe8WOAeHDw989L2mf7lzu4/g79Uh4TQeuQOEBkc/hBBy3pO9iXiJdZz8JWenE6d1XoyClbvsHMrxT32J/jck2fBcVX9r3MnkHr1QFXf7FqvFpdJh+OIJwBV3USJ0nYzmjLJOGOUD/+0vNHgaaVkE04w3H1BbXb+06I1bJNEn12sv2MBlpZzRo6kpQgxuEoo60DKlC78Q08RGkkXz3SGN6wvwlByZK9GmZ4uuvpF/QeYpimK3rdIiD6yQJSOresmACvmF0ksX+2gNt0QA6jb+q3GamuT0fX/JA+nfXKjTkp0PxTp9oI6YQ==
         * out_trade_no : 4290281517361572247056076946218
         * nonce_str : Face5db6961012cf0
         */

        private String appid;
        private String mch_id;
        private String sub_mch_id;
        private String store_id;
        private String store_name;
        private String device_id;
        private String auth_info;
        private String out_trade_no;
        private String nonce_str;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getMch_id() {
            return mch_id;
        }

        public void setMch_id(String mch_id) {
            this.mch_id = mch_id;
        }

        public String getSub_mch_id() {
            return sub_mch_id;
        }

        public void setSub_mch_id(String sub_mch_id) {
            this.sub_mch_id = sub_mch_id;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getAuth_info() {
            return auth_info;
        }

        public void setAuth_info(String auth_info) {
            this.auth_info = auth_info;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }
    }
}

package com.tencent.data.bean;

public class LoginBean {

    /**
     * code : 100
     * msg : 登录成功
     * data : {"uid":4147,"sid":4290,"mch_name":"同心烟酒店","reg_time":"2019-09-06 14:11:13","appid":"5906781603","key":"WBDL08SNEECQ8N3MTGXBTK","store_name":"张旭","store_tel":"13582010041","api_time":"2019-10-14 21:03:09"}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public static class DataBean {
        /**
         * uid : 4147
         * sid : 4290
         * mch_name : 同心烟酒店
         * reg_time : 2019-09-06 14:11:13
         * appid : 5906781603
         * key : WBDL08SNEECQ8N3MTGXBTK
         * store_name : 张旭
         * store_tel : 13582010041
         * api_time : 2019-10-14 21:03:09
         */

        private int uid;
        private int sid;
        private String mch_name;
        private String reg_time;
        private String appid;
        private String key;
        private String store_name;
        private String store_tel;
        private String api_time;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getMch_name() {
            return mch_name;
        }

        public void setMch_name(String mch_name) {
            this.mch_name = mch_name;
        }

        public String getReg_time() {
            return reg_time;
        }

        public void setReg_time(String reg_time) {
            this.reg_time = reg_time;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getStore_name() {
            return store_name;
        }

        public void setStore_name(String store_name) {
            this.store_name = store_name;
        }

        public String getStore_tel() {
            return store_tel;
        }

        public void setStore_tel(String store_tel) {
            this.store_tel = store_tel;
        }

        public String getApi_time() {
            return api_time;
        }

        public void setApi_time(String api_time) {
            this.api_time = api_time;
        }
    }
}

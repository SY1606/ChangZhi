package com.tencent.data.bean;

public class PayBean {

    /**
     * code : 100
     * msg : 成功
     * data : {"appid":"wx42bc02eb7a18a5e4","mch_id":"1536575351","sub_mch_id":"1544679911","total_fee":"1","transaction_id":"4200000416201910304041636450","out_trade_no":"4230301829201572431360469357864","time_end":"20191030183256","status":1,"nonce_str":"Face5db966d98cc52"}
     * sign : 19E978AE44FA9FEFA767400AA35FC579
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
         * sub_mch_id : 1544679911
         * total_fee : 1
         * transaction_id : 4200000416201910304041636450
         * out_trade_no : 4230301829201572431360469357864
         * time_end : 20191030183256
         * status : 1
         * nonce_str : Face5db966d98cc52
         */

        private String appid;
        private String mch_id;
        private String sub_mch_id;
        private int total_fee;
        private String transaction_id;
        private String out_trade_no;
        private String time_end;
        private int status;
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

        public int getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(int total_fee) {
            this.total_fee = total_fee;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public String getTime_end() {
            return time_end;
        }

        public void setTime_end(String time_end) {
            this.time_end = time_end;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }
    }
}

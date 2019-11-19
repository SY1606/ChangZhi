package com.tencent.data.bean;

public class ScanPay {


    /**
     * code : 100
     * msg : null
     * data : {"out_trade_no":"42301572588448422884869","status":3,"payType":"wx","payTime":"2019-11-01 14:07:39","total":"1","amount":0,"msg":"","nonce_str":"Face5dbbcbabd3471"}
     * sign : 135507AEEABEFC3FBDF74F1733AC3503
     */

    private int code;
    private Object msg;
    private DataBean data;
    private String sign;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
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
         * out_trade_no : 42301572588448422884869
         * status : 3
         * payType : wx
         * payTime : 2019-11-01 14:07:39
         * total : 1
         * amount : 0
         * msg :
         * nonce_str : Face5dbbcbabd3471
         */

        private String out_trade_no;
        private int status;
        private String payType;
        private String payTime;
        private int total;
        private int amount;
        private String msg;
        private String nonce_str;

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }
    }
}

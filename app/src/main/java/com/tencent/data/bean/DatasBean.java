package com.tencent.data.bean;

public class DatasBean {
    private String nonce_str;
    private String rawdata;
    private String store_id;

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getRawdata() {
        return rawdata;
    }

    public void setRawdata(String rawdata) {
        this.rawdata = rawdata;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "nonce_str='" + nonce_str + '\'' +
                ", rawdata='" + rawdata + '\'' +
                ", store_id='" + store_id + '\'' +
                '}';
    }
}

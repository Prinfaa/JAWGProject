package com.jinganweigu.RoadWayFire.Entries;

public class EZUIkitLogin {


    /**
     * data : {"accessToken":"at.ajtxp9fx0oihbzwlbdnt6vh6dqudsw4k-4qos3tz393-0xn7dxc-qmpb5shfc","expireTime":1530605855799}
     * code : 200
     * msg : 操作成功!
     */

    private DataBean data;
    private String code;
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * accessToken : at.ajtxp9fx0oihbzwlbdnt6vh6dqudsw4k-4qos3tz393-0xn7dxc-qmpb5shfc
         * expireTime : 1530605855799
         */

        private String accessToken;
        private long expireTime;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(long expireTime) {
            this.expireTime = expireTime;
        }
    }
}

package com.jinganweigu.entrysystem.entry;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class OutHouseBean {

    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"mitu_type":"水压水位监测"}
     */

    private int code;
    private String msg;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * mitu_type : 水压水位监测
         */

        private String mitu_type;

        public String getMitu_type() {
            return mitu_type;
        }

        public void setMitu_type(String mitu_type) {
            this.mitu_type = mitu_type;
        }
    }
}

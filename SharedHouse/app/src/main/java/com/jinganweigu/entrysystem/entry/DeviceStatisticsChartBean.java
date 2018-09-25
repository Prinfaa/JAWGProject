package com.jinganweigu.entrysystem.entry;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class DeviceStatisticsChartBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"mitu_type":"一键报警","total":"0"},{"mitu_type":"气体探测","total":"0"},{"mitu_type":"水压水位监测","total":"17"},{"mitu_type":"火焰探测","total":"0"},{"mitu_type":"车载报警","total":"0"}]
     */

    private int code;
    private String msg;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * mitu_type : 一键报警
         * total : 0
         */

        private String mitu_type;
        private String total;

        public String getMitu_type() {
            return mitu_type;
        }

        public void setMitu_type(String mitu_type) {
            this.mitu_type = mitu_type;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}

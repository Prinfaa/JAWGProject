package com.example.dell_pc.unitpad.item;

import java.util.List;

public class BankLocationInformation {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"company_id":"337","com_name":"北京银行(大明湖支行)","com_lati":"36.68511706854481","com_long":"117.0471777598549"},{"company_id":"338","com_name":"北京银行(高新区支行)","com_lati":"36.67713604830755","com_long":"117.13755627721379"}]
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
         * company_id : 337
         * com_name : 北京银行(大明湖支行)
         * com_lati : 36.68511706854481
         * com_long : 117.0471777598549
         */

        private String company_id;
        private String com_name;
        private String com_lati;
        private String com_long;

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getCom_name() {
            return com_name;
        }

        public void setCom_name(String com_name) {
            this.com_name = com_name;
        }

        public String getCom_lati() {
            return com_lati;
        }

        public void setCom_lati(String com_lati) {
            this.com_lati = com_lati;
        }

        public String getCom_long() {
            return com_long;
        }

        public void setCom_long(String com_long) {
            this.com_long = com_long;
        }
    }
}

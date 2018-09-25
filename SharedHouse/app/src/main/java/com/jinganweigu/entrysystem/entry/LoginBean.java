package com.jinganweigu.entrysystem.entry;

/**
 * Created by Administrator on 2018/3/7 0007.
 */

public class LoginBean {

    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"warehouse_name":"经安纬固仓库","company_name":"山东经安纬固消防科技有限公司","real_name":"测试1","max_row":"10","colour_in":"#5F97F1","colour_out":"#5F97F1","colour_return":"#5F97F1"}
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
         * warehouse_name : 经安纬固仓库
         * company_name : 山东经安纬固消防科技有限公司
         * real_name : 测试1
         * max_row : 10
         * colour_in : #5F97F1
         * colour_out : #5F97F1
         * colour_return : #5F97F1
         */

        private String warehouse_name;
        private String company_name;
        private String real_name;
        private String max_row;
        private String colour_in;
        private String colour_out;
        private String colour_return;

        public String getWarehouse_name() {
            return warehouse_name;
        }

        public void setWarehouse_name(String warehouse_name) {
            this.warehouse_name = warehouse_name;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public String getMax_row() {
            return max_row;
        }

        public void setMax_row(String max_row) {
            this.max_row = max_row;
        }

        public String getColour_in() {
            return colour_in;
        }

        public void setColour_in(String colour_in) {
            this.colour_in = colour_in;
        }

        public String getColour_out() {
            return colour_out;
        }

        public void setColour_out(String colour_out) {
            this.colour_out = colour_out;
        }

        public String getColour_return() {
            return colour_return;
        }

        public void setColour_return(String colour_return) {
            this.colour_return = colour_return;
        }
    }
}

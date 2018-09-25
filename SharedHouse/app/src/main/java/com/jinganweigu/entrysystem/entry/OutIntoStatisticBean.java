package com.jinganweigu.entrysystem.entry;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22 0022.
 */

public class OutIntoStatisticBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"mitu_type":"一键报警","warehouse_in":"0","warehouse_out":"0","warehouse_return":"0"},{"mitu_type":"气体探测","warehouse_in":"0","warehouse_out":"0","warehouse_return":"0"},{"mitu_type":"水压水位监测","warehouse_in":"0","warehouse_out":"0","warehouse_return":"0"},{"mitu_type":"火焰探测","warehouse_in":"0","warehouse_out":"0","warehouse_return":"0"},{"mitu_type":"车载报警","warehouse_in":"0","warehouse_out":"0","warehouse_return":"0"}]
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
         * warehouse_in : 0
         * warehouse_out : 0
         * warehouse_return : 0
         */

        private String mitu_type;
        private String warehouse_in;
        private String warehouse_out;
        private String warehouse_return;

        public String getMitu_type() {
            return mitu_type;
        }

        public void setMitu_type(String mitu_type) {
            this.mitu_type = mitu_type;
        }

        public int getWarehouse_in() {
            return Integer.valueOf(warehouse_in);
        }

        public void setWarehouse_in(String warehouse_in) {
            this.warehouse_in = warehouse_in;
        }

        public int getWarehouse_out() {
            return Integer.valueOf(warehouse_out);
        }

        public void setWarehouse_out(String warehouse_out) {
            this.warehouse_out = warehouse_out;
        }

        public int getWarehouse_return() {

            return Integer.valueOf(warehouse_return);
        }

        public void setWarehouse_return(String warehouse_return) {
            this.warehouse_return = warehouse_return;
        }
    }
}

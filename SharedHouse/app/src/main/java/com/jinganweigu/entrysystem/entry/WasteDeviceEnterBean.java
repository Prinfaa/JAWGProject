package com.jinganweigu.entrysystem.entry;

public class WasteDeviceEnterBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"rtu_id":"000000000000001","rtu_type":"4","mitu_type":"水压水位监测","in_mitu_peo":"测试1","warehouse_id":"1","warehouse_name":"经安纬固仓库"}
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
         * rtu_id : 000000000000001
         * rtu_type : 4
         * mitu_type : 水压水位监测
         * in_mitu_peo : 测试1
         * warehouse_id : 1
         * warehouse_name : 经安纬固仓库
         */

        private String rtu_id;
        private String rtu_type;
        private String mitu_type;
        private String in_mitu_peo;
        private String warehouse_id;
        private String warehouse_name;

        public String getRtu_id() {
            return rtu_id;
        }

        public void setRtu_id(String rtu_id) {
            this.rtu_id = rtu_id;
        }

        public String getRtu_type() {
            return rtu_type;
        }

        public void setRtu_type(String rtu_type) {
            this.rtu_type = rtu_type;
        }

        public String getMitu_type() {
            return mitu_type;
        }

        public void setMitu_type(String mitu_type) {
            this.mitu_type = mitu_type;
        }

        public String getIn_mitu_peo() {
            return in_mitu_peo;
        }

        public void setIn_mitu_peo(String in_mitu_peo) {
            this.in_mitu_peo = in_mitu_peo;
        }

        public String getWarehouse_id() {
            return warehouse_id;
        }

        public void setWarehouse_id(String warehouse_id) {
            this.warehouse_id = warehouse_id;
        }

        public String getWarehouse_name() {
            return warehouse_name;
        }

        public void setWarehouse_name(String warehouse_name) {
            this.warehouse_name = warehouse_name;
        }
    }
}

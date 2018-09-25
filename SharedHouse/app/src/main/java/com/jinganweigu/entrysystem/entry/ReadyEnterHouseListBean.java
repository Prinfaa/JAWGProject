package com.jinganweigu.entrysystem.entry;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class ReadyEnterHouseListBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"chip_IMEI":"777778888899999","rtu_type":"水压水位监测","stat_id":"成品预入库"},{"chip_IMEI":"444445555566666","rtu_type":"水压水位监测","stat_id":"成品预入库"},{"chip_IMEI":"999998888877777","rtu_type":"水压水位监测","stat_id":"成品预入库"},{"chip_IMEI":"111112222244444","rtu_type":"水压水位监测","stat_id":"成品预入库"},{"chip_IMEI":"111112222233333","rtu_type":"水压水位监测","stat_id":"成品预入库"},{"chip_IMEI":"222222222222222","rtu_type":"水压水位监测","stat_id":"成品预入库"}]
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
         * chip_IMEI : 777778888899999
         * rtu_type : 水压水位监测
         * stat_id : 成品预入库
         */

        private String chip_IMEI;
        private String rtu_type;
        private String stat_id;
        private String return_num;

        public String getChip_IMEI() {
            return chip_IMEI;
        }

        public void setChip_IMEI(String chip_IMEI) {
            this.chip_IMEI = chip_IMEI;
        }

        public String getRtu_type() {
            return rtu_type;
        }

        public void setRtu_type(String rtu_type) {
            this.rtu_type = rtu_type;
        }

        public String getStat_id() {
            return stat_id;
        }

        public void setStat_id(String stat_id) {
            this.stat_id = stat_id;
        }

        public String getReturn_num() {
            return return_num;
        }

        public void setReturn_num(String return_num) {
            this.return_num = return_num;
        }
    }
}

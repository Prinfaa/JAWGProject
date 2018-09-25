package com.jinganweigu.entrysystem.entry;

import java.util.List;

public class WasteDeviceEnterListBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"chip_IMEI":"000000000000042","rtu_type":"气体探测","stat_id":"废品预入库"}]
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
         * chip_IMEI : 000000000000042
         * rtu_type : 气体探测
         * stat_id : 废品预入库
         */

        private String chip_IMEI;
        private String rtu_type;
        private String stat_id;

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
    }
}

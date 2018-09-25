package com.jinganweigu.entrysystem.entry;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class ProjectStatisticListBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"com_name":"K88名泉广场","alarm":"1","water":"4","gas":"0","flame":"1","vehicular_unit":"0","all":"6"},{"com_name":"山东经安纬固消防科技有限公司","alarm":"1","water":"2","gas":"0","flame":"1","vehicular_unit":"0","all":"4"},{"com_name":"柳行社区","alarm":"2","water":"0","gas":"0","flame":"0","vehicular_unit":"0","all":"2"},{"com_name":"济南居然之家北园店","alarm":"0","water":"5","gas":"0","flame":"0","vehicular_unit":"0","all":"5"},{"com_name":"黄台家居广场","alarm":"1","water":"2","gas":"0","flame":"0","vehicular_unit":"0","all":"3"},{"com_name":"黄台电子商务产业园","alarm":"1","water":"5","gas":"0","flame":"0","vehicular_unit":"0","all":"6"}]
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
         * com_name : K88名泉广场
         * alarm : 1
         * water : 4
         * gas : 0
         * flame : 1
         * vehicular_unit : 0
         * all : 6
         */

        private String com_name;
        private String alarm;
        private String water;
        private String gas;
        private String flame;
        private String vehicular_unit;
        private String all;

        public String getCom_name() {
            return com_name;
        }

        public void setCom_name(String com_name) {
            this.com_name = com_name;
        }

        public String getAlarm() {
            return alarm;
        }

        public void setAlarm(String alarm) {
            this.alarm = alarm;
        }

        public String getWater() {
            return water;
        }

        public void setWater(String water) {
            this.water = water;
        }

        public String getGas() {
            return gas;
        }

        public void setGas(String gas) {
            this.gas = gas;
        }

        public String getFlame() {
            return flame;
        }

        public void setFlame(String flame) {
            this.flame = flame;
        }

        public String getVehicular_unit() {
            return vehicular_unit;
        }

        public void setVehicular_unit(String vehicular_unit) {
            this.vehicular_unit = vehicular_unit;
        }

        public String getAll() {
            return all;
        }

        public void setAll(String all) {
            this.all = all;
        }
    }
}

package com.jinganweigu.entrysystem.entry;

import java.util.List;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class ProjectStaticPicBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"company":"6","alarm":"6","gas_sensing":"0","flame":"2","water":"18","vehicular_unit":"0"}]
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
         * company : 6
         * alarm : 6
         * gas_sensing : 0
         * flame : 2
         * water : 18
         * vehicular_unit : 0
         */

        private String company;
        private String alarm;
        private String gas_sensing;
        private String flame;
        private String water;
        private String vehicular_unit;

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getAlarm() {
            return alarm;
        }

        public void setAlarm(String alarm) {
            this.alarm = alarm;
        }

        public String getGas_sensing() {
            return gas_sensing;
        }

        public void setGas_sensing(String gas_sensing) {
            this.gas_sensing = gas_sensing;
        }

        public String getFlame() {
            return flame;
        }

        public void setFlame(String flame) {
            this.flame = flame;
        }

        public String getWater() {
            return water;
        }

        public void setWater(String water) {
            this.water = water;
        }

        public String getVehicular_unit() {
            return vehicular_unit;
        }

        public void setVehicular_unit(String vehicular_unit) {
            this.vehicular_unit = vehicular_unit;
        }
    }
}

package com.jinganweigu.entrysystem.entry;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20 0020.
 */

public class DeviceStatisticsBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"mitu_type":"一键报警","inventory":"0","arrsemi_finished":"1","uninstall":"1","arrinstall":"0","arrreturn":"0"},{"mitu_type":"气体探测","inventory":"0","arrsemi_finished":"0","uninstall":"0","arrinstall":"0","arrreturn":"2"},{"mitu_type":"水压水位监测","inventory":"17","arrsemi_finished":"38","uninstall":"28","arrinstall":"1","arrreturn":"4"},{"mitu_type":"火焰探测","inventory":"0","arrsemi_finished":"0","uninstall":"0","arrinstall":"0","arrreturn":"1"},{"mitu_type":"车载报警","inventory":"0","arrsemi_finished":"0","uninstall":"0","arrinstall":"0","arrreturn":"1"}]
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
         * inventory : 0
         * arrsemi_finished : 1
         * uninstall : 1
         * arrinstall : 0
         * arrreturn : 0
         */

        private String mitu_type;
        private String inventory;
        private String arrsemi_finished;
        private String uninstall;
        private String arrinstall;
        private String arrreturn;

        public String getMitu_type() {
            return mitu_type;
        }

        public void setMitu_type(String mitu_type) {
            this.mitu_type = mitu_type;
        }

        public String getInventory() {
            return inventory;
        }

        public void setInventory(String inventory) {
            this.inventory = inventory;
        }

        public String getArrsemi_finished() {
            return arrsemi_finished;
        }

        public void setArrsemi_finished(String arrsemi_finished) {
            this.arrsemi_finished = arrsemi_finished;
        }

        public String getUninstall() {
            return uninstall;
        }

        public void setUninstall(String uninstall) {
            this.uninstall = uninstall;
        }

        public String getArrinstall() {
            return arrinstall;
        }

        public void setArrinstall(String arrinstall) {
            this.arrinstall = arrinstall;
        }

        public String getArrreturn() {
            return arrreturn;
        }

        public void setArrreturn(String arrreturn) {
            this.arrreturn = arrreturn;
        }
    }
}

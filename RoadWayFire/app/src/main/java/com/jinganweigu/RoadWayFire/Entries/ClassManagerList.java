package com.jinganweigu.RoadWayFire.Entries;

import java.util.List;

public class ClassManagerList {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"classes_id":"1","classes_name":"早班","cla_starttime":"8:30","cla_stoptime":"20:30"},{"classes_id":"2","classes_name":"晚班","cla_starttime":"20:30","cla_stoptime":"8:30"},{"classes_id":"3","classes_name":"中班","cla_starttime":"12:00","cla_stoptime":"14:00"}]
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
         * classes_id : 1
         * classes_name : 早班
         * cla_starttime : 8:30
         * cla_stoptime : 20:30
         */

        private String classes_id;
        private String classes_name;
        private String cla_starttime;
        private String cla_stoptime;

        public String getClasses_id() {
            return classes_id;
        }

        public void setClasses_id(String classes_id) {
            this.classes_id = classes_id;
        }

        public String getClasses_name() {
            return classes_name;
        }

        public void setClasses_name(String classes_name) {
            this.classes_name = classes_name;
        }

        public String getCla_starttime() {
            return cla_starttime;
        }

        public void setCla_starttime(String cla_starttime) {
            this.cla_starttime = cla_starttime;
        }

        public String getCla_stoptime() {
            return cla_stoptime;
        }

        public void setCla_stoptime(String cla_stoptime) {
            this.cla_stoptime = cla_stoptime;
        }
    }
}

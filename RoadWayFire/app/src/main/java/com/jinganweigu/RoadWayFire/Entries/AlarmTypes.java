package com.jinganweigu.RoadWayFire.Entries;

import java.util.List;

public class AlarmTypes {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"type_id":"1","type":"电话联系值班人员"},{"type_id":"2","type":"实地查看"},{"type_id":"3","type":"推送他人处理"}]
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
         * type_id : 1
         * type : 电话联系值班人员
         */

        private String type_id;
        private String type;

        public String getType_id() {
            return type_id;
        }

        public void setType_id(String type_id) {
            this.type_id = type_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

package com.jinganweigu.RoadWayFire.Entries;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventModel {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"off_line":[{"long":"117.36843538756887","lati":"36.637866455944196","mitu_id":"866710032538644"}],"on_line":[{"long":"117.04936064224417","lati":"36.71537657376867","mitu_id":"867793033601420"}],"alarm":[{"long":"116.94590688149991","lati":"36.701651751399515","mitu_id":"111111111111111"}]}
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
        private List<OffLineBean> off_line;
        private List<OnLineBean> on_line;
        private List<AlarmBean> alarm;

        public List<OffLineBean> getOff_line() {
            return off_line;
        }

        public void setOff_line(List<OffLineBean> off_line) {
            this.off_line = off_line;
        }

        public List<OnLineBean> getOn_line() {
            return on_line;
        }

        public void setOn_line(List<OnLineBean> on_line) {
            this.on_line = on_line;
        }

        public List<AlarmBean> getAlarm() {
            return alarm;
        }

        public void setAlarm(List<AlarmBean> alarm) {
            this.alarm = alarm;
        }

        public static class OffLineBean {
            /**
             * long : 117.36843538756887
             * lati : 36.637866455944196
             * mitu_id : 866710032538644
             */

            @SerializedName("long")
            private String longX;
            private String lati;
            private String mitu_id;

            public String getLongX() {
                return longX;
            }

            public void setLongX(String longX) {
                this.longX = longX;
            }

            public String getLati() {
                return lati;
            }

            public void setLati(String lati) {
                this.lati = lati;
            }

            public String getMitu_id() {
                return mitu_id;
            }

            public void setMitu_id(String mitu_id) {
                this.mitu_id = mitu_id;
            }
        }

        public static class OnLineBean {
            /**
             * long : 117.04936064224417
             * lati : 36.71537657376867
             * mitu_id : 867793033601420
             */

            @SerializedName("long")
            private String longX;
            private String lati;
            private String mitu_id;

            public String getLongX() {
                return longX;
            }

            public void setLongX(String longX) {
                this.longX = longX;
            }

            public String getLati() {
                return lati;
            }

            public void setLati(String lati) {
                this.lati = lati;
            }

            public String getMitu_id() {
                return mitu_id;
            }

            public void setMitu_id(String mitu_id) {
                this.mitu_id = mitu_id;
            }
        }

        public static class AlarmBean {
            /**
             * long : 116.94590688149991
             * lati : 36.701651751399515
             * mitu_id : 111111111111111
             */

            @SerializedName("long")
            private String longX;
            private String lati;
            private String mitu_id;

            public String getLongX() {
                return longX;
            }

            public void setLongX(String longX) {
                this.longX = longX;
            }

            public String getLati() {
                return lati;
            }

            public void setLati(String lati) {
                this.lati = lati;
            }

            public String getMitu_id() {
                return mitu_id;
            }

            public void setMitu_id(String mitu_id) {
                this.mitu_id = mitu_id;
            }
        }
    }
}

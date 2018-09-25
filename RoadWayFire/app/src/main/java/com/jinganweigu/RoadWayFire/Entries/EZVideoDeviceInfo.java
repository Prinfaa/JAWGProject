package com.jinganweigu.RoadWayFire.Entries;

import java.util.List;

public class EZVideoDeviceInfo {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"lng":"117.04582131853485","lat":"36.687866455944196","mitu_id":"867793033454879","camera_name":"4号设备","position":"中控室","dev_number":"752866317","status":"离线"},{"lng":"117.04936064224417","lat":"36.71537657376867","mitu_id":"867793033601420","camera_name":"5号设备","position":"软件部","dev_number":"818226751","status":"正常"},{"lng":"116.94590688149991","lat":"36.701651751399515","mitu_id":"111111111111111","camera_name":"7号设备","position":"一楼大厅","dev_number":"818226750","status":"报警"}]
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
         * lng : 117.04582131853485
         * lat : 36.687866455944196
         * mitu_id : 867793033454879
         * camera_name : 4号设备
         * position : 中控室
         * dev_number : 752866317
         * status : 离线
         */

        private String lng;
        private String lat;
        private String mitu_id;
        private String camera_name;
        private String position;
        private String dev_number;
        private String status;

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getMitu_id() {
            return mitu_id;
        }

        public void setMitu_id(String mitu_id) {
            this.mitu_id = mitu_id;
        }

        public String getCamera_name() {
            return camera_name;
        }

        public void setCamera_name(String camera_name) {
            this.camera_name = camera_name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getDev_number() {
            return dev_number;
        }

        public void setDev_number(String dev_number) {
            this.dev_number = dev_number;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

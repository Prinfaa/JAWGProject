package com.jinganweigu.RoadWayFire.Entries;

import java.util.List;

public class SendMeMessageBean {

    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"warning_num":"1806JAWG010001","camera_name":"5号设备","position":"软件部","total_time":"5421","start_time":"1529798400","photo_adress":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/cover.jpg","status":"已处警","warning_type":"电话联系值班人员","remark":"车辆驶离"},{"warning_num":"1806JAWG010002","camera_name":"7号设备","position":"一楼大厅","total_time":"5421","start_time":"1529798300","photo_adress":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/1.jpg","status":"未处警","warning_type":"","remark":""}]
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
         * warning_num : 1806JAWG010001
         * camera_name : 5号设备
         * position : 软件部
         * total_time : 5421
         * start_time : 1529798400
         * photo_adress : http://photo.fis119.com/document/fire_inspection/jrzj/2017/cover.jpg
         * status : 已处警
         * warning_type : 电话联系值班人员
         * remark : 车辆驶离
         */

        private String warning_num;
        private String camera_name;
        private String position;
        private String total_time;
        private String start_time;
        private String photo_adress;
        private String status;
        private String warning_type;
        private String remark;

        public String getWarning_num() {
            return warning_num;
        }

        public void setWarning_num(String warning_num) {
            this.warning_num = warning_num;
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

        public String getTotal_time() {
            return total_time;
        }

        public void setTotal_time(String total_time) {
            this.total_time = total_time;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getPhoto_adress() {
            return photo_adress;
        }

        public void setPhoto_adress(String photo_adress) {
            this.photo_adress = photo_adress;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getWarning_type() {
            return warning_type;
        }

        public void setWarning_type(String warning_type) {
            this.warning_type = warning_type;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}

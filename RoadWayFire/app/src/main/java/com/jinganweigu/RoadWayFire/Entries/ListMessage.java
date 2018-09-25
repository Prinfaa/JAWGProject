package com.jinganweigu.RoadWayFire.Entries;

import java.io.Serializable;
import java.util.List;

public class ListMessage {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"type":"报警消息","name":"","camera_name":"5号设备","position":"软件部","start_time":"2018-08-04 15:21:45","comment":"","photo_adress":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/cover.jpg","total_time":"5421","dev_number":"818226751","send_timestamp":"2018-08-04 15:21:45","warning_num":"1806JAWG010001","deal_time":"2018-07-17 14:08:46","warning_type":"电话联系值班人员","remark":"车辆驶离","deal_peo":"玉虎"},{"type":"发出消息","name":"一块","camera_name":"7号设备","position":"一楼大厅","start_time":"2018-06-24 08:01:40","comment":"小迪","photo_adress":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/4.jpg","total_time":"5421","dev_number":"818226750","send_timestamp":"2018-08-04 14:38:33","warning_num":"1806JAWG010005","deal_time":"2018-08-04 15:05:06","warning_type":"电话联系值班人员","remark":"过去看看","deal_peo":"小迪兔"},{"type":"报警消息","name":"","camera_name":"7号设备","position":"一楼大厅","start_time":"2018-06-24 08:01:40","comment":"","photo_adress":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/4.jpg","total_time":"5421","dev_number":"818226750","send_timestamp":"2018-06-24 08:01:40","warning_num":"1806JAWG010005","deal_time":"2018-08-04 15:05:06","warning_type":"电话联系值班人员","remark":"过去看看","deal_peo":"小迪兔"}]
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

    public static class ResultBean implements Serializable{
        /**
         * type : 报警消息
         * name :
         * camera_name : 5号设备
         * position : 软件部
         * start_time : 2018-08-04 15:21:45
         * comment :
         * photo_adress : http://photo.fis119.com/document/fire_inspection/jrzj/2017/cover.jpg
         * total_time : 5421
         * dev_number : 818226751
         * send_timestamp : 2018-08-04 15:21:45
         * warning_num : 1806JAWG010001
         * deal_time : 2018-07-17 14:08:46
         * warning_type : 电话联系值班人员
         * remark : 车辆驶离
         * deal_peo : 玉虎
         */

        private String type;
        private String name;
        private String camera_name;
        private String position;
        private String start_time;
        private String comment;
        private String photo_adress;
        private String total_time;
        private String dev_number;
        private String send_timestamp;
        private String warning_num;
        private String deal_time;
        private String warning_type;
        private String remark;
        private String deal_peo;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getPhoto_adress() {
            return photo_adress;
        }

        public void setPhoto_adress(String photo_adress) {
            this.photo_adress = photo_adress;
        }

        public String getTotal_time() {
            return total_time;
        }

        public void setTotal_time(String total_time) {
            this.total_time = total_time;
        }

        public String getDev_number() {
            return dev_number;
        }

        public void setDev_number(String dev_number) {
            this.dev_number = dev_number;
        }

        public String getSend_timestamp() {
            return send_timestamp;
        }

        public void setSend_timestamp(String send_timestamp) {
            this.send_timestamp = send_timestamp;
        }

        public String getWarning_num() {
            return warning_num;
        }

        public void setWarning_num(String warning_num) {
            this.warning_num = warning_num;
        }

        public String getDeal_time() {
            return deal_time;
        }

        public void setDeal_time(String deal_time) {
            this.deal_time = deal_time;
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

        public String getDeal_peo() {
            return deal_peo;
        }

        public void setDeal_peo(String deal_peo) {
            this.deal_peo = deal_peo;
        }
    }
}

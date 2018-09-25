package com.jinganweigu.RoadWayFire.Entries;

import java.util.List;

public class OneMonthAllPicture {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"warning_num":"1806JAWG010001","photo_adress":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/cover.jpg","timestamp":"1529798420"},{"warning_num":"1806JAWG010002","photo_adress":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/5.jpg","timestamp":"1531467678"},{"warning_num":"1806JAWG010003","photo_adress":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/2.jpg","timestamp":"1529798445"},{"warning_num":"1806JAWG010005","photo_adress":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/4.jpg","timestamp":"1531467678"}]
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
         * photo_adress : http://photo.fis119.com/document/fire_inspection/jrzj/2017/cover.jpg
         * timestamp : 1529798420
         */

        private String warning_num;
        private String photo_adress;
        private String timestamp;

        public String getWarning_num() {
            return warning_num;
        }

        public void setWarning_num(String warning_num) {
            this.warning_num = warning_num;
        }

        public String getPhoto_adress() {
            return photo_adress;
        }

        public void setPhoto_adress(String photo_adress) {
            this.photo_adress = photo_adress;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
    }
}

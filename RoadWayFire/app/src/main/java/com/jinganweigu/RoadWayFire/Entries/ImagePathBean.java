package com.jinganweigu.RoadWayFire.Entries;

import java.util.List;

public class ImagePathBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"photo_adress":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/cover.jpg"},{"photo_adress":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/1.jpg"},{"photo_adress":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/3.jpg"}]
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
         * photo_adress : http://photo.fis119.com/document/fire_inspection/jrzj/2017/cover.jpg
         */

        private String photo_adress;

        public String getPhoto_adress() {
            return photo_adress;
        }

        public void setPhoto_adress(String photo_adress) {
            this.photo_adress = photo_adress;
        }
    }
}

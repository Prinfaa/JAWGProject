package com.jinganweigu.RoadWayFire.Entries;

public class PersonInformatication {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"name":"张三","phone":"11044444444","cla_starttime":"8:30","cla_stoptime":"20:30","user_head":"http://photo.fis119.com/document/fire_inspection/jrzj/2017/cover.jpg"}
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
        /**
         * name : 张三
         * phone : 11044444444
         * cla_starttime : 8:30
         * cla_stoptime : 20:30
         * user_head : http://photo.fis119.com/document/fire_inspection/jrzj/2017/cover.jpg
         */

        private String name;
        private String phone;
        private String cla_starttime;
        private String cla_stoptime;
        private String user_head;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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

        public String getUser_head() {
            return user_head;
        }

        public void setUser_head(String user_head) {
            this.user_head = user_head;
        }
    }
}

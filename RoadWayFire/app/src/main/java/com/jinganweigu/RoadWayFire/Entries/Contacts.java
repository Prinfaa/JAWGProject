package com.jinganweigu.RoadWayFire.Entries;

import java.util.List;

public class Contacts {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"user_id":"3","name":"小伙伴222","phone":"16666666666","cid":"jawg","sex":"男","user_type":"区域负责人"},{"user_id":"11","name":"Tom ","phone":"13333333333","cid":"12222222222","sex":"男","user_type":"值班人员"},{"user_id":"20","name":"ktjt","phone":"22222222223","cid":"22222222223","sex":"男","user_type":"值班人员"},{"user_id":"24","name":"筒","phone":"44444444444","cid":"44444444444","sex":"男","user_type":"值班人员"},{"user_id":"29","name":"李晓迪","phone":"15314283786","cid":"15314283786","sex":"男","user_type":"值班人员"},{"user_id":"31","name":"小明","phone":"17485236911","cid":"17485236911","sex":"男","user_type":"区域负责人"}]
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
         * user_id : 3
         * name : 小伙伴222
         * phone : 16666666666
         * cid : jawg
         * sex : 男
         * user_type : 区域负责人
         */

        private String user_id;
        private String name;
        private String phone;
        private String cid;
        private String sex;
        private String user_type;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

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

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }
    }
}

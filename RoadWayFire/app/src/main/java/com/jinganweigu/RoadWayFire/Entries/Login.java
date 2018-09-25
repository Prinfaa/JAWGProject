package com.jinganweigu.RoadWayFire.Entries;

public class Login {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"company_id":"330","user_id":"25","api_token":"8f673b2f105cc97987e4f3e07bdb0ce5","user_privilege":"0","app_key":"364242dd7adb44c1a6b932657142dcc8","app_secret":"2d3c566e986ed10db48c21b1307e1ba0"}
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
         * company_id : 330
         * user_id : 25
         * api_token : 8f673b2f105cc97987e4f3e07bdb0ce5
         * user_privilege : 0
         * app_key : 364242dd7adb44c1a6b932657142dcc8
         * app_secret : 2d3c566e986ed10db48c21b1307e1ba0
         */

        private String company_id;
        private String user_id;
        private String api_token;
        private String user_privilege;
        private String app_key;
        private String app_secret;

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getApi_token() {
            return api_token;
        }

        public void setApi_token(String api_token) {
            this.api_token = api_token;
        }

        public String getUser_privilege() {
            return user_privilege;
        }

        public void setUser_privilege(String user_privilege) {
            this.user_privilege = user_privilege;
        }

        public String getApp_key() {
            return app_key;
        }

        public void setApp_key(String app_key) {
            this.app_key = app_key;
        }

        public String getApp_secret() {
            return app_secret;
        }

        public void setApp_secret(String app_secret) {
            this.app_secret = app_secret;
        }
    }
}

package com.jinganweigu.entrysystem.entry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2018/3/24 0024.
 */

public class PersonStatisticsBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"first":[{"worker_name":"袁忠","send_back":"1","install":"0","uninstall":"0","all":"1"},{"worker_name":"贾工","send_back":"6","install":"0","uninstall":"1","all":"7"}],"second":[{"return":"7","install":"0","uninstall":"1","all":"8"}]}
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
        private List<FirstBean> first;
        private List<SecondBean> second;

        public List<FirstBean> getFirst() {
            return first;
        }

        public void setFirst(List<FirstBean> first) {
            this.first = first;
        }

        public List<SecondBean> getSecond() {
            return second;
        }

        public void setSecond(List<SecondBean> second) {
            this.second = second;
        }

        public static class FirstBean {
            /**
             * worker_name : 袁忠
             * send_back : 1
             * install : 0
             * uninstall : 0
             * all : 1
             */

            private String worker_name;
            private String send_back;
            private String install;
            private String uninstall;
            private String all;

            public String getWorker_name() {
                return worker_name;
            }

            public void setWorker_name(String worker_name) {
                this.worker_name = worker_name;
            }

            public String getSend_back() {
                return send_back;
            }

            public void setSend_back(String send_back) {
                this.send_back = send_back;
            }

            public String getInstall() {
                return install;
            }

            public void setInstall(String install) {
                this.install = install;
            }

            public String getUninstall() {
                return uninstall;
            }

            public void setUninstall(String uninstall) {
                this.uninstall = uninstall;
            }

            public String getAll() {
                return all;
            }

            public void setAll(String all) {
                this.all = all;
            }
        }

        public static class SecondBean {
            /**
             * return : 7
             * install : 0
             * uninstall : 1
             * all : 8
             */

            @SerializedName("return")
            private String returnX;
            private String install;
            private String uninstall;
            private String all;

            public String getReturnX() {
                return returnX;
            }

            public void setReturnX(String returnX) {
                this.returnX = returnX;
            }

            public String getInstall() {
                return install;
            }

            public void setInstall(String install) {
                this.install = install;
            }

            public String getUninstall() {
                return uninstall;
            }

            public void setUninstall(String uninstall) {
                this.uninstall = uninstall;
            }

            public String getAll() {
                return all;
            }

            public void setAll(String all) {
                this.all = all;
            }
        }
    }
}

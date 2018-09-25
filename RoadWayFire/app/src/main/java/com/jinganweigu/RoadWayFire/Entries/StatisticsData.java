package com.jinganweigu.RoadWayFire.Entries;

public class StatisticsData {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"com_name":"山东经安纬固消防科技有限公司","logo":"http://dndzl.cn/lane_occupancy/images/20180629103149.png","warning_num":"10","total_time":"54210","off_line":"1","normal":"1"}
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
         * com_name : 山东经安纬固消防科技有限公司
         * logo : http://dndzl.cn/lane_occupancy/images/20180629103149.png
         * warning_num : 10
         * total_time : 54210
         * off_line : 1
         * normal : 1
         */

        private String com_name;
        private String logo;
        private String warning_num;
        private String total_time;
        private String off_line;
        private String normal;

        public String getCom_name() {
            return com_name;
        }

        public void setCom_name(String com_name) {
            this.com_name = com_name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getWarning_num() {
            return warning_num;
        }

        public void setWarning_num(String warning_num) {
            this.warning_num = warning_num;
        }

        public String getTotal_time() {
            return total_time;
        }

        public void setTotal_time(String total_time) {
            this.total_time = total_time;
        }

        public String getOff_line() {
            return off_line;
        }

        public void setOff_line(String off_line) {
            this.off_line = off_line;
        }

        public String getNormal() {
            return normal;
        }

        public void setNormal(String normal) {
            this.normal = normal;
        }
    }
}

package com.jinganweigu.RoadWayFire.Entries;

import java.util.List;

public class StatisticAlarmCount {

    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"first":[{"hour":"1","count":"2"},{"hour":"2","count":"2"},{"hour":"3","count":"1"},{"hour":"4","count":"2"},{"hour":"5","count":"1"},{"hour":"6","count":"2"},{"hour":"7","count":"1"},{"hour":"8","count":"1"},{"hour":"9","count":"2"},{"hour":"10","count":"1"},{"hour":"11","count":"1"},{"hour":"12","count":"1"},{"hour":"13","count":"1"},{"hour":"14","count":"1"},{"hour":"15","count":"1"},{"hour":"16","count":"1"},{"hour":"17","count":"1"},{"hour":"18","count":"1"},{"hour":"19","count":"1"},{"hour":"20","count":"1"},{"hour":"21","count":"1"},{"hour":"22","count":"1"},{"hour":"23","count":"1"},{"hour":"24","count":"1"}],"second":[{"w_camera":"752866313","nums":"1"},{"w_camera":"752866316","nums":"2"},{"w_camera":"752866317","nums":"6"},{"w_camera":"752866319","nums":"1"}]}
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
             * hour : 1
             * count : 2
             */

            private String hour;
            private String count;

            public String getHour() {
                return hour;
            }

            public void setHour(String hour) {
                this.hour = hour;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }
        }

        public static class SecondBean {
            /**
             * w_camera : 752866313
             * nums : 1
             */

            private String w_camera;
            private String nums;

            public String getW_camera() {
                return w_camera;
            }

            public void setW_camera(String w_camera) {
                this.w_camera = w_camera;
            }

            public String getNums() {
                return nums;
            }

            public void setNums(String nums) {
                this.nums = nums;
            }
        }
    }
}

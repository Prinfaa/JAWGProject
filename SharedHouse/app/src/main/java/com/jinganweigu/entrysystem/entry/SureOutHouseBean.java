package com.jinganweigu.entrysystem.entry;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class SureOutHouseBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"first_line":"设备ID","second_line":"000000000000001"},{"first_line":"设备类型","second_line":"水压水位监测"},{"first_line":"设备状态","second_line":"成品预入库"},{"first_line":"仓库名","second_line":""},{"first_line":"入库人员","second_line":"房刚"},{"first_line":"入库时间","second_line":"2018-03-19 09:54:47"},{"first_line":"出库时间","second_line":""},{"first_line":"出库人员","second_line":""},{"first_line":"领用人员","second_line":""},{"first_line":"单位名称","second_line":""},{"first_line":"安装位置","second_line":""}]
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
         * first_line : 设备ID
         * second_line : 000000000000001
         */

        private String first_line;
        private String second_line;

        public String getFirst_line() {
            return first_line;
        }

        public void setFirst_line(String first_line) {
            this.first_line = first_line;
        }

        public String getSecond_line() {
            return second_line;
        }

        public void setSecond_line(String second_line) {
            this.second_line = second_line;
        }
    }
}

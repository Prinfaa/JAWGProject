package com.jinganweigu.entrysystem.entry;

import java.util.List;

public class ExamineDeviceNumBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : [{"in_advance":"8","out_advance":"0","waste_advance":"0"}]
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
         * in_advance : 8
         * out_advance : 0
         * waste_advance : 0
         */

        private String in_advance;
        private String out_advance;
        private String waste_advance;

        public String getIn_advance() {
            return in_advance;
        }

        public void setIn_advance(String in_advance) {
            this.in_advance = in_advance;
        }

        public String getOut_advance() {
            return out_advance;
        }

        public void setOut_advance(String out_advance) {
            this.out_advance = out_advance;
        }

        public String getWaste_advance() {
            return waste_advance;
        }

        public void setWaste_advance(String waste_advance) {
            this.waste_advance = waste_advance;
        }
    }
}

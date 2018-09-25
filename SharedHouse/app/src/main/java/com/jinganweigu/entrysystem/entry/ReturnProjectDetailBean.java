package com.jinganweigu.entrysystem.entry;

import java.util.List;

public class ReturnProjectDetailBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"nice":[{"chip_IMEI":"000000000000047","mitu_type":"车载报警","return_time_nice":"1522129385"}],"bad":[{"chip_IMEI":"100000000000000","mitu_type":"水压水位监测","return_time_bad":"1522477213"}]}
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
        private List<NiceBean> nice;
        private List<BadBean> bad;

        public List<NiceBean> getNice() {
            return nice;
        }

        public void setNice(List<NiceBean> nice) {
            this.nice = nice;
        }

        public List<BadBean> getBad() {
            return bad;
        }

        public void setBad(List<BadBean> bad) {
            this.bad = bad;
        }

        public static class NiceBean {
            /**
             * chip_IMEI : 000000000000047
             * mitu_type : 车载报警
             * return_time_nice : 1522129385
             */

            private String chip_IMEI;
            private String mitu_type;
            private String return_time_nice;

            public String getChip_IMEI() {
                return chip_IMEI;
            }

            public void setChip_IMEI(String chip_IMEI) {
                this.chip_IMEI = chip_IMEI;
            }

            public String getMitu_type() {
                return mitu_type;
            }

            public void setMitu_type(String mitu_type) {
                this.mitu_type = mitu_type;
            }

            public String getReturn_time_nice() {
                return return_time_nice;
            }

            public void setReturn_time_nice(String return_time_nice) {
                this.return_time_nice = return_time_nice;
            }
        }

        public static class BadBean {
            /**
             * chip_IMEI : 100000000000000
             * mitu_type : 水压水位监测
             * return_time_bad : 1522477213
             */

            private String chip_IMEI;
            private String mitu_type;
            private String return_time_bad;

            public String getChip_IMEI() {
                return chip_IMEI;
            }

            public void setChip_IMEI(String chip_IMEI) {
                this.chip_IMEI = chip_IMEI;
            }

            public String getMitu_type() {
                return mitu_type;
            }

            public void setMitu_type(String mitu_type) {
                this.mitu_type = mitu_type;
            }

            public String getReturn_time_bad() {
                return return_time_bad;
            }

            public void setReturn_time_bad(String return_time_bad) {
                this.return_time_bad = return_time_bad;
            }
        }
    }
}

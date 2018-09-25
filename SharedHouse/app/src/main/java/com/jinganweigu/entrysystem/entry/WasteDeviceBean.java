package com.jinganweigu.entrysystem.entry;

import java.util.List;

public class WasteDeviceBean {


     /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"total":[{"mitu_type":"一键报警","quantity":"0"},{"mitu_type":"气体探测","quantity":"0"},{"mitu_type":"水压水位监测","quantity":"0"},{"mitu_type":"火焰探测","quantity":"0"},{"mitu_type":"车载报警","quantity":"2"}],"particular":[{"chip_IMEI":"999998888877777","receive_unit":null,"waste_time":"2018/04/04","return_reason":null},{"chip_IMEI":"000000000000047","receive_unit":"k99","waste_time":"2018/03/31","return_reason":"不稳定"}]}
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
        private List<TotalBean> total;
        private List<ParticularBean> particular;

        public List<TotalBean> getTotal() {
            return total;
        }

        public void setTotal(List<TotalBean> total) {
            this.total = total;
        }

        public List<ParticularBean> getParticular() {
            return particular;
        }

        public void setParticular(List<ParticularBean> particular) {
            this.particular = particular;
        }

        public static class TotalBean {
            /**
             * mitu_type : 一键报警
             * quantity : 0
             */

            private String mitu_type;
            private String quantity;

            public String getMitu_type() {
                return mitu_type;
            }

            public void setMitu_type(String mitu_type) {
                this.mitu_type = mitu_type;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }
        }

        public static class ParticularBean {
            /**
             * chip_IMEI : 999998888877777
             * receive_unit : null
             * waste_time : 2018/04/04
             * return_reason : null
             */

            private String chip_IMEI;
            private Object receive_unit;
            private String waste_time;
            private Object return_reason;

            public String getChip_IMEI() {
                return chip_IMEI;
            }

            public void setChip_IMEI(String chip_IMEI) {
                this.chip_IMEI = chip_IMEI;
            }

            public Object getReceive_unit() {
                return receive_unit;
            }

            public void setReceive_unit(Object receive_unit) {
                this.receive_unit = receive_unit;
            }

            public String getWaste_time() {
                return waste_time;
            }

            public void setWaste_time(String waste_time) {
                this.waste_time = waste_time;
            }

            public Object getReturn_reason() {
                return return_reason;
            }

            public void setReturn_reason(Object return_reason) {
                this.return_reason = return_reason;
            }
        }
    }
}

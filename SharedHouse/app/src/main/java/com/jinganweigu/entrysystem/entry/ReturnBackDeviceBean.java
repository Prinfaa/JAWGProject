package com.jinganweigu.entrysystem.entry;

import java.util.List;

public class ReturnBackDeviceBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"particular":[{"receive_unit":"","damage":"7","normal":"10"},{"receive_unit":"k88名泉广场","damage":"0","normal":"0"},{"receive_unit":"k99","damage":"1","normal":"0"},{"receive_unit":"山东经安纬固消防科技有限公司","damage":"0","normal":"0"}],"total":[{"damages":"8","normals":"10","all":"18"}]}
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
        private List<ParticularBean> particular;
        private List<TotalBean> total;

        public List<ParticularBean> getParticular() {
            return particular;
        }

        public void setParticular(List<ParticularBean> particular) {
            this.particular = particular;
        }

        public List<TotalBean> getTotal() {
            return total;
        }

        public void setTotal(List<TotalBean> total) {
            this.total = total;
        }

        public static class ParticularBean {
            /**
             * receive_unit :
             * damage : 7
             * normal : 10
             */

            private String receive_unit;
            private String damage;
            private String normal;

            public String getReceive_unit() {
                return receive_unit;
            }

            public void setReceive_unit(String receive_unit) {
                this.receive_unit = receive_unit;
            }

            public String getDamage() {
                return damage;
            }

            public void setDamage(String damage) {
                this.damage = damage;
            }

            public String getNormal() {
                return normal;
            }

            public void setNormal(String normal) {
                this.normal = normal;
            }
        }

        public static class TotalBean {
            /**
             * damages : 8
             * normals : 10
             * all : 18
             */

            private String damages;
            private String normals;
            private String all;

            public String getDamages() {
                return damages;
            }

            public void setDamages(String damages) {
                this.damages = damages;
            }

            public String getNormals() {
                return normals;
            }

            public void setNormals(String normals) {
                this.normals = normals;
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

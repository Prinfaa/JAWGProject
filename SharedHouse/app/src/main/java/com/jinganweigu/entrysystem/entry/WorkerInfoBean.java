package com.jinganweigu.entrysystem.entry;

import java.util.List;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class WorkerInfoBean {


    /**
     * code : 200
     * msg : 操作成功，请求成功!
     * result : {"people":[{"id":"1","name":"袁忠"},{"id":"2","name":"贾工"}],"company":[{"projects":"济南居然之家北园店"},{"projects":"K88名泉广场"},{"projects":"山东经安纬固消防科技有限公司"},{"projects":"柳行社区"},{"projects":"黄台家居广场"},{"projects":"黄台电子商务产业园"}]}
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
        private List<PeopleBean> people;
        private List<CompanyBean> company;

        public List<PeopleBean> getPeople() {
            return people;
        }

        public void setPeople(List<PeopleBean> people) {
            this.people = people;
        }

        public List<CompanyBean> getCompany() {
            return company;
        }

        public void setCompany(List<CompanyBean> company) {
            this.company = company;
        }

        public static class PeopleBean {
            /**
             * id : 1
             * name : 袁忠
             */

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        public static class CompanyBean {
            /**
             * projects : 济南居然之家北园店
             */

            private String projects;

            public String getProjects() {
                return projects;
            }

            public void setProjects(String projects) {
                this.projects = projects;
            }
        }
    }
}

package com.jinganweigu.entrysystem.entry;

/**
 * Created by Administrator on 2018/3/3 0003.
 */

public class EnterStaisticsBean {

    String in;
    String out;
    String returns;

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getReturns() {
        return returns;
    }

    public void setReturns(String returns) {
        this.returns = returns;
    }

    @Override
    public String toString() {
        return "EnterStaisticsBean{" +
                "in='" + in + '\'' +
                ", out='" + out + '\'' +
                ", returns='" + returns + '\'' +
                '}';
    }
}

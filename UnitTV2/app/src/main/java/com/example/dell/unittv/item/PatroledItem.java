package com.example.dell.unittv.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class PatroledItem {
    private String cardNo;
    private String Position;
    private String times;


    public PatroledItem(String cardNo, String Position, String times) {
        setCardNo(cardNo);
        setPosition(Position);
        setTimes(times);
    }

    private void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNo() {
        return cardNo;
    }




    public String getPosition() {
        return Position;
    }

    public void setPosition(String Position) {
        this.Position = Position;
    }


    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }


}


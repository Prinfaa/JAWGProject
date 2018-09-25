package com.example.dell_pc.qr_code_patrol.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class PatrolItem {
    private String cardNo;
    private String Position;
    private String dateTime;


    public PatrolItem(String cardNo, String Position, String dateTime) {
        setCardNo(cardNo);
        setPosition(Position);
        setDateTime(dateTime);
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


    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


}


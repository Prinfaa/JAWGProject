package com.example.dell_pc.unitpad.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class UnPatrolItem {
    private String cardNo;
    private String Position;


    public UnPatrolItem(String cardNo, String Position) {
        setCardNo(cardNo);
        setPosition(Position);
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




}


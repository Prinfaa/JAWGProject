package com.example.dell_pc.unitpad.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class CheckCardItem {

    private String cardNo;
    private String Position;
    private String Fac;

    public CheckCardItem(String cardNo, String Position, String Fac) {
        setCardNo(cardNo);
        setPosition(Position);
        setFac(Fac);
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPosition() { return Position;}

    public void setPosition(String Position) {
        this.Position = Position;
    }

    public String getFac() { return Fac;}

    public void setFac(String Fac) {
        this.Fac = Fac;
    }


    }




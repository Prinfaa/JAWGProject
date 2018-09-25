package com.example.dell_pc.qr_code_patrol.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class UnCheckItem {
    private String cardNo;
    private String Position;
    private String fac;


    public UnCheckItem(String cardNo, String Position, String fac) {
        setCardNo(cardNo);
        setPosition(Position);
        setFac(fac);
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

    public String getFac() {
        return fac;
    }

    public void setFac(String fac) {
        this.fac = fac;
    }
}


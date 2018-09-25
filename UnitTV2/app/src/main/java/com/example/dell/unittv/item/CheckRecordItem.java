package com.example.dell.unittv.item;


import com.example.dell.unittv.CheckedCard;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class CheckRecordItem extends CheckedCard {

    private String cardNo;
    private String time;
    private String Note;
    private String picUrl;
    private String Statue;



    public CheckRecordItem(String cardNo, String time, String Note, String picUrl, String Statue) {
        setCardNo(cardNo);
        setTime(time);
        setNote(Note);
        setPicUrl(picUrl);
        setStatue(Statue);
        //setFac(Fac);
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getStatue(){
        return Statue;
    }

    public void setStatue(String Statue){
        this.Statue = Statue;
    }

}




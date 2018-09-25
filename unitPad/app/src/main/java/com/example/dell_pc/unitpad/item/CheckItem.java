package com.example.dell_pc.unitpad.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class CheckItem {
    private String cardNo;
    private String Position;
    private String dateTime;
    private String imageName;
    private String Fac;
    private String Note;
    private String Statue;


    public CheckItem(String cardNo, String Position, String dateTime, String imageName, String Fac, String Note, String Statue) {
        setCardNo(cardNo);
        setPosition(Position);
        setDateTime(dateTime);
        setImageName(imageName);
        setFac(Fac);
        setNote(Note);
        setStatue(Statue);

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

    public String getImageName(){
        return imageName;
    }

    public void setImageName(String imageName){
        this.imageName = imageName;
    }

    public String getFac(){
        return Fac;
    }

    public void setFac(String Fac){
        this.Fac = Fac;
    }

    public String getNote(){
        return Note;
    }

    public void setNote(String Note){
        this.Note = Note;
    }

    public String getStatue(){
        return Statue;
    }

    public void setStatue(String Statue){
        this.Statue = Statue;
    }

}


package com.example.dell_pc.qr_code_patrol.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class DealItem {
    private String cardNo;
    private String recordID;
    private String timestamp;
    private String imageName;


    public DealItem(String cardNo, String recordID, String timestamp, String imageName) {
        setCardNo(cardNo);
        setRecordID(recordID);
        setTimestamp(timestamp);
        setImageName(imageName);
    }

    private void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageName(){
        return imageName;
    }

    public void setImageName(String imageName){
        this.imageName = imageName;
    }

    public String getRecordID() {
        return recordID;
    }

    public void setRecordID(String recordID) {
        this.recordID = recordID;
    }
}


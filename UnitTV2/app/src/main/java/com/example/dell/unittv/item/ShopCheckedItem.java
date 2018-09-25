package com.example.dell.unittv.item;

/**
 * Created by dell-pc on 2016/2/2.
 */
public class ShopCheckedItem {
    private String cardNo;
    private String shopID;
    private String parentID;
    private String timestamp;
    private String Statue;


    public ShopCheckedItem(String cardNo, String shopID, String parentID, String timestamp, String Statue) {
        setCardNo(cardNo);
        setShopID(shopID);
        setParentID(parentID);
        setTimestamp(timestamp);
        setStatue(Statue);

    }

    private void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getParentID() {
        return parentID;
    }

    public String getShopID() {
        return shopID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatue(){
        return Statue;
    }

    public void setStatue(String Statue){
        this.Statue = Statue;
    }

}

